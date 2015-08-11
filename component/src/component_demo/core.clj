(ns component-demo.core
  (:require [com.stuartsierra.component :as c]
            [component-demo.server :as server]
            [component-demo.db :as db]
            [component-demo.migration :as migration]
            [component-demo.app :as app]
            [component-demo.file-logger :as fl])
  (:import [clojure.lang ExceptionInfo])
  (:gen-class))

(defn new-system
  [opts]
  (c/system-map

    :server
    (c/using (server/new-server (:port opts))
             [:app])

    :app
    (c/using (app/new-app)
             [:migration :pool :logger])

    :migration
    (c/using (migration/new-migration)
             [:pool])


    :pool
    (c/using (db/new-connection-pool (:db-uri opts)
                                     (:db-username opts)
                                     (:db-password opts))
             [:database])


    :database
    (c/using (db/new-database)
             [])

    :logger
    (c/using (fl/new-logger (:logfile opts))
             [])
    ,,,))


(defn -main
  [& args]
  (try
    (let [opts {:port 8080
                :db-uri "jdbc:h2:tcp://localhost/~/test;DB_CLOSE_ON_EXIT=FALSE"
                :db-username "sa"
                :db-password ""
                :logfile "/tmp/app.log"}

          sys (c/start (new-system opts))]

      (.addShutdownHook (Runtime/getRuntime)
                        (Thread. (fn []
                                   (println "\nReceived term signal, shutting down...")
                                   (c/stop sys)))))

    (catch ExceptionInfo ex
      (let [sys      (-> ex .getData :system)
            err-comp (-> ex .getData :component)]
        (-> ex .getCause .printStackTrace)
        (println "Error in component" err-comp "\nTerminating application")
        (c/stop sys)
        (System/exit 1)))))
