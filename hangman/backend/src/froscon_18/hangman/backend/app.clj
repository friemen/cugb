(ns froscon-18.hangman.backend.app
  "Ring handler."
  (:require
   [compojure.core :as cp]

   [ring.middleware.params :as ring-mw-params]
   [ring.middleware.transit :as mw-transit]

   [froscon-18.hangman.backend.app.api :as api]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Routes

(def ^:private routes
  (cp/routes

    (cp/GET "/hello" []
      "Hello World")

    (cp/context "/api" []
      (-> api/routes
          (mw-transit/wrap-transit)))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API


(def handler
  (-> routes
      (ring-mw-params/wrap-params)))
