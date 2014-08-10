(ns lcars.core
  (:gen-class)
  (:require [quil.core :as q :refer :all]
            [quil.middleware :as m]
            [lcars.colors :as colors]))

(defn rectangular-button
  [button-text]
  (colors/fill :primary :primary)
  (rect 0 0 150 60)
  (fill 0)
  (text-size 25)
  (text-align :right)
  (text button-text 140 50))

(defn standard-button
  [button-text]
  (colors/fill :primary :primary)
  (arc 50 40 75 60 (/ PI 2) (/ (* PI 3) 2))
  (fill 0)
  (text-size 25)
  (text-align :right)
  (text button-text 140 50))

(defn cap
  [x y orientation]
  (colors/fill :primary :primary)
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
              (rect 80 10 (- (width) 170 (text-width title)) 60)
              (colors/fill :primary :secondary)
              (text-size 60)
              (text-align :right)
              (text title (- (width) 80) (+ (text-ascent) 5))
              (cap (- (width) 70) 10 :right))
    :footer (let []
              (cap 10 (- (height) 70) :left)
              (text-size 60)
              (rect (+ (text-width title) 90)
                    (- (height) 70)
                    (- (width) 170 (text-width title))
                    60)
              (colors/fill :primary :secondary)
              (text-align :left)
              (text title 80 (- (height) 20))
              (cap (- (width) 70) (- (height) 70) :right))))

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
  (colors/fill :primary :primary)
  (no-stroke)
  (ellipse-mode :corner)
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
