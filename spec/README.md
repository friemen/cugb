# clojure.spec

## Useful resources

* [Official Guide](http://clojure.org/guides/spec)
* [API](http://clojure.github.io/clojure/branch-master/clojure.spec-api.html)
* [Comparision with schema](http://www.lispcast.com/clojure.spec-vs-schema)
* [An application for REST endpoints](https://www.bevuta.com/en/blog/declaratively-parse-query-params-using-clojure-spec/)


## At a glance

* Available with Clojure 1.9
* The Cognitect answer to schematizing data
* No type system!
* Tight integration with [test.check](https://github.com/clojure/test.check)
* More or less enforces use of global, namespaced keywords


## First steps for defining specs

A new ns dependency:

```clojure
(ns spec.examples
  (:require [clojure.spec :as s]))
```

A *spec* can be:
* some expression defined with `s/def`
* a predicate
* a set
* another defined spec


A first example:

```clojure
;; spec definition
(s/def ::date (partial instance? java.util.Date))

;; applying spec or a spec definition:

(s/conform ::date (java.util.Date.))
;; returns value or :clojure.spec/invalid

(s/valid? ::date nil)
;; returns true or false

;; a simple set is enough
(s/valid? #{1 2 3} 4)

```

Prime-time for namespaced keywords in maps:

```clojure
;; let's define a map using s/keys
(s/def ::sent ::date)
(s/def ::message (s/keys :req [::sent]))

(s/valid? ::message {::sent (java.util.Date.)})
;= true
(s/valid? ::message {:sent (java.util.Date.)})
;= false

;; got it, namespaced keywords seem to be important

;; oh, look! a new reader macro
(def m #:spec.examples {:sent (java.util.Date.)})
(s/valid? ::message m)
;= true
```

Another map spec example

```clojure
(s/def ::non-empty-string (s/and string? #(-> % str/trim count (> 1))))
(s/def ::firstname ::non-empty-string)
(s/def ::lastname ::non-empty-string)
(s/def ::person (s/keys :req [::firstname ::lastname]))

(s/describe ::person)
;= (keys :req [:spec.examples/firstname :spec.examples/lastname])


;; now some data
(def person {::firstname "oha"
             ::lastname  "bar"})

;; and use the spec
(s/valid? ::person person)
```

But interchangeability of records and maps suffers, and
[not everybody likes this](http://dev.clojure.org/jira/browse/CLJ-1938)

```clojure
(defrecord Person [firstname lastname])

(s/valid? ::person (Person. "foo" "bar"))
;= false

(s/def ::unqualified-person (s/keys :req-un [::firstname ::lastname]))
(s/valid? ::unqualified-person (Person. "foo" "bar")) ;= true
```

`coll-of` and `map-of` seem very useful for defining homogeneous
collections:

```clojure
(s/valid? (s/coll-of int? :count 3) [1 2 3])
;= true

(s/valid? (s/map-of keyword? number?) {:foo 3.141
                                       :bar 2.7})
;= true
```

`s/cat` is king for checking heterogeneous (possibly recursive)
sequences:

```clojure
(s/def ::tag   #{:body :p :h1})
(s/def ::attrs (s/map-of keyword? string?))
(s/def ::html  (s/cat :tag      ::tag
                      :attrs    (s/? ::attrs)
                      :children (s/* (s/or :i
                                           int?
                                           :s string?
                                           :html ::html))))

(s/valid? ::html [:body {:bar "1"} [:p "bum"]])
;= true

(s/conform ::html [:p "Hello World"])
;= {:tag :p, :children [[:s "Hello World"]]}
```



Spec'ing functions

```clojure
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
```

An invocation of `fizzbuzz` is checked only when the function has been instrumented:

```clojure
(clojure.spec.test/instrument `fizzbuzz)
```


## Ways to use specs

* `s/valid?` - Predicate, useful in :pre and :post conditions
* `s/conform` - Returns a parsed representation of input data or `:clojure.spec/invalid`
* `s/explain` - Prints help on spec violations of data
* `s/describe` - Inspect an existing spec
* `s/assert` - Return input or throw an exception
* `clojure.spec.test/instrument` - Enables auto-checking with fdef spec on function invocation
* `s/gen` - Create a test data generator to be used with test.check


## Test data generators

Needs an additional library as dev-dependency
Add in your project.clj

```clojure
:profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
```

Try it:

```clojure
(require '[clojure.test.check.generators :as gen])

(gen/sample (s/gen #{1 2 3}))

(gen/sample (s/gen ::age) 50)
```

Using -- under the hood -- test data generators guided by specs, we
can excercise a spec'd function with random test data to see inputs and outputs:

```clojure
(s/exercise-fn `fizzbuzz)
```

To thoroughly check one function use

```clojure
(clojure.spec.test/check `fizzbuzz)
```

Or do this for a complete namespace

```clojure
(-> 'spec.examples
    clojure.spec.test/enumerate-namespace
    clojure.spec.test/check)
```
