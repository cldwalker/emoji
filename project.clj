(defproject emoji "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "http://github.com/cldwalker/emoji"
  :license {:name "The MIT License"
            :url "https://en.wikipedia.org/wiki/MIT_License"}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:1.5 {:dependencies [[org.clojure/clojure "1.5.0-RC1"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.5"]})
