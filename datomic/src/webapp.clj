(ns webapp
  (:require [org.httpkit.server :as httpkit]
            [compojure.handler :as handler]
            [ring.util.response :refer [redirect response]]
            [hiccup.page :refer [html5]]
            [compojure.core :refer [defroutes GET POST]]
            [datomic.api :refer [db q] :as d]))

(def messages-schema1
  ;; TODO add a very basic schema to allow storing messages
  [])

(def db-uri "datomic:free://localhost:4334/webapp")

(def conn (atom nil))

(defn init-db!
  []
  ;; TODO connect to the database
  ;; TODO create the database and transact the schema if the database is new
  )

(def tempid
  (partial d/tempid :db.part/user))

;; data access
;; TODO add insert and query expressions to the following functions
(defn add-message!
  [s]
  )

(defn load-messages
  []
  )

;; rendering

(defn layout [ & body]
  (html5 [:head [:title "Message DB"]]
         [:body body]))

(defn form
  []
  [:form {:method "POST"
          :action "/new-message"
          :class "form-inline"}
   "Enter Message: " [:input {:name "text"}] [:br]
   [:input {:type "submit" :value "Submit Message"}]
   [:p]])

(defn messages
  []
  [:p "Messages sent"
   [:ul (map #(vector :li %) (load-messages))]])

;; routing + startup

(defroutes app
  (GET "/" [] (layout (form) (messages)))
  (POST "/new-message" [text] (do (add-message! text) (redirect "/"))))

(defn -main
  []
  (init-db!)
  (httpkit/run-server (handler/site #'app) {:port 8080 :join? false}))

