(ns libdemos.schema-demo
  (:require [clojure.string :as string]
            [schema.core :as s]))


(def Age (s/both s/Int
                 (s/pred #(and (<= 0 %) (<= % 140)) 'reasonable-human-age)))

(def User {:firstname (s/maybe s/Str)
           :lastname (s/both s/Str
                             (s/pred (comp not string/blank?)))
           (s/optional-key :age) s/Int})



(s/defn ^:always-validate new-user :- User
  [firstname :- (s/maybe s/Str)
   lastname :- s/Str]
  {:firstname firstname
   :lastname lastname})


(comment

  ;; returns non-nil result in case of constraint violation
  (s/check Age 42)

  ;; throws Exception
  (s/validate Age -1)

  (new-user nil "R")

,,,)
