# Introduction to ClojureScript

![ClojureScript](https://github.com/friemen/cugb/blob/master/cljsintro/cljs-logo.png)

## Prerequisites
* [JavaSE 7, better 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* For headless unit test execution: [PhantomJS](http://phantomjs.org/)
* For compiling + dependency resolution: [Leiningen](http://leiningen.org/) or [Boot](http://boot-clj.com/)
* IDE with integrated REPL: [Emacs/CIDER](https://github.com/clojure-emacs/cider), [IntelliJ/Cursive](https://cursiveclojure.com/), [Eclipse/Counterclockwise](http://doc.ccw-ide.org/documentation.html)

If you just want to stick your toe in as illustrated in this
[guide](https://github.com/clojure/clojurescript/wiki/Quick-Start), a
JDK and the
[cljs.jar](https://github.com/clojure/clojurescript/releases)
suffices.


## Example projects

* [Quick-Start](https://github.com/friemen/cugb/tree/master/cljsintro/1-cljs-quickstart)
* [Boot](https://github.com/friemen/cugb/tree/master/cljsintro/2-boot)
* [Boot with Om + core.async](https://github.com/friemen/cugb/tree/master/cljsintro/3-boot-om-core-async)
* [Leiningen with Om + core.async](https://github.com/friemen/zackzack)
* [Leiningen with Quil](http://www.falkoriemenschneider.de/snowyforest/)

## Resources

### General
* [On GitHub](https://github.com/clojure/clojurescript)
* [Latest releases](https://github.com/clojure/clojurescript/releases)
* [Cheatsheet](http://cljs.info/cheatsheet/)
* [Quickstart Guide](https://github.com/clojure/clojurescript/wiki/Quick-Start)
* [In Maven Central](http://search.maven.org/#search|ga|1|clojurescript)
* [Google Closure API](https://google.github.io/closure-library/api/)
* [Clojure vs. ClojureScript](https://github.com/clojure/clojurescript/wiki/Differences-from-Clojure)
* [Compiler options](https://github.com/clojure/clojurescript/wiki/Compiler-Options)
* [Online Tutorial](https://www.niwi.nz/cljs-workshop)
* [Popular JS libraries, packed for Cljs](http://cljsjs.github.io/)

### Leiningen related
* [lein-cljsbuild Plugin](https://github.com/emezeske/lein-cljsbuild)
* [Chestnut Project Template](https://github.com/plexus/chestnut)
* [Luminus](http://www.luminusweb.net/docs/clojurescript.md)

### Boot related
* [boot-cljs](https://github.com/adzerk-oss/boot-cljs)
* [boot-cljs-test](https://github.com/infracanophile/boot-cljs-test)
* [Tenzing Project Template](https://github.com/martinklepsch/tenzing)
* [Project example](https://github.com/adzerk-oss/boot-cljs-example)

### Some important libraries
* [bidi](https://github.com/juxt/bidi) - Request Routing (browser and backend)
* [cljs-http](https://github.com/r0man/cljs-http) - HTTP client
* [cljs-time](https://github.com/andrewmcveigh/cljs-time) - Date/Time related functions in ClojureScript
* [core.async](https://github.com/clojure/core.async) - CSP as a library for Clojure and ClojureScript
* [datascript](https://github.com/tonsky/datascript) - Datalog in the browser
* [enfocus](https://github.com/ckirkendall/enfocus) - Client-side HTML templating
* [jayq](http://github.com/ibdknox/jayq) - JQuery wrapper
* [om](https://github.com/omcljs/om) - React wrapper with global state
* [prismatic/schema](https://github.com/Prismatic/schema) - Data type specification
* [prismatic/om-tools](https://github.com/Prismatic/om-tools) - Schema and Om
* [quil](http://quil.info/) - [Processing](https://processing.org/) wrapper for Clojure and ClojureScript
* [reagent](https://reagent-project.github.io/) - React wrapper
* [sablono](https://github.com/r0man/sablono) - Create React DOM objects from Hiccup-style data
* [secretary](https://github.com/gf3/secretary) - Request Routing (browser)
* [transit-cljs](https://github.com/cognitect/transit-cljs) - Remote communication serialization

### Play around
* [Cljs REPL as-a-Service](http://himera.herokuapp.com/index.html)
* [Quil](http://quil.info/)
