(ns user
  (:require [clojure.tools.namespace.repl :as tools]
            [com.stuartsierra.component :as c]
            [component-demo.core :as core]))


(def options
  {:port 1337
   :db-uri "jdbc:h2:tcp://localhost/~/test;DB_CLOSE_ON_EXIT=FALSE"
   :db-username "sa"
   :db-password ""
   :logfile "/tmp/app.log"})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dev System
(defonce system
  nil)

(defn system-init! []
  (alter-var-root #'system
                  (constantly (core/new-system options))))

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
