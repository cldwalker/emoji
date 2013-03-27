(ns emoji.core
  (:require [clojure.java.shell :as sh]
            [me.raynes.fs :as fs]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(def public-images-dir "images/emoji")
(def resources-images-dir (str "public/" public-images-dir))
(def images-dir (str "resources/" resources-images-dir))

(defn copy-images
  "Copy gemoji's images to a local directory for app use.
 Defaults to images-dir if no directory specified."
  ([] (copy-images images-dir))
  ([dir]
     (println "Cloning github/gemoji...")
     (sh/sh "git" "clone" "git://github.com/github/gemoji.git")
     (println "Copying images...")
     (fs/copy-dir "gemoji/images/emoji" dir)
     ;; copy-dir copies symlinks as files thus removing need for unicode/
     (sh/sh "rm" "-rf" (str dir "/unicode"))
     (sh/sh "rm" "-rf" "gemoji")))

(defn- image-tag [name]
  (format "<img height='20' src='%s' style='vertical-align:middle' width='20' />"
          (str "/" public-images-dir "/" name)))

(defn- replace-emoji-string [replace-fn local-dir name]
  (let [basename (str name ".png")
        valid-replace (if local-dir
                        #(some #{%} (fs/list-dir local-dir))
                        #(io/resource (str resources-images-dir "/" %)))]
    (if (valid-replace basename) (replace-fn basename) name)))

(defn emoji-response
  "Modifies a ring response's body by substituting emoji names with images.
Can be used from Ring middlewares or Pedestal interceptors. By default substitution
only happens if an emoji name is wrapped in colons e.g. :name:.

Options:

* :wild        When set to true, substitution happens on any word, no
               colon-delimitation required. Default is false.
* :replace-fn  Custom fn to replace an emoji match, given emoji file basename.
               Defaults to generating a 20x20 image tag.
* :images-dir  Specifies a local directory for emoji images. When not
               set, images that come with the emoji clojar are used."
  [{body :body :as response} & args]
  (let [options (apply array-map args)
        replace-emoji (partial replace-emoji-string
                               (or (:replace-fn options) image-tag)
                               (:images-dir options))]
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