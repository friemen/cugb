(ns foo
  (:require [bar :refer [barfn]]))

(defn foofn [name] (str "Hello " (barfn name)))

