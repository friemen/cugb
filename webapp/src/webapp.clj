(ns webapp
  (:require [org.httpkit.server :as httpkit]
            [compojure.handler :as handler]
            [ring.util.response :refer [redirect response]]
            [hiccup.page :refer [html5]]
            [compojure.core :refer [defroutes GET POST]]))


(defn layout [ & body]
  (html5 [:head
               [:title "First webapp"]
               [:link {:rel "stylesheet"
                       :href "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css"}]]
         [:body
          body]))


(defn app
  [request]
  {:code 200 :body "Hello World"})


(defn -main []
  (httpkit/run-server (handler/site #'app) {:port 8080 :join? false}))

(-main)
