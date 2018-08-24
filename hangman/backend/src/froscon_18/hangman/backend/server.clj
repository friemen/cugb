(ns froscon-18.hangman.backend.server
  "Server"
  (:require
   [org.httpkit.server :as httpkit]
   [froscon-18.hangman.backend.app :as app]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private: State

(defonce ^:private !server
  (atom nil))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn start!
  "Start server."
  [opts handler]
  (cond-> !server
    (not @!server)
    (reset! (httpkit/run-server handler opts))))

(defn stop!
  "Stop server."
  []
  (cond-> !server
    @!server
    (reset! (@!server :timeout 10))))
