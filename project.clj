(defproject emoji "0.1.0-SNAPSHOT"
  :description "This library provides middleware/interceptorware to replace a response
containing emoji names with bundled emoji images."
  :url "http://github.com/cldwalker/emoji"
  :license {:name "The MIT License"
            :url "https://en.wikipedia.org/wiki/MIT_License"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [me.raynes/fs "1.4.0"]]
  :min-lein-version "2.0.0"
  :aliases {"all" ["with-profile" "dev"]})
