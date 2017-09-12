(ns de.doctronic.prgdemo.magman.handler
  "Web handlers, routes and middleware."
  (:require
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [taoensso.timbre :as log]
   [compojure.route :as route]
   [compojure.core :as cp :refer [GET POST]]
   [ring.util.response :as response]
   [ring.middleware
    [defaults :refer [site-defaults wrap-defaults]]]
   [de.doctronic.prgdemo.magman.util :as u]
   [de.doctronic.prgdemo.magman.actions :as actions]
   [de.doctronic.prgdemo.magman.views :as views]

   ;; make sure to list all views namespaces
   [de.doctronic.prgdemo.magman.views.index]
   [de.doctronic.prgdemo.magman.views.issue-details]
   ,,,))



(defn- meta-inf
  "Returns plain text response containing META-INF/MANIFEST.MF"
  [_]
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    (-> "META-INF/MANIFEST.MF"
                (io/resource)
                (slurp))})


(defn- handle-action!
  "Processes the action and sends a redirect response."
  [context action-params]
  (log/debug "Execute action" action-params)
  (let [{:keys [view-id view-params]}
        (actions/process! context action-params)]
    (log/debug "Redirecting to" view-id "with params" view-params)
    (response/redirect (u/to-view view-id view-params)
                       303)))


(defn- app-routes
  [config]
  (cp/routes
   (GET "/meta" []
        meta-inf)

   (GET "/views/:view-id" request
        (views/render {:config config}
                      (:params request)))

   (POST "/actions/:action-id" request
         (handle-action! {:config config}
                         (:params request)))

   (GET "/" []
        (response/redirect "/views/index" 303))

   (route/resources "/")
   (route/not-found {:status  404
                     :headers {"Content-Type" "text/html"}
                     :body    "Not found"})))


;; Public API


(defn new-handler
  [config]
  (-> (app-routes config)
      (wrap-defaults (-> site-defaults
                         (assoc-in [:security :anti-forgery] false)))))
