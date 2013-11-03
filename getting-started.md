# Getting started with Clojure

Some hints to make you find your way easier.

First User Group Meeting 2013 featured some [introductory slides](webapp/Clojure tools & ecosystem.pdf), thanks to Gerrit Hentschel.

## Development Environment

 * [Emacs Live](http://overtone.github.io/emacs-live/)
 * [Eclipse Counterclockwise](http://code.google.com/p/counterclockwise/)
 * [Lighttable](http://www.lighttable.com/)
 * [Leiningen](http://leiningen.org/)
 * [Lein-try](https://github.com/rkneufeld/lein-try)

## In the REPL

 * clojure.core
   * ns - Switch to namespace.
   * ns-interns, ns-publics - Print internal or public symbols of namespace.
   * ns-unmap - Unmap symbol from namespace.
   * macroexpand-1 - Expand one level of macro application.
 * clojure.repl
   * dir - Print sorted list of public vars of namespace.
   * doc - Print docstring of var referenced by given symbol.
   * pst - Print stack trace.
   * source - Print source.
 * clojure.pprint
   * pp - Pretty print last REPL output.
   * pprint - Pretty print given object.
 * clojure.tools.trace [org.clojure/tools.trace](https://github.com/clojure/tools.trace)
   * trace-ns - Add tracing to all functions in a namespace.
   * untrace-ns - Remove tracing to all functions in a namespace.
 * clojure.tools.namespace.repl [org.clojure/tools.namespace](https://github.com/clojure/tools.namespace)
   * refresh - Reload all namespaces from their files within a project.

To redirect output from native threads enter in a REPL:

```clojure
(alter-var-root #'*out* (fn [_] *out*))
```

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
 * Sierra, VanderHart - ClojureScript: Up and Running
 * VanderHart, Neufeld - Clojure Cookbook (upcoming)
 * Sotnikov - Web Development with Clojure (upcoming)
 
