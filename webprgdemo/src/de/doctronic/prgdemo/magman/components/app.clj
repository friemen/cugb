(ns de.doctronic.prgdemo.magman.components.app
  "Implementation of app component."
  (:require [de.doctronic.prgdemo.magman.handler :as handler]))


(defn new-app
  [config]
  {:handler (handler/new-handler config)})
