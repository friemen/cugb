IMPORTANT:
This readme only contains the common first steps.
Some steps are IDE specific. 
For those, please consult the corresponding text file.


What software do I need?
========================

For Clojure development you'll need three things:

- Java SDK
- Leiningen (the Clojure build automation tool)
- IDE / editor

You can find all necessary software for Linux, MacOSX 
and Windows in the software/ directory.


Installation
============

Install JDK if missing, or make sure >= 1.6 is available with command: 
java -version

For Leiningen follow these steps, adapted from http://leiningen.org/

You'll find the lein script in the software/ directory.
Place it on your $PATH where your shell can find it (eg. ~/bin)
Set it to be executable (chmod a+x ~/bin/lein)
Run it (lein) and it will download the self-install package


Select one IDE
- Eclipse
- IntelliJ
- LightTable
- Emacs
Install it, add plugins as necessary
(==> please see IDE dependant instructions)


Create helloweb project
=======================

Create a project with command: 
lein new helloweb

Leiningen creates a project skeleton for you.
You'll find the first source file in src/helloworld.clj.


Start coding
============

Open src/helloweb/core.clj and start REPL = read-eval-print-loop
(==> please see IDE dependant instructions)


Add a function like

(defn foo
  []
  (println "Foo"))

Evaluate it in REPL


