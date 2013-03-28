(ns emoji.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [clojure.java.shell :as sh]
            [emoji.core :refer :all]))

(deftest copy-images-test
  (let [dir ".images/emoji"]
    (copy-images dir)
    (is (.isDirectory (io/file dir)))
    (let [names (fs/list-dir dir)]
      (is (.endsWith (first names) ".png"))
      (is (= (count names) 885)))
    (is (not (.isDirectory (io/file (str dir "/unicode")))))
    (sh/sh "rm" "-rf" dir)))

(deftest emoji-response-test
  (testing "substitutes :name: correctly"
    (is (.contains
         (:body (emoji-response {:body "This is on :fire:"}))
         "/images/emoji/fire.png")))
  (testing "with :wild option substitutes multiple names correctly"
    (is (re-find
         #"/images/emoji/on.png.*/images/emoji/fire.png"
         (:body (emoji-response {:body "This is on fire"} :wild true)))))
  (testing "with :replace-fn option substitutes correctly"
    (is (re-find
         #"FIRE"
         (:body (emoji-response {:body "This is on :fire:"}
                                :replace-fn #(.toUpperCase %))))))
  (testing "with :local-dir option substitutes :name: correctly"
    (is (.contains
         (:body (emoji-response {:body "This is on :fire:"}
                                :images-dir "resources/public/images/emoji"))
         "/images/emoji/fire.png"))))

(deftest wrap-emoji-test
  (testing "substitutes :name: correctly"
    (is (.contains
         (:body ((wrap-emoji identity) {:body "This is on :fire:"}))
         "/images/emoji/fire.png"))))