# Typed Clojure 

An introduction to [clojure.core.typed](http://typedclojure.org/).

Here's some background for the everlasting discussion around the gains and
perils of using a type system:
[What To Know Before Debating Type Systems](http://blogs.perl.org/users/ovid/2010/08/what-to-know-before-debating-type-systems.html).

## TL DR: pros and cons of a static type system

Pros, most important first:

* Only "correct" programs pass the check
* Enables better tooling support for analyzing and changing code
* Documentation that cannot lie
* Enables optimizations during compilation

Cons, no special order:

* Design space is narrowed
* Introduces complexity
* Increases verbosity (in some languages)

A type system is a sophisticated tool. Certainly one can create good
software without a type system. In the end it boils down to the usual
tradeoff "benefit vs. cost".


## Prepare your project

* Add project dependency to
  [clojure.core.typed](https://github.com/clojure/core.typed).
* For Leiningen support add
  [lein-typed plugin](https://github.com/typedclojure/lein-typed).

## First steps in the REPL

`cf` is used in the REPL to infer the type of an expression.

```clojure
(require '[clojure.core.typed :refer [ann cf]])
;= nil
(def bar 1)
;= #'user/bar
(cf bar)
; Type Error (NO_SOURCE_PATH) Unannotated var user/bar
; Hint: Add the annotation for user/bar via check-ns or cf
; in: bar
(cf (ann bar String))
;= Any
(cf bar)
;= java.lang.String
(cf (+ bar 1))
; Type Error (NO_SOURCE_PATH:1:5) Static method
; clojure.lang.Numbers/add could not be applied to arguments:
;
;
; Domains:
;	clojure.core.typed/AnyInteger clojure.core.typed/AnyInteger
;	java.lang.Number java.lang.Number
;
; Arguments:
;	java.lang.String (Value 1)
;
; Ranges:
;	clojure.core.typed/AnyInteger
;	java.lang.Number
;
; in: (clojure.lang.Numbers/add bar 1)
```

Note: sometimes `(cf ...)` has odd results when referring to new forms
that have just been evaluated. This usually improves after a
`(check-ns)`.

## Basic usage in a project

* Exclude clojure.core `defn` in your namespace declaration using `(:refer-clojure :exclude [defn])`.
* Require `[clojure.core.typed :as t :refer [ann Any cf check-ns defn U IFn]]` in namespaces
  that offer type checking.
* Annotate vars with `ann` or use forms `defn` or `fn` from clojure.core.typed namespace.
* Execute a type-check with `(check-ns)` in the REPL.
* On Emacs you can also use
  [typed-clojure-mode](https://github.com/typedclojure/typed-clojure-mode)
  and use `C-c C-x n` to check the current namespace.
* Use [lein-typed plugin](https://github.com/typedclojure/lein-typed)
  in project.clj or in your profile, add namespaces in project.clj to
  the `:core.typed {:check []}` vector and execute `lein typed check` in a shell.

## Types

For an overview of the available types see the
[Types wiki page](https://github.com/clojure/core.typed/wiki/Types).

Some important types

* Every type is a `Any`.
* `nil` is nil. 
* Java types (classes + interfaces) are allowed and treated as expected.
* Scalar types
  * Numeric: `Integer`, `Long`, `clojure.lang.Ratio`, `Double`, `Num` 
  * Others: `Str`, `Character`, `Bool`, `Sym`, `Kw`
* Complex types
  * Sequences: `Seqable`, `Seq`, 
  * Datastructures: `Vec`, `Map`, `Set`, `List`, `HVec`, `HMap`, `HList`
  * Union: `U`
  * Intersection: `I`
* Support for type variables: `All`

Curious about the details? Take a look at the
[Types wiki page](https://github.com/clojure/core.typed/wiki/Types)
and try `cf` expressions in the REPL. For example

```clojure
(cf (seq [:a :b :c]) (Seq Kw))
;= (NonEmptySeq (U (Value :c) (Value :b) (Value :a)))
(cf [:a :b :c] (Seqable Kw))
;= [(HVec [(Value :a) (Value :b) (Value :c)]) {:then tt, :else ff}]
```

## Some examples for function types

No args:
```clojure
(ann forty-two [-> Number])
(defn forty-two [] 42)
```

Variadic:
```clojure
(ann add-numbers [Number Number * -> Number])
(defn add-numbers
  [x & ys]
  (apply + (cons x ys)))
```

Multi-arity:
```clojure
(ann add-many-numbers (IFn [Number Number -> Number]
                           [Number Number Number Number * -> Number]))
(defn add-many-numbers
  ([x y]
     (add-many-numbers x y 0))
  ([x y z & rest]
     (apply + (concat [x y z] rest))))
```

## Example for heterogeneous maps

```clojure
(ann print-person [(t/HMap :mandatory
                           {:name String
                            :age Number}) -> nil])
(defn print-person
  [p]
  (println (:name p) (:age p))
```


With the macro `defalias` we are able to refer to complex
type definitions using a concise name.

Example:

```clojure
(t/defalias Person (t/HMap :mandatory
                           {:name String
                            :age Number}))
(ann print-person [Person -> nil])
(defn print-person
  [p]
  (t/print-env "Hello!")
  (println (:name p) (:age p)))
```

Please note: an alias is only available to
`(cf ...)` after a `(check-ns)` run!


## Use other forms from clojure.core.typed

Example for `defn` with type declarations:

```clojure
(defn inc-or-nil
  [x :- Any] :- (t/Option Number)
  (if (number? x) (inc x) nil))
```

Here, `(t/Option Number)` is equivalent to `(U nil Number)`.

Unfortunately there is no `defrecord>`, you have to use
```clojure
(t/ann-record Address [street :- String zipcode :- Integer])
(defrecord Address [street zipcode])
```
which contains redundant information... but wait, this is Clojure,
isn't it? We can help ourselves:

```clojure
(defmacro defrecord>
  [sym fields-and-types]
  (let [fields (->> fields-and-types (partition 3) (map first))]
    `(do (clojure.core.typed/ann-record ~sym ~fields-and-types)
         (defrecord ~sym ~(vec fields)))))
```

This allows us to define a record concisely:
```clojure
(defrecord> Address [street :- String zipcode :- Integer])
```


## Exercises

Use in the REPL `(cf form expected-type)` to check your type definition for:

1. A type for `(atom {:name "Foo" :age "42"})`.
1. A type for the function `inc`.
1. A type for the function `println`.
1. A typed function `street-length` that takes an Address and
   returns the number of chars of the :street field.
