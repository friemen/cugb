(ns de.doctronic.prgdemo.magman.components.server
  (:require [com.stuartsierra.component :as c]
            [org.httpkit.server :as httpkit]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper

(defn- run-server!
  [opts-httpkit handler]
  (httpkit/run-server handler opts-httpkit))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Record

(defrecord Server [handler opts-httpkit
                   server
                   app]

  c/Lifecycle

  (start [component]
    (if server
      component
      (do (println ";; [Server] Starting HTTP Kit on port" (:port opts-httpkit))
          (->> (or handler (:handler app))
               (run-server! opts-httpkit)
               (assoc component :server)))))

  (stop [component]
    (if-not server
      component
      (do (println ";; [Server] Stopping HTTP Kit")
          (server :timeout 10)
          (assoc component :server nil)))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn new-server
  [config]
  (map->Server {:handler (:handler config)
                :opts-httpkit (select-keys config [:port :max-body])}))
