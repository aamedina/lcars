(defproject lcars "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.319.0-6b1aca-alpha"]
                 [quil "2.2.1"]]
  :main ^:skip-aot lcars.core
  :profiles {:uberjar {:aot :all}})
