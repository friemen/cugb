# clojure.spec

## Useful resources

* [Official Guide](http://clojure.org/guides/spec)
* [Comparision with schema](http://www.lispcast.com/clojure.spec-vs-schema)
* [An application for REST endpoints](https://www.bevuta.com/en/blog/declaratively-parse-query-params-using-clojure-spec/)


## At a glance

* Available with Clojure 1.9
* The Cognitect answer to schematizing data
* No type system!
* Tight integration with test.check
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

Prime-time for namespaced keywords:

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


Checking (recursive) sequences

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


## Ways to use specs

* `s/valid?` - Predicate, useful in :pre and :post conditions
* `s/conform` - Returns a parsed representation of input data or `:clojure.spec/invalid`
* `s/explain` - Prints help on spec violations of data
* `s/assert` - Return input or throw an exception
* `clojure.spec.test/instrument` - Enables auto-checking with fdef spec on function invocation
* `s/gen` - Create a test data generator to be used tih test.check
