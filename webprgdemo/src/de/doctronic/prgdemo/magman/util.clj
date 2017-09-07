(ns de.doctronic.prgdemo.magman.util
  "Technical utilities used in many places."
  (:require
   [clojure.string :as str]
   [ring.util.codec :as ring-codec]))



(defn str->int
  "Converts `s` to an integer, returns an int or nil if parsing
  fails."
  [s]
  (try (Integer/parseInt s)
       (catch NumberFormatException ex
         nil)))


(defn encode-url
  "Properly encodes URL parameters in `params-map` and concats them to
  `path`."
  [path params-map]
  (str path "?" (ring-codec/form-encode params-map)))


(defn to-view
  [view-id view-params]
  (encode-url (str "/views/" view-id) view-params))
