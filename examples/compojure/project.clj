(defproject emoji-compojure "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [compojure "1.1.5"]
                 [emoji "0.1.0"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler emoji.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
