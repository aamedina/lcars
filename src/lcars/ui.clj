(ns lcars.ui)

(def ^:dynamic *system* :primary)

(defprotocol Displayable
  (display [this]))

(defprotocol Clickable
  (click [this]))

(defprotocol Hoverable
  (hover [this]))
