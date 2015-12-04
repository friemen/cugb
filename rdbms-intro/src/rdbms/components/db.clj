(ns rdbms.components.db
  (:require [com.stuartsierra.component :as c])
  (:import [org.h2.tools Server]))



(defrecord Database [tcp-server web-server]
  c/Lifecycle

  (start [component]
    (if tcp-server
      component
      (do (println ";; [Database] Starting, web console is http://localhost:8082")
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
      (do (println ";; [Database] Stopping")
          (.stop tcp-server)
          (.stop web-server)
          (assoc component
                 :tcp-server nil
                 :web-server nil)))))


(defn new-database
  []
  (map->Database {}))
