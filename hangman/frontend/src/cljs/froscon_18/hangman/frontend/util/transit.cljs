(ns froscon-18.hangman.frontend.util.transit
  "Transit remote helpers."
  (:require
   [cognitect.transit :as t]
   [clojure.string :as str]
   [cljs.pprint :as pprint])

  (:import
   [goog.net XhrIo EventType]
   [goog.events]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper

(defn- callback-goog
  [callback-success callback-error request]
  (let [status
        (.getStatus request)

        headers
        (->> (.getAllResponseHeaders request)
             (str/split-lines)
             (map #(str/split % #":"))
             (into {}))

        body
        (->> (.getResponseText request)
             (t/read (t/reader :json)))

        error?
        (or (find body :error)
            (not (.isSuccess request)))

        callback-success-or-error
        (if error?
          callback-error callback-success)]

    (callback-success-or-error
     {:status status
      :headers headers
      :body body})))


(defn- callback-error-default
  [response]
  (let [error
        (with-out-str (pprint/pprint response))]

    (js/console.error error)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transit

(defn post
  "Http post via transit"
  ([url payload callback-success]
   (post url payload callback-success callback-error-default))

  ([url payload callback-success callback-error]
   (let [request (XhrIo.)]
     (doto request
       (goog.events.listen
        (.-SUCCESS EventType)
        #(callback-goog callback-success callback-error request))

       (goog.events.listen
        (.-ERROR EventType)
        #(callback-error {:body {:error "Server nicht verfügbar."}}))

       (.send url
              "POST"
              (t/write (t/writer :json) payload)
              #js {"Content-Type" "application/transit+json"})))))
