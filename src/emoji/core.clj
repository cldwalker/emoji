(ns emoji.core
  (:require [clojure.java.shell :as sh]
            [me.raynes.fs :as fs]
            [clojure.string :as string]))

(def images-dir "resources/public/images/emoji")

(defn emoji-names
  "List of emoji file basenames"
  ([] (emoji-names images-dir))
  ([dir] (fs/list-dir dir)))

(defn copy-images
  "Copy gemoji's images to a local directory for app use.
 Defaults to images-dir if no directory specified."
  ([] (copy-images images-dir))
  ([dir]
     (println "Cloning github/gemoji...")
     (sh/sh "git" "clone" "git://github.com/github/gemoji.git")
     (println "Copying images...")
     (fs/copy-dir "gemoji/images/emoji" dir)
     (sh/sh "rm" "-rf" (str dir "/unicode"))
     (sh/sh "rm" "-rf" "gemoji")))

(defn- image-tag [name]
  (format "<img height='20' src='%s' style='vertical-align:middle' width='20' />"
          (str "/images/emoji/" name)))

(defn- replace-emoji-string [replace-fn name]
  (if (some #{(str name ".png")} (emoji-names))
    (replace-fn (str name ".png"))
    name))

(defn emoji-response
  "Modifies a ring response's body by substituting emoji names with images.
Can be used from Ring middlewares or Pedestal interceptors. By default substitution
only happens if an emoji name is wrapped in colons e.g. :name:.

Options:

* :wild        When set to true, substitution happens on any word, no
               colon-delimitation required. Default is false.
* :replace-fn  Custom fn to replace an emoji match, given emoji file basename.
               Defaults to generating a 20x20 image tag."
  [{body :body :as response} & args]
  (let [options (apply array-map args)
        replace-emoji (partial replace-emoji-string
                               (or (:replace-fn options) image-tag))]
    (assoc response
      :body
      (if (:wild options)
        (string/replace body #"\S+" replace-emoji)
        (string/replace body #":(\S+):"
                        #(replace-emoji (second %1)))))))

(defn wrap-emoji
  "Ring middleware to substitute emoji names with images. Takes same options as emoji-response."
  [handler & opts]
  (fn [request]
    (apply emoji-response (handler request) opts)))

(defn -main [& args]
  (apply copy-images args))