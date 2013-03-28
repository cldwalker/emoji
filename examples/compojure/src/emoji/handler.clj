(ns emoji.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [emoji.core :refer [wrap-emoji]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defroutes app-routes
  (GET "/" [] "This page is on fire")
  (route/not-found "Not Found"))

(def app
  (handler/site (-> app-routes
                    (wrap-emoji :wild true)
                    (wrap-resource "/public"))))
