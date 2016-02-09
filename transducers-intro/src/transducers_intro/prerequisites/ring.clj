(ns transducers-intro.prerequisites.ring
  (:require
   [clojure.test :refer [deftest is]]
   [cheshire.core :as json]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Handler: Req -> Resp

(defn handler
  [{{value :omg} :body}]
  {:status 200
   :body value})


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Middleware: Handler -> Handler

(defn wrap-parse-body
  [f]
  (fn [req]
    (f (update req :body json/parse-string true))))

(defn wrap-authorize
  [f]
  (fn [req]
    (if (= "secret" (get-in req [:headers "Authorize"]))
      (f req) {:status 403 :body "Not authorized"})))

(def middleware
  (comp wrap-parse-body
        wrap-authorize))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(def handler*
  (middleware handler))

(deftest test-ring

  (is (= {:status 403
          :body "Not authorized"}

         (handler* {:headers {"Authorize" "..."}
                    :body "{\"omg\":\"ohai\"}"})))

  (is (= {:status 200
          :body "ohai"}

         (handler* {:headers {"Authorize" "secret"}
                    :body "{\"omg\":\"ohai\"}"})))
  ,,,)
