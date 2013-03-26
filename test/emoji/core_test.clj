(ns emoji.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [emoji.core :refer :all]))

(deftest images-copy-correctly-test
  (copy-images)
  (is (.isDirectory (io/file images-dir)))
  (let [names (emoji-names)]
    (is (.endsWith (first names) ".png"))
    (is (> (count names) 800)))
  (sh/sh "rm" "-rf" emoji.core/images-dir))
