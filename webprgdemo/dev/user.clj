(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.tools.namespace.repl :as tools]
   [com.stuartsierra.component :as c]
   [de.doctronic.prgdemo.magman.core :as core]
   [clojure.edn :as edn]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dev System

(defn new-dev-system
  []
  (let [config
        (-> "etc/config.edn" (io/resource) (slurp) (edn/read-string))]
    (core/new-system config)))

(defonce system
  nil)

(defn system-init! []
  (alter-var-root #'system
                  (constantly (new-dev-system))))

(defn system-start! []
  (alter-var-root #'system
                  c/start))

(defn system-stop! []
  (alter-var-root #'system
                  #(when % (c/stop %))))

(defn system-go! []
  (system-init!)
  (system-start!))

(defn system-restart! []
  (system-stop!)
  (tools/refresh-all :after 'user/system-go!))
