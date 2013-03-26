(ns emoji.core
  (:require [clojure.java.shell :as sh]
            [me.raynes.fs :as fs]))

(def images-dir "resources/public/images")

(defn emoji-names
  []
  (fs/list-dir images-dir))

(defn copy-images
  ([] (copy-images images-dir))
  ([dir]
     (println "Cloning github/gemoji...")
     (sh/sh "git" "clone" "git://github.com/github/gemoji.git")
     (println "Copying images...")
     (fs/copy-dir "gemoji/images/emoji" dir)
     (sh/sh "rm" "-rf" "gemoji")))

(defn -main [& args]
  (apply copy-images args))