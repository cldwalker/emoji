(ns emoji.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [emoji.core :refer :all]))

(deftest copy-images-test
  (copy-images)
  (is (.isDirectory (io/file images-dir)))
  (let [names (emoji-names)]
    (is (.endsWith (first names) ".png"))
    (is (> (count names) 800)))
  (sh/sh "rm" "-rf" "resources"))

;; requires copy-images to have created images
(deftest emoji-response-test
  (testing "substitutes :name: correctly"
    (is (.contains
         (:body (emoji-response {:body "This is on :fire:"}))
         "/images/emoji/fire.png")))
  (testing "with wild option substitutes multiple names correctly"
    (is (re-find
         #"/images/emoji/on.png.*/images/emoji/fire.png"
         (:body (emoji-response {:body "This is on fire"} :wild true))))))