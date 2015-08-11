(ns component-demo.server
  "Web-Server."
  (:require [com.stuartsierra.component :as c]
            [org.httpkit.server :as httpkit]))

(defrecord Server [app port server]
  c/Lifecycle

  (start [component]
    (if server
      component
      (do (println ";; [Server] Starting HttpKit on port" port)
          (->> (httpkit/run-server (:handler app) {:port port})
               (assoc component :server)))))

  (stop [component]
    (if-not server
      component
      (do (println ";; [Server] Stopping HttpKit")
          (server :timeout 10)
          (assoc component :server nil)))))


(defn new-server
  [port]
  (map->Server {:port port}))
