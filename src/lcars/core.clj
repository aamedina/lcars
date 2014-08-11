(ns lcars.core
  (:gen-class)
  (:require [quil.core :as q :refer :all]
            [quil.middleware :as m]
            [lcars.colors :as colors]
            [lcars.ui :as ui]
            [clojure.set :as set]))

(defrecord Button [x y width height color hovered?]
  ui/Displayable
  (display [this]
    (if hovered?
      (fill colors/off-white)
      (colors/fill color))    
    (rect x y width height))

  ui/Clickable
  (click [this]
    (println "I'm clicked!"))

  ui/Hoverable
  (hover [this]
    (assoc this
      :hovered? (not hovered?))))

(defn button
  [x y width height]
  (Button. x y 150 60 :primary false))

(defn responsive-width
  [w]
  (let [proportion (/ (width) (screen-width))]
    (* proportion w)))

(defn responsive-height
  [h]
  (let [proportion (/ (height) (screen-height))]
    (* proportion h)))

(defn button-group
  [x y n]
  (text-font (create-font "Helvetica LT UltraCompressed" 80 true))
  (text-align :right)
  (let [[w h] [(responsive-width 150) (responsive-height 60)]]
    (doseq [[h y color] (reduce (fn [xs [h color]]
                            (conj xs [h (if-let [[p-h p-y] (peek xs)]
                                          (+ p-y p-h 5)
                                          y) color]))
                                [] (->> (interleave (iterate #(* 2 %) h)
                                                    (colors/color-stream))
                                        (partition 2)
                                        (take n)))]
      (colors/fill color)
      (rect x y w h)
      (fill 0)
      (text-size (responsive-width (ceil 25)))
      (text "LCARS 49-2931" (+ x w) (- (+ y h) 5)))))

(defn top-elbow
  [x y]
  (button-group x y 2)
  (colors/fill :elbows)
  (let [[w h bar-height] [(responsive-width 150)
                          (responsive-height 92.5)
                          (responsive-height 30)]
        y (+ y (+ w bar-height) 10)]
    (arc x (- y (/ w 2)) w w 0 PI)
    (rect (+ x (/ w 2)) y (/ w 2) (/ w 2))
    (rect (+ x w) (- (+ y (/ w 2)) bar-height) w bar-height)
    (colors/fill :tertiary)
    (rect (+ x (* w 2) 5) (- (+ y (/ w 2)) bar-height)
          (- (width) (+ 35 (* w 2)) 30) bar-height)
    (colors/fill :elbows)
    (rect (+ x (* w 2) 10 (- (width) (+ 35 (* w 2)) 30))
          (- (+ y (/ w 2)) bar-height) (/ w 4) bar-height)
    (ellipse-mode :center)
    (ellipse (+ x w) (+ y (- (/ w 2) bar-height)) (/ w 4) (/ w 4))
    (fill 0)
    (ellipse-mode :corner)
    (ellipse (+ x w) y (- (/ w 2) bar-height) (- (/ w 2) bar-height))))

(defn bottom-elbow
  [x y]
  (let [[w h bar-height] [(responsive-width 150)
                          (responsive-height 92.5)
                          (responsive-height 30)]]
    (colors/fill :tertiary)
    (arc x y w w PI (* 2 PI))
    (rect x (floor (+ y (/ w 2))) w w)
    (rect (+ x (/ w 2)) y (/ w 2) (/ w 2))
    (rect (+ x w) y w bar-height)
    (let [x (+ x (* w 3) 5)]
      (colors/fill :elbows)
      (rect (- x w) y (* w 2) bar-height)
      (rect (+ x w) y (/ w 2) (/ w 2))      
      (rect (+ x w) (floor (+ y (/ w 2))) w w)
      (arc (+ x w) y w w PI (* 2 PI))
      (ellipse-mode :center)
      (ellipse (+ x w) (+ y bar-height) (/ w 4) (/ w 4))
      (fill 0)
      (ellipse-mode :corner)
      (ellipse (- (+ x w) (- (/ w 2) bar-height))
               (+ y bar-height) (- (/ w 2) bar-height)
               (- (/ w 2) bar-height))
      (button-group (+ x w) (+ y 5 (floor (* 1.5 w))) 3)
      (fill 0)
      (rect (+ x w) (- (height) 5) w 5))
    (colors/fill :tertiary)
    (ellipse-mode :center)
    (ellipse (+ x w) (+ y bar-height) (/ w 4) (/ w 4))
    (fill 0)
    (ellipse-mode :corner)
    (ellipse (+ x w) (+ y bar-height) (- (/ w 2) bar-height)
             (- (/ w 2) bar-height))
    (colors/fill :secondary)
    (button-group x (+ y 5 (floor (* 1.5 w))) 3)
    (fill 0)
    (rect x (- (height) 5) w 5)))

(defmacro with-elbow
  [title & content]
  `(binding [ui/*system* :primary]
     ))

(defn logo
  ([state x y] (logo state x y (responsive-width (ceil 120))))
  ([state x y font-size]
     (colors/fill :primary)
     (text-align :right)
     (text-font (get-in state [:fonts :title]))
     (text-size font-size)
     (text "STAR" (- x (text-width "TREK")) y)
     (text "TREK" (- x 12.5) (+ y 38))
     (text-size (responsive-width (ceil 58)))
     (text "The Final Frontier" (- x 15) (+ y 90))))

(defn menu-buttons
  [state x y]
  (let [[w h] [(responsive-width 150) (responsive-width 60)]
        [x y] [(+ x w) (+ y h 20)]
        button-texts ["New Game" "Load Game" "Options" "Exit"]]
    (text-align :center)
    (colors/fill :primary)
    (text-size (responsive-width (ceil 60)))
    (doseq [[button-text n] (partition 2 (interleave button-texts (range)))]
      (text button-text (+ x (* 1.5 w)) (+ y (* 60 n) (* n h) 60)))))

(defn setup
  []
  (ellipse-mode :corner)
  (no-stroke)
  (text-font (create-font "Helvetica LT UltraCompressed" 80 true))
  (colors/fill :primary)
  {:emblem (load-shape "img/UFP_Emblem.svg")
   :fonts {:lcars (create-font "Helvetica LT UltraCompressed" 60 true)
           :title (create-font "Federation" 60 true)
           :logo (create-font "Krupper" 60 true)
           :body (create-font "Nova Light Ultra SSi" 60 true)}
   :x 0 :y 0
   :components #{}})

(defn update
  [state]
  state)

(defn draw
  [{:keys [emblem x y components] :as state}]
  (let [[top-x top-y] [5 5]
        [bottom-x bottom-y] [top-x (+ top-y (responsive-width 270))]]
    (background 0)
    (doseq [component components]
      (ui/display component))
    (logo state (- (width) 40) 100)
    (top-elbow top-x top-y)
    (bottom-elbow bottom-x bottom-y)
    (text-font (get-in state [:fonts :lcars]))
    (menu-buttons state bottom-x bottom-y)
    (shape emblem
           (- (width) (.-width emblem) 40)
           (- (height) (.-height emblem) 100))
    (colors/fill :primary)
    (text-align :left)
    (text "UNITED FEDERATION OF PLANETS"
          (- (width) (.-width emblem))
          (- (height) 30))))

(defn focus-gained
  [state]
  state)

(defn focus-lost
  [state]
  state)

(defn mouse-entered
  [state {:keys [x y]}]
  (assoc state
    :x x
    :y y))

(defn mouse-exited
  [state {:keys [x y]}]
  (assoc state
    :x x
    :y y))

(defn mouse-pressed
  [state {:keys [x y button]}]
  (assoc state
    :x x
    :y y
    :button button))

(defn mouse-released
  [state {:keys [x y]}]
  (assoc state
    :x x
    :y y))

(defn mouse-clicked
  [state {:keys [x y]}]
  (-> state
      (assoc :x x :y y)
      (update-in [:components] conj (button x y 150 60))))

(defn mouse-moved
  [state {:keys [x y p-x p-y]}]
  (assoc state
    :x x
    :y y
    :p-x p-x
    :p-y p-y))

(defn mouse-dragged
  [state {:keys [x y p-x p-y button]}]
  (assoc state
    :x x
    :y y
    :p-x p-x
    :p-y p-y))

(defn mouse-wheel
  [state event]
  (assoc state
    :mouse-wheel event))

(defn key-pressed
  [state {:keys [key key-code raw-key]}]
  (assoc state
    :key key
    :key-code key-code
    :raw-key raw-key))

(defn key-released
  [state]
  state)

(defmulti key-typed (fn [state {:keys [key key-code raw-key]}] raw-key))

(defmethod key-typed \`
  [state {:keys [key key-code raw-key]}]
  state)

(defmethod key-typed :default
  [state {:keys [key key-code raw-key]}]
  (assoc state
    :key key
    :key-code key-code
    :raw-key raw-key))

(defn on-close
  [state]
  (println "on-close"))

(defsketch LCARS
  :title "Library Computer Access/Retrieval System"
  :size [1280 800]
  :setup setup
  :update update
  :draw draw
  :focus-gained focus-gained
  :focus-lost focus-lost
  :mouse-entered mouse-entered
  :mouse-exited mouse-exited
  :mouse-pressed mouse-pressed
  :mouse-released mouse-released
  :mouse-clicked mouse-clicked
  :mouse-moved mouse-moved
  :mouse-dragged mouse-dragged
  :mouse-wheel mouse-wheel
  :key-pressed key-pressed
  :key-released key-released
  :key-typed key-typed
  :on-close on-close
  :middleware [m/fun-mode]
  ;; :renderer "processing.core.PGraphicsRetina2D"
  :features [:resizable])

(defn -main
  [& args])
