(ns webapp
  (:require [org.httpkit.server :as httpkit]
            [compojure.handler :as handler]
            [ring.util.response :refer [redirect response]]
            [hiccup.page :refer [html5]]
            [compojure.core :refer [defroutes GET POST]]
            [datomic.api :refer [db q] :as d]))

;; Datomic related


(defn attr
  ([id type desc]
   (attr id :db.cardinality/one type desc))
  ([id card type desc]
   {:db/ident id
    :db/id (d/tempid :db.part/db) ;;code should use code for datum definition. use #db/id[:db.part/db] in EDN.
    :db/valueType type
    :db/cardinality card
    :db/doc desc
    :db.install/_attribute :db.part/db}))

(def ONE :db.cardinality/one)
(def MANY :db.cardinality/many)
(def STRING :db.type/string)

(def messages-schema1
  [(attr :message/text STRING "A message.")
   (attr :message/author STRING "The author that issued the message.")
   (attr :message/recipients MANY STRING "Names of who will receive the message.")])

(def db-uri "datomic:free://localhost:4334/webapp")

(def conn (atom nil))

(defn init-db!
  []
  (if (d/create-database db-uri)
    (do
      (println "created new db" db-uri)
      (reset! conn (d/connect db-uri))
      (println "creating schema")
      (d/transact @conn messages-schema1))
    (reset! conn (d/connect db-uri)))
  @conn)

(def tempid
  (partial d/tempid :db.part/user))

;; data access

(defn add-message!
  [s]
  (d/transact @conn [[:db/add (tempid) :message/text s]]))

(defn load-messages
  []
  (->> (q '[:find ?e ?text
            :in $
            :where
            [?e :message/text ?text]]
          (db @conn))
       ;; entity IDs are auto-incrementing
       (sort-by first)
       (map second)))

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
  (httpkit/run-server (handler/site #'app) {:port 8080 :join? false})
  (println "Server started on http://localhost:8080"))
