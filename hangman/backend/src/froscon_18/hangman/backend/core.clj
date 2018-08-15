(ns froscon-18.hangman.backend.core
  "Main namespace"
  (:require
   [froscon-18.hangman.backend.server :as server]
   [froscon-18.hangman.backend.app :as app]))


(comment

  ;; Start server
  (server/start! {:port 1337} app/handler)

  ;; Stop server
  (server/stop!)

  ,,,)
