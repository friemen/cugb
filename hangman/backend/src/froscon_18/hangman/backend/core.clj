(ns froscon-18.hangman.backend.core
  "System definition"
  (:require
   [com.stuartsierra.component :as c]
   [froscon-18.hangman.backend.components.app :as app]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; System

(defn new-system
  [config]
  (c/system-map
    :app
    (c/using
     (app/new {})
     [])))
