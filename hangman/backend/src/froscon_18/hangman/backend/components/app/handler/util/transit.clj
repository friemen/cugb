(ns froscon-18.hangman.backend.components.app.handler.util.transit
  (:require
   [cognitect.transit :as t])

  (:import
   [java.io ByteArrayOutputStream]))


(defn encode
  [value]
  (let [out
        (ByteArrayOutputStream.)]


    (-> out
        (t/writer :json)
        (t/write value))

    (.toString out)))


(defn response
  ([body]
   (response body {:encode? false}))

  ([body {:keys [encode?]}]
   {:status 200
    :headers {"Content-Type"
              "application/transit+json; charset=utf-8"

              "Access-Control-Allow-Origin"
              "*"

              "Access-Control-Allow-Headers"
              "Content-Type"
              }
    :body (cond-> body encode? encode)}))
