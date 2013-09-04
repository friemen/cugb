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

(defn form
  []
  [:form {:method "POST"
          :action "/new-message"
          :class "form-inline"}
   "Message:" [:input {:name "message"}]
   [:p]])


(def msgs (atom ["Foo" "Bar"]))

(defn messages
  []
  [:ul (map #(vector :li %) @msgs)])


(defn add-message
  [msg]
  (swap! msgs #(conj % msg)))


(defroutes app
  (GET "/" [] (layout (form) (messages)))
  (POST "/new-message" [message] (do (add-message message) (redirect "/"))))


(defn -main []
  (httpkit/run-server (handler/site #'app) {:port 8080 :join? false}))
