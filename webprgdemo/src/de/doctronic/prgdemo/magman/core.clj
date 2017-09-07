(ns de.doctronic.prgdemo.magman.core
  "Component system definition."
  (:require
   [com.stuartsierra.component :as c]
   [de.doctronic.prgdemo.magman.components.server :as server]
   [de.doctronic.prgdemo.magman.components.app :as app]))


(defn new-system
  [config]
  (println "CONFIG" (:server config))
  (c/system-map
    :app
    (c/using
     (app/new-app (:app config))
     [])

    :server
    (c/using
     (server/new-server (:server config))
     [:app])))
