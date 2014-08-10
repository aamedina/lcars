(ns lcars.colors
  (:require [quil.core :as q]))

(def off-white [0xff 0xff 0xcc])
(def light-purple [0xA5 0x8E 0xC6])
(def purple [0x76 0x58 0x97])
(def dark-purple [0x66 0x3A 0x58])
(def orange [0xE5 0x80 0x21])
(def orange-yellow [0xff 0xcc 0x66])
(def yellow [0xff 0xff 0x99])
(def brown [0xE8 0xA1 0x5F])
(def light-blue [0x99 0xcc 0xff])
(def blue [0x33 0x66 0xcc])
(def dark-blue [0x00 0x66 0x99])
(def pink [0xf1 0xb1 0xaf])
(def red [0xcc 0x66 0x66])
(def offline-red [0xff 0x00 0x00])
(def offline-black [0x33 0x00 0x00])

(def primary-systems
  {:primary light-blue 
   :secondary yellow
   :tertiary off-white
   :elbows orange-yellow})

(def secondary-systems
  {:primary light-purple
   :secondary orange-yellow
   :tertiary yellow
   :elbows brown})

(def ancillary-systems
  {:primary light-purple
   :secondary yellow
   :tertiary dark-purple
   :elbows pink})

(def database-systems
  {:primary light-blue
   :secondary orange
   :tertiary orange-yellow
   :elbows red})

(defn fill
  [system color]
  (let [[r g b] (case system
                  :primary (primary-systems color)
                  :secondary (secondary-systems color)
                  :ancillary (ancillary-systems color)
                  :database (database-systems color))]
    (q/fill r g b)))
