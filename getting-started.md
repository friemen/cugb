# Getting started with Clojure

Some hints to make you find your way easier.

First User Group Meeting 2013 featured some [introductory slides](webapp/Clojure tools & ecosystem.pdf), thanks to Gerrit Hentschel.

## Development Environment

 * [Emacs Live](http://overtone.github.io/emacs-live/)
 * [Eclipse Counterclockwise](http://code.google.com/p/counterclockwise/)
 * [Lighttable](http://www.lighttable.com/)
 * [Leiningen](http://leiningen.org/)
 * [Lein-try](https://github.com/rkneufeld/lein-try)

## Links

 * [Clojure.org](http://clojure.org)
 * [Introduction](http://java.ociweb.com/mark/clojure/article.html)
 * [Cheatsheet](http://clojure.org/cheatsheet)
 * [Clojure Documentation](http://clojure-doc.org/)
 * [Clojars](https://clojars.org/)
 * [Toolbox](http://www.clojure-toolbox.com/)
 * [Planet Clojure](http://planet.clojure.in/)
 * [4Clojure](http://www.4clojure.com/)
 * [Labrepl](https://github.com/relevance/labrepl)
 * [German Clojure-de group](https://groups.google.com/forum/#!forum/clojure-de)

## Some fundamental talks by Rich Hickey

 * [Are We There Yet?](http://www.infoq.com/presentations/Are-We-There-Yet-Rich-Hickey), 2009
 * [Simple Made Easy](http://www.infoq.com/presentations/Simple-Made-Easy), 2011
 * [The Value of Values](http://www.infoq.com/presentations/Value-Values), 2012
 * [The Language of the System](http://www.youtube.com/watch?v=ROor6_NGIWU), 2012
 * [Design, Composition and Performance](http://www.infoq.com/presentations/Design-Composition-Performance), 2013

## Books

 * Emerick, Carper, Grand - Clojure Programming
 * Fogus, Houser - The Joy of Clojure
 * Halloway - Programming Clojure
 * Higginbotham - [Clojure for the Brave and True](http://www.braveclojure.com/) (online)
 * Sierra, VanderHart - ClojureScript: Up and Running
 * VanderHart, Neufeld - Clojure Cookbook
 * Sotnikov - Web Development with Clojure
 
# Taming the REPL

Read this awesome blog post [Pimp my REPL](http://dev.solita.fi/2014/03/18/pimp-my-repl.html)
to learn about additional tooling for the REPL.

## REPL cheatsheet

 * clojure.core
   * `*ns*` denotes currrent namespace
   * `(ns namespace)` - Create or switch to namespace.
   * `(ns-interns namespace)`, `(ns-publics namespace)` - Print internal or public symbols of namespace.
   * `(ns-unmap namespace sym)` - Unmap symbol from namespace.
   * `(macroexpand-1 quoted-expr)` - Expand one level of macro application.
 * clojure.repl
   * `(dir ns)` - Print sorted list of public vars of namespace.
   * `(doc sym)` - Print docstring of var referenced by given symbol.
   * `(pst)` - Print stack trace.
   * `(source sym)` - Print source.
 * clojure.pprint
   * `(pp)` - Pretty print last REPL output.
   * `(pprint expr)` - Pretty print given object.
 * clojure.tools.trace, requires project dependency to [org.clojure/tools.trace](https://github.com/clojure/tools.trace)
   * `(trace-ns namespace)` - Add tracing to all functions in a namespace.
   * `(untrace-ns namespace)` - Remove tracing to all functions in a namespace.
 * clojure.tools.namespace.repl, requires project dependency to [org.clojure/tools.namespace](https://github.com/clojure/tools.namespace)
   * `(refresh)` - Reload all namespaces from their files within a project.

## Use your User Profile

Use [vinyasa](https://github.com/zcaudate/vinyasa) to get more out of the REPL.

Example of a simple ~/.lein/profiles.clj

```clojure
{:user  {:plugins [[cider/cider-nrepl "0.1.0-SNAPSHOT"]]
         :dependencies [[leiningen "2.3.4"]
                        [im.chit/vinyasa "0.1.8"]]
         :repl-options {:port 9090 }
         :injections [(require 'vinyasa.inject)
                      (vinyasa.inject/inject 'clojure.core '>
                                             '[[clojure.repl doc source]
                                               [clojure.pprint pprint pp]])]}}
```

The profile above will make symbols >doc, >source, >pprint and >pp
available in every namespace.


## More tricks in the REPL

Redirect output from native threads:

```clojure
(alter-var-root #'*out* (fn [_] *out*))
```

Do complete macroexpansion:

```clojure
(use 'clojure.walk)
;-> nil
(macroexpand-all '(-> foo bar baz wat))
;->  (wat (baz (bar foo)))
```

Get Clojure version:

```clojure
*clojure-version*
;-> {:major 1, :minor 5, :incremental 1, :qualifier nil}
```

Limit length for output of sequences:

```clojure
(set! *print-length* 10)
;-> 10
(iterate inc 1)
;-> (1 2 3 4 5 6 7 8 9 10 ...)
```

Open Javadoc in browser:

```clojure
(use 'clojure.java.javadoc)
;-> nil
(javadoc java.util.Set)
;-> "http://java.sun.com/javase/6/docs/api/java/util/Set.html"
```


