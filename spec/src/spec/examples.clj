(ns spec.examples
  (:require [clojure.spec :as s]
            [clojure.string :as str]
            [clojure.test.check.generators :as gen]
            [clojure.spec.test :as stest]))

;; helpful resources
;; http://clojure.org/guides/spec
;; http://www.lispcast.com/clojure.spec-vs-schema
;; https://www.bevuta.com/en/blog/declaratively-parse-query-params-using-clojure-spec/

;; defining specs
;; global activation / deactivation
;; using specs
;;  - pre- and post conditions
;;  - declaring functions
;;  - generating test data from specs


;; a spec can be
;; - a predicate
;; - a set
;; - another defined spec


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; spec definition
(s/def ::date (partial instance? java.util.Date))

;; applying spec or a spec definition:

(s/conform ::date (java.util.Date.))
;; returns value or :clojure.spec/invalid

(s/valid? ::date (java.util.Date.))
;= true

(s/valid? ::date nil)
;= false

;; a simple set is enough
(s/valid? #{1 2 3} 4)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; let's define a map
(s/def ::sent ::date)
(s/def ::message (s/keys :req [::sent]))

(s/valid? ::message {::sent (java.util.Date.)})
;= true
(s/valid? ::message {:sent (java.util.Date.)})
;= false

;; ok, namespaced keywords seem to be important

;; oh, look! a new reader macro
(def m #:spec.examples {:sent (java.util.Date.)})
(s/valid? ::message m)
;= true



;; s/and and s/or
(s/def ::age (s/and int? #(<= 0 % 130)))
(s/valid? ::age 85)
(s/valid? ::age 130)
(s/valid? ::age -1)


(s/def ::int-or-string (s/or :i int?
                             :s string?))
(s/valid? ::int-or-string 42)
(s/valid? ::int-or-string "0.1")
(s/valid? ::int-or-string 0.1)


;; maybe
(s/def ::maybe-string (s/nilable string?))
(s/valid? ::maybe-string "foo")
(s/valid? ::maybe-string nil)
(s/valid? ::maybe-string 42)


;; there are also specs for ranges
(s/def ::age (s/int-in 0 131))


;; maps
(s/def ::non-empty-string (s/and string? #(-> % str/trim count (> 1))))
(s/def ::firstname ::non-empty-string)
(s/def ::lastname ::non-empty-string)
(s/def ::person (s/keys :req [::firstname ::lastname]))

(def person {::firstname "oha"
             ::lastname  "bar"})
(s/valid? ::person person)
(s/explain ::person person)

;; s/multi-spec

;; s/merge merging map specs (mimics inheritance)

;; s/tuple, s/coll-of, s/map-of


;; s/cat helps with sequences

(s/def ::tag     #{:body :p :h1})
(s/def ::attrs   (s/map-of keyword? string?))
(s/def ::html    (s/cat :tag      ::tag
                        :attrs    (s/? ::attrs)
                        :children (s/* (s/or :i
                                             int?
                                             :s string?
                                             :html ::html))))
(s/valid? ::html [:body {:bar "1"} [:p "bum"]])
;= true

(s/conform ::html [:p "Hello World"])
;= {:tag :p, :children [[:s "Hello World"]]}

;; s/fdef

(defn divisible?
  [n d]
  (-> n (mod d) (zero?)))

(defn fizzbuzz
  [n]
  (str (if (divisible? n 3) "fizz")
       (if (divisible? n 5) "buzz")))


(s/fdef fizzbuzz
        :args (s/cat :n (s/and int? pos?))
        :ret string?)


(clojure.spec.test/instrument `fizzbuzz)

(s/exercise-fn `fizzbuzz)

;; Generators

(last (gen/sample (s/gen ::html) 2))




(comment
  ;; find out what clojure.spec stores

  (->> @@#'s/registry-ref
       (filter #(-> % first namespace (= "spec.examples")))
       (map (fn [[k spec]]
              (cond
                (satisfies? s/Spec spec) (s/describe* spec)
                :else spec))))
,,,)


;; open questions:
;; where to locate spec definitions?
;; how to find out which keys are contained in a spec?
