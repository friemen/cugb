(ns typetests
  (:require [clojure.core.typed :as t :refer [ann Any cf check-ns defn> U]]))


(ann add-numbers [Number Number * -> Number])
(defn add-numbers
  [x & ys]
  (apply + (cons x ys)))

(ann add-many-numbers (Fn [Number Number -> Number]
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


(defn> concat-strings
  "Returns the concatenation of two strings"
  :- String
  [x :- String y :- String]
  (str x y))

(concat-strings "foo" "bar")


(defn> inc-or-nil
  :- (t/Option Number)
  [x :- Any]
  (if (number? x) (inc x) nil))


;; ----------------------------------------------------------------------------
;; using an alias

(t/defalias Person (t/HMap :mandatory
                           {:name String
                            :age Number}))

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


(defn> street-length
  :- Number
  [a :- '{:street String}]
  (-> a :street count))

