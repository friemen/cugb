(ns user
  (:require
   [clojure.tools.namespace.repl :as tools]
   [com.stuartsierra.component :as c]

   [froscon-18.hangman.backend.components.server :as server]
   [froscon-18.hangman.backend.core :as core]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Development System

(defn- new-dev-system
  [config]
  {:server
   (c/using
    (server/new (:server config))
    [:app])})

(defn new-system
  [config]
  (merge (core/new-system config)
         (new-dev-system config)))

(defonce system
  nil)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CLJ Helper

(defn system-init!
  []
  (let [config
        {:server
         {:port 1337}}]

    (alter-var-root #'system
                    (constantly (new-system config)))))

(defn system-start!
  []
  (alter-var-root #'system
                  c/start))

(defn system-stop!
  []
  (alter-var-root #'system
                  #(when % (c/stop %))))

(defn system-go!
  []
  (system-init!)
  (system-start!))

(defn system-restart!
  []
  (system-stop!)
  (tools/refresh-all :after 'user/system-go!))
