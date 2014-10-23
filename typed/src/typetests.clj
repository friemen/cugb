(ns typetests
  (:refer-clojure :exclude [defn])
  (:require [clojure.core.typed :as t :refer [ann Any cf check-ns defn U IFn]]))


(ann add-numbers [Number Number * -> Number])
(defn add-numbers
  [x & ys]
  (apply + (cons x ys)))

(ann add-many-numbers (IFn [Number Number -> Number]
                           [Number Number Number Number * -> Number]))
(defn add-many-numbers
  ([x y]
     (add-many-numbers x y 0))
  ([x y z & rest]
     (apply + (concat [x y z] rest))))

(ann make-nonsense [ -> Number])
(defn make-nonsense
  []
  (add-numbers 1 2))


;; ----------------------------------------------------------------------------
;; alternative way: use defn>


(defn concat-strings
  "Returns the concatenation of two strings"
  [x :- String y :- String] :- String
  (str x y))

(concat-strings "foo" "bar")


(defn inc-or-nil
  [x :- Any] :- (t/Option Number)
  (if (number? x) (inc x) nil))


;; ----------------------------------------------------------------------------
;; using an alias

(t/defalias Person (t/HMap :mandatory
                           {:name String
                            :age Number}))

(t/defalias Address2 (t/HMap :mandatory {:street String}))

(t/defalias Baz '{:a Number})

(t/defalias Address3 '{:street String :baz Baz})

(ann print-person [Person -> nil])
(defn print-person
  [p]
  (t/print-env "Hello!")
  (println (:name p) (:age p)))


(print-person {:name "Foo" :age 42})


(defmacro defrecord>
  [sym fields-and-types]
  (let [fields (->> fields-and-types (partition 3) (map first))]
    `(do (clojure.core.typed/ann-record ~sym ~fields-and-types)
         (defrecord ~sym ~(vec fields)))))

(defrecord> Address [street :- String zipcode :- Integer])


(ann street-length ['{:street String} -> Number])
;; cannot use inline type declaration because of
;; http://dev.clojure.org/jira/browse/CTYP-169
(defn street-length
  [a]
  (-> a :street count))


; (ann takes-a-function [(Fn [-> Number]) -> (t/Seq Number)])

; (ann may-take-nil [(t/Option Number) -> Number])

(t/defalias Labeled '{:label String})
(t/defalias Location '{:x Number :y Number})

(t/defalias Poi (t/I Labeled Location))
