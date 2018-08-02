(ns froscon-18.hangman.backend.main
  (:require [com.stuartsierra.component :as c]
            [froscon-18.hangman.backend.core :as core]
            [froscon-18.hangman.backend.components.server :as server])
  (:import [clojure.lang ExceptionInfo])
  (:gen-class))



(defn- shutdown-hook
  [sys]
  (Thread. (fn []
             (println "\nReceived term signal, shutting down...")
             (c/stop sys)
             (println "Application stopped"))))

(defn -main
  [& args]
  (try
    (let [system
          (-> (core/new-system {})
              (assoc :server (c/using
                              (server/new {:port 1337})
                              [:app]))
              (c/start))]

      (-> (Runtime/getRuntime)
          (.addShutdownHook (shutdown-hook system)))
      (println "Application started successfully"))

    (catch ExceptionInfo ex
      (let [{:keys [system component]}
            (.getData ex)]

        (-> ex
            .getCause
            .printStackTrace)
        (println "Error in component" component "\nTerminating application")
        (c/stop system)
        (System/exit 1)))))
