# Introduction to namespaces

The solutions...

## Exercise 2

Create a map of name to function var for all functions with a suffix like "-page".
 
See [nsfns.clj](src/nsfns.clj).

## Exercise 3

The simplest `render` function might look like this:

```clojure
(defn render
  [page]
  (-> (nsfns "-page" *ns*) (get page) deref (apply [])))
```


# License

Copyright © 2013 F.Riemenschneider

Distributed under the Eclipse Public License, the same as Clojure.
