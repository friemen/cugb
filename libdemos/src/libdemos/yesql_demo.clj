(ns libdemos.yesql-demo
  (:require [clojure.java.jdbc :as jdbc]
            [yesql.core :refer [defqueries]])
  (:import [org.h2.tools Server]
           [com.zaxxer.hikari HikariDataSource]))

(defqueries "queries.sql")


(comment

  (-> *ns* ns-publics keys >pprint)

,,,)


(comment
  (start-db!)
  (def pool (make-datasource "jdbc:h2:tcp://localhost/~/test"
                             "sa" ""))

  (jdbc/with-db-connection [c pool]
    (manufacturer-by-id c 1))

  (stop-db!)
,,,)



;; Database infrastructure

(def db! (atom {}))


(defn start-db!
  []
  (swap! db! (fn [db]
               (let [{:keys [tcp-server web-server]} db
                     tcp-server (or tcp-server
                                    (doto (Server/createTcpServer (into-array String []))
                                      (.start)))
                     web-server (or web-server
                                    (doto (Server/createWebServer (into-array String []))
                                      (.start)))]
                 (assoc db
                        :tcp-server tcp-server
                        :web-server web-server)))))


(defn stop-db!
  []
  (swap! db! (fn [db]
               (let [{:keys [tcp-server web-server]} db]
                 (and tcp-server (.stop tcp-server))
                 (and web-server (.stop web-server))
                 (dissoc db :tcp-server :web-server)))))


(defn make-datasource
  [db-uri username password]
  {:datasource (doto (HikariDataSource.)
                 (.setJdbcUrl db-uri)
                 (.setUsername username)
                 (.setPassword password))})
