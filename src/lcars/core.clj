(ns lcars.core
  (:gen-class)
  (:require [quil.core :as q :refer :all]
            [quil.middleware :as m]
            [lcars.colors :as colors]
            [lcars.ui :as ui]
            [clojure.tools.logging :as log]))

(defn rectangular-button
  [button-text]
  (colors/fill :primary)
  (rect 0 0 150 60)
  (fill 0)
  (text-size 25)
  (text-align :right)
  (text button-text 140 50))

(defn standard-button
  [button-text]
  (colors/fill :primary)
  (arc 50 40 75 60 (/ PI 2) (/ (* PI 3) 2))
  (fill 0)
  (text-size 25)
  (text-align :right)
  (text button-text 140 50))

(defn cap
  [x y orientation]
  (colors/fill :secondary)
  (case orientation
    :left (do (arc x y 60 60 (/ PI 2) (/ (* PI 3) 2))
              (rect (+ x 30) y 30 60))
    :right (do (arc x y 60 60 (/ (* PI 3) 2) (/ (* PI 5) 2))
               (rect x y 30 60))))

(defn anchor
  [anchor-type title]
  (case anchor-type
    :header (let []              
              (cap 10 10 :left)
              (colors/fill :primary)
              (rect 80 10 (- (width) 170 (text-width title)) 60)
              (colors/fill :tertiary)
              (text-size 60)
              (text-align :right)
              (text title (- (width) 80) (+ (text-ascent) 5))
              (cap (- (width) 70) 10 :right))
    :footer (let []
              (cap 10 (- (height) 70) :left)
              (text-size 60)
              (colors/fill :primary)
              (rect (+ (text-width title) 90)
                    (- (height) 70)
                    (- (width) 170 (text-width title))
                    60)
              (colors/fill :tertiary)
              (text-align :left)
              (text title 80 (- (height) 20))
              (cap (- (width) 70) (- (height) 70) :right))))

(defn center-shape
  [sh]
  (shape sh
         (/ (- (width) (.-width sh)) 2)
         (/ (- (height) (.-height sh)) 2)))

(defmacro with-frame
  [title subtitle system & body]
  `(binding [ui/*system* ~system]
     (anchor :header ~title)
     ~@body
     (anchor :footer ~subtitle)))

(defn start-up-sequence
  [{:keys [emblem x y] :as state}]
  (with-frame "LCARS CONSOLE" "LCARS CONSOLE" :primary
    (text-align :center)
    (colors/fill :primary)
    (shape emblem
           (/ (- (width) (.-width emblem)) 2)
           (- (/ (- (height) (.-height emblem)) 2) 80))
    (text-size 80)
    (text "THE LCARS COMPUTER NETWORK"
          (/ (width) 2)
          (- (+ (/ (+ (height) (.-height emblem)) 2) (text-ascent)) 80))
    (text-size 25)
    (text "AUTHORIZED ACCESS ONLY \u2022 SYSTEM AVAILABLE"
          (/ (width) 2)
          (+ (/ (+ (height) (.-height emblem)) 2) 25))

    (text (pr-str [x y])
          (/ (width) 2)
          (+ (/ (+ (height) (.-height emblem)) 2) 50))))

(defn setup
  []
  (log/debug "SETUP")
  (ellipse-mode :corner)
  (no-stroke)
  (text-font (create-font "Helvetica LT UltraCompressed" 80 true))
  (colors/fill :primary)
  {:emblem (load-shape "img/UFP_Emblem.svg")
   :x 0 :y 0})

(defn update
  [state]
  state)

(defn draw
  [state]
  (background 0)
  (start-up-sequence state))

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
  [state {:keys [x y button]}]
  (assoc state
    :x x
    :y y))

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

(defn key-typed
  [state {:keys [key key-code raw-key]}]
  (assoc state
    :key key
    :key-code key-code
    :raw-key raw-key))

(defn on-close
  [state]
  (log/debug "ON CLOSE"))

(defsketch LCARS
  :title "Library Computer Access/Retrieval System"
  :size [1440 800]
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
  :middleware [m/fun-mode])

(defn -main
  [& args])
