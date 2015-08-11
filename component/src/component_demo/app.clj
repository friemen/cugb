(ns component-demo.app
  "Web-App."
  (:require [com.stuartsierra.component :as c]
            [component-demo.app.students :as students]
            [component-demo.logger :as logger]
            [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]
            [bidi.ring :as bidi]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper

(defn text-response [status body]
  {:status status
   :headers {"Content-Type" "text/plain"}
   :body (pr-str body)})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Middleware

(defn wrap-db-transaction [pool]
  (fn [next-handler]
    (fn [req]
      (jdbc/with-db-transaction [c pool]
        (next-handler (assoc req :db-conn c))))))



(defn wrap-request-logging [logger]
  (fn [next-handler]
    (fn [req]
      (logger/log logger (pr-str req))
      (let [response (next-handler req)]
        (logger/log logger (pr-str response))
        response))))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Handler

(defn students
  [{:keys [db-conn]}]
  (->> (students/find-all db-conn)
       (text-response 200)))


(defn student
  [{:keys [db-conn params]}]
  (->> (students/find-by-id db-conn (:id params))
       (text-response 200)))


(defn not-found
  [req]
  (->> "not found"
       (text-response 404)))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Routes

(def app-routes
  ["/" [["students"
         [[""
           students]

          [["/" :id]
           student]]]

        [true
         not-found]]])


(defn new-handler
  [pool logger]
  (-> (bidi/make-handler app-routes)
      ((wrap-db-transaction pool))
      ((wrap-request-logging logger))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Record

(defrecord App [handler pool logger]
  c/Lifecycle

  (start [component]
    (if handler
      component
      (do (println ";; [App] Starting")
          (->> (new-handler pool logger)
               (assoc component :handler)))))

  (stop [component]
    (println ";; [App] Stopping")
    (assoc component :handler nil)))


(defn new-app
  []
  (map->App {}))





(comment

  (jdbc/with-db-transaction [c (:pool user/system)]
    (apply jdbc/insert! c :students
           [{:name "John" :age 42}
            {:name "Jane" :age 99}]))

  ,,,)
