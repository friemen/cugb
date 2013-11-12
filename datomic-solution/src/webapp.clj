(ns webapp
  (:require [org.httpkit.server :as httpkit]
            [compojure.handler :as handler]
            [ring.util.response :refer [redirect response]]
            [hiccup.page :refer [html5]]
            [compojure.core :refer [defroutes GET POST]]
            [datomic.api :refer [db q] :as d]))

;; Datomic related

(def messages-schema1
  [{:db/ident :message/text
    ;;code should use code for datum definition. use #db/id[:db.part/db] in EDN.
    :db/id (d/tempid :db.part/db)
    :db/valueType :db.type/string
    :db/fulltext true
    :db/cardinality :db.cardinality/one
    :db/doc "A message."
    :db.install/_attribute :db.part/db}])

(def db-uri "datomic:free://localhost:4334/webapp")

(def conn (atom nil))

(defn init-db!
  []
  (reset! conn (d/connect db-uri))
  (when (d/create-database db-uri)
    @(d/transact @conn messages-schema1)))

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
