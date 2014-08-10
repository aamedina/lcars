(ns lcars.ui)

(def ^:dynamic *system* :primary)

(defprotocol Displayable
  (display [this]))

(defprotocol Clickable
  (click [this]))

(defprotocol Hoverable
  (hover [this]))

(defrecord Button [x y width height]
  Displayable
  (display [this])

  Clickable
  (click [this])

  Hoverable
  (hover [this]))

(defn button
  [on-click on-hover]
  (Button. 0 0 150 60))
