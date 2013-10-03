(ns webapp
  (:require [org.httpkit.server :as httpkit]
            [compojure.handler :as handler]
            [ring.util.response :refer [redirect response]]
            [hiccup.page :refer [html5]]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.jdbc.sql :as sql]
            [clojure.java.jdbc.ddl :as ddl]
            [korma.core :as k]
            [korma.db :refer [defdb]]
            [korma.config :refer [set-delimiters]]))


;; relational data access

(def db-file "~/webapp.db")
(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname db-file
              :user "sa"
              :password ""
              :naming {:keys clojure.string/lower-case}})

(defn missing?
  [table column name]
  (empty? (jdbc/query db-spec (sql/select * table (sql/where {column name})))))

(defn create-schema!
  []
  (when (missing? :information_schema.sequences :sequence_name "PKSEQ")
    (jdbc/execute! db-spec ["create sequence pkseq"]))
  
;; TODO add a ddl/create-table expression to the empty vector 
  (when (missing? :information_schema.tables :table_name "MESSAGES")
    (jdbc/execute! db-spec [ ])))


;; You can work with or without Korma.

;; Korma hint: use the expression (set-delimiters "") after (defdb ... ) to make Korma work with H2


;; TODO add insert and select expressions to the following functions

(defn add-message!
  [s]
  nil)

(defn load-messages
  []
  nil)


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
   [:ul (map #(vector :li (:text %)) (load-messages))]])


;; routing + startup

(defroutes app
  (GET "/" [] (layout (form) (messages)))
  (POST "/new-message" [text] (do (add-message! text) (redirect "/"))))

(defn -main
  []
  (create-schema!)
  (httpkit/run-server (handler/site #'app) {:port 8080 :join? false}))

