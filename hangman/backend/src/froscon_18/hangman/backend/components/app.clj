(ns froscon-18.hangman.backend.components.app
  "App component that contains the Ring handler"
  (:require
   [com.stuartsierra.component :as c]
   [froscon-18.hangman.backend.components.app.handler :as handler]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Component

(defrecord App [,,,
                handler]

  c/Lifecycle
  (start [component]
    (println ";; [App] Started.")
    (-> component
        (assoc :handler (handler/new))))

  (stop [component]
    (println ";; [App] Stopped.")
    (-> component
        (assoc :handler nil))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Component Constructor

(defn new
  [config]
  (map->App config))
