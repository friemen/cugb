(ns froscon-18.hangman.backend.components.app.handler
  "Ring handler."
  (:require
   [compojure.core :as cp]

   [ring.middleware.params :as ring-mw-params]
   [ring.middleware.transit :as mw-transit]

   [froscon-18.hangman.backend.components.app.handler.api :as api]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Routes

(defn- new-routes
  []
  (cp/routes
    (cp/context "/api" []
      (-> (api/new-routes)
          (mw-transit/wrap-transit)))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API


(defn new
  []
  (-> (new-routes)
      (ring-mw-params/wrap-params)))
