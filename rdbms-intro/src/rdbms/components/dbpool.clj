(ns rdbms.components.dbpool
  (:require [com.stuartsierra.component :as c])
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource]))


(defn make-datasource
  [uri username password]
  (doto (ComboPooledDataSource.)
    ;; (.setDriverClass (:classname spec))
    (.setJdbcUrl uri)
    (.setUser username)
    (.setPassword password)

    ;; expire excess connections after 30 minutes of inactivity:
    (.setMaxIdleTimeExcessConnections (* 30 60))
    ;; expire connections after 3 hours of inactivity:
    (.setMaxIdleTime (* 3 60 60)))
  )


(defrecord DatabaseConnectionPool [uri username password datasource]
  c/Lifecycle

  (start [component]
    (if datasource
      component
      (do
        (println ";; [DatabaseConnectionPool] Starting: " uri)
        (let [datasource (make-datasource uri username password)]
          (assoc component :datasource datasource)))))

  (stop [component]
    (if-not datasource
      component
      (do (println ";; [DatabaseConnectionPool] Stopping")
          ;; (-> datasource .shutdown)
          (-> datasource .close)
          (assoc component :datasource nil)))))


(defn new-connection-pool
  [config]
  (-> config
      (select-keys [:uri :username :password])
      map->DatabaseConnectionPool))
