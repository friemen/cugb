(ns froscon-18.hangman.backend.components.server
  "Server component"
  (:require
   [com.stuartsierra.component :as c]
   [org.httpkit.server :as httpkit]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper

(defn- run-server!
  [opts handler]
  (httpkit/run-server handler opts))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Record

(defrecord Server [port
                   server
                   app]

  c/Lifecycle

  (start [component]
    (if server
      component
      (do (println ";; [Server] Starting HTTP Kit on port" port)
          (->> (:handler app)
               (run-server! {:port port})
               (assoc component :server)))))

  (stop [component]
    (if-not server
      component
      (do (println ";; [Server] Stopping HTTP Kit")
          (server :timeout 10)
          (assoc component :server nil)))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn new
  [config]
  (map->Server config))
