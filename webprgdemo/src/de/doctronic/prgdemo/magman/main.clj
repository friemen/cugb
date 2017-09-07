(ns de.doctronic.prgdemo.magman.main
  "Main entry point to standalone execution."
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as c]
            [de.doctronic.prgdemo.magman.core :as core]
            [taoensso.timbre :as log]
            [clojure.edn :as edn])
  (:gen-class))



(defn- shutdown-hook
  [sys]
  (Thread. (fn []
             (println "\nReceived term signal, shutting down...")
             (c/stop sys)
             (log/info "Application stopped"))))



(defn -main
  []
  (try
    (let [config
          (-> "etc/config.edn" (io/resource) (slurp) (edn/read-string))
          system
          (-> config
              (core/new-system)
              (c/start))]

      (-> (Runtime/getRuntime)
          (.addShutdownHook (shutdown-hook system)))
      (log/info "Application started")
      system)

    (catch clojure.lang.ExceptionInfo ex
      (let [system
            (-> ex .getData :system)

            err-comp
            (-> ex .getData :component)]

        (-> ex .getCause .printStackTrace)
        (println "Error in component" err-comp "\nTerminating application")
        (c/stop system)
        (log/error ex "Application failed to start")
        (System/exit 1)))))
