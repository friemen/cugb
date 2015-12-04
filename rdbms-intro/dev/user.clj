(ns user
  (:require [clojure.tools.namespace.repl :as tools]
            [com.stuartsierra.component :as c]
            [rdbms.system :as system]))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dev System

(defonce system nil)

(defn system-init! []
  (alter-var-root #'system
                  (constantly (system/new-system system/default-config))))

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
