# Introduction to namespaces

## Simple commands in the REPL

`(ns foo.bar)` switches to namespace foo.bar.

`(use 'foo.baz)` compiles and loads foo.baz namespace into the current namespace.

`*ns*` references the current namespace.

`(def s (...))` interns a var in the current namespace that can be referenced by the symbol `s`.

`(ns-unmap *ns* 's)` removes the var that the symbol `s` references from the current namespace.


## Query functions on namespaces

`ns-interns` returns a map of symbols to vars that the given ns defines.

`ns-publics` like ns-interns, but only public mappings.

`ns-refers` returns a map of symbol to vars that the given ns refers to. 

`ns-imports` returns a map of symbol to class names that the given ns refers to.

`ns-map` returns a map of all namespace mappings of the given ns.


## Excercise 1

 * Define some symbols in the current namespace.
 * List all symbols that the current namespace defines.
 * Remove one mapping.


## The ns form

An example

```clojure
(ns foo.bar "documentation"
  (:require [clojure.tools.logging :as log]      ; introduce ns alias
            [clojure.test :refer :all]           ; use all public vars
			[clojure.string :refer [blank?]]     ; use only selected vars
			[:refer-clojure :exclude [map]])     ; exclude clojure default vars
  (:import [java.utils ArrayList HashMap]))
```


## Refresh using clojure.tools.namespace

`(use '[clojure.tools.namespace.repl :only (refresh)])` imports the refresh function.

`(refresh)` destroys and reloads all namespaces that have changed since the last refresh.


## Exercise 2

 * Create a map of name to function var for all functions with a suffix like "-page".
 
```clojure
(defn s-page
  []
  "S")


(nsfns "-page" *ns*)
;=> { "s" #'user/s-page }
```


# License

Copyright © 2013 F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
