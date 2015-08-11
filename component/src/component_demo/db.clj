(ns component-demo.db
  "Database access."
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.ddl :as ddl]
            [java-jdbc.sql :as sql]
            [com.stuartsierra.component :as c])
  (:import [org.h2.tools Server]
           [com.zaxxer.hikari HikariDataSource]))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Connection Pool

(defn- make-datasource
  [db-uri username password]
  (doto (HikariDataSource.)
    (.setJdbcUrl db-uri)
    (.setUsername username)
    (.setPassword password)))


(defrecord DatabaseConnectionPool [db-uri username password datasource]
  c/Lifecycle

  (start [component]
    (if datasource
      component
      (do
        (println ";; [DatabaseConnectionPool] Setting up DB connection pool" db-uri)
        (let [datasource (make-datasource db-uri username password)]
          (assoc component :datasource datasource)))))

  (stop [component]
    (if datasource
      (do (println ";; [DatabaseConnectionPool] Releasing DB connection pool")
          (.shutdown datasource)
          (assoc component :datasource nil))
      component)))


(defn new-connection-pool
  [db-uri username password]
  (map->DatabaseConnectionPool {:db-uri db-uri
                                :username username
                                :password password}))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Database

(defrecord Database [tcp-server web-server]
  c/Lifecycle

  (start [component]
    (if tcp-server
      component
      (do (println ";; [Database] Starting H2 Database, web console is http://localhost:8082")
          (let [tcp-server (Server/createTcpServer (into-array String []))
                web-server (Server/createWebServer (into-array String []))]
            (.start tcp-server)
            (.start web-server)
            (assoc component
                   :tcp-server tcp-server
                   :web-server web-server)))))

  (stop [component]
    (if-not tcp-server
      component
      (do (println ";; [Database] Stopping H2 Database")
          (.stop tcp-server)
          (.stop web-server)
          (assoc component
                 :tcp-server nil
                 :web-server nil)))))

(defn new-database
    []
    (map->Database {}))
