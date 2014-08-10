(ns lcars.core
  (:gen-class)
  (:require [quil.core :as q :refer :all]
            [quil.middleware :as m]))

(def color-scheme
  {:off-white [0xcc 0xdd 0xff]
   :light-blue [0x55 0x99 0xff]
   :primary-blue [0x33 0x66 0xff]
   :secondary-blue [0x00 0x11 0xee]
   :darker-blue [0x00 0x00 0x88]
   :yellow [0xbb 0xaa 0x55]
   :orange [0xbb 0x44 0x11]
   :red [0x88 0x22 0x11]})

(defn rectangular-button
  [button-text]
  (apply fill (:primary-blue color-scheme))
  (rect 0 0 150 60)
  (fill 0)
  (text-size 25)
  (text-align :right)
  (text button-text 140 50))

(defn standard-button
  [button-text]
  (apply fill (:primary-blue color-scheme))
  (arc 150 60 150 60 PI (* 2 PI) :chord)
  (fill 0)
  (text-size 25)
  (text-align :right)
  (text button-text 140 50))

(defn anchor
  [anchor-type title]
  (case anchor-type
    :header (let []
              (standard-button "")
              (apply fill (:light-blue color-scheme))
              (text-size 40)
              (text-align :right)
              (text title (- (width) 10) (text-ascent)))
    :footer (let []
              (apply fill (:light-blue color-scheme))
              (text-size 40)
              (text-align :left)
              (text title 10 (- (height) 10)))))

(defn center-shape
  [sh]
  (shape sh
         (/ (- (width) (.-width sh)) 2)
         (/ (- (height) (.-height sh)) 2)))

(defn start-up-sequence
  [{:keys [emblem] :as state}]
  (anchor :header "MAIN TITLE \u2022 ALIGNED RIGHT")
  (text-align :center)
  (shape emblem
         (/ (- (width) (.-width emblem)) 2)
         (- (/ (- (height) (.-height emblem)) 2) 80))
  (text-size 60)
  (text "THE LCARS COMPUTER NETWORK"
        (/ (width) 2)
        (- (+ (/ (+ (height) (.-height emblem)) 2) (text-ascent)) 80))
  (text-size 25)
  (text "AUTHORIZED ACCESS ONLY \u2022 SYSTEM AVAILABLE"
        (/ (width) 2)
        (- (+ (/ (+ (height) (.-height emblem)) 2) (text-ascent)) 10))
  (anchor :footer "SUBTITLE \u2022 ALIGNED LEFT"))

(defn setup
  []
  (smooth)
  {:font (create-font "Helvetica LT UltraCompressed" 60 true)
   :emblem (load-shape "img/UFP_Emblem.svg")})

(defn update
  [state]
  state)

(defn draw
  [state]
  (background 0)
  (text-font (:font state))
  (apply fill (:light-blue color-scheme))
  (start-up-sequence state))

(defsketch LCARS
  :title "Library Computer Access/Retrieval System"
  :size [1440 800]
  :setup setup
  :update update
  :draw draw
  :renderer "processing.core.PGraphicsRetina2D"
  :middleware [m/fun-mode])

(defn -main
  [& args])
