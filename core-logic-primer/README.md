# Core Logic Primer

First steps into the wonderful world of logic programming.

## Putting it simply

### A fairly common way of software development

* Analyse the initial situation  
  _(Where am I?)_
* Define the destination to reach  
  _(Where do I want to go?)_
* Work out the transformation strategy  
  _(How do I get there?)_
* Implementing the transformation strategy  
  _(Telling the computer **exactly** and **step-by-step** what to do)_

followed by:

* Re-sharpening the initial situation  
  (sometimes because of an incomplete analysis in the first place)
* Re-sharpening the destination to reach  
  (sometimes because of an inaccurate first definition)
* Re-sharpening the transformation strategy  
  (as a mandatory consequence)
* Debug your implementation  
  (also eliminating [ordinary bugs](http://en.wikipedia.org/wiki/Off-by-one_error))

but at these points in time, the program is already written.
So we are working directly on the code.
And all this re-sharpening feels like _the normal way to debug_.

### An alternate route exists

* Analyse the initial situation thoroughly  
  You will have to do it anyway.
  But do it now. Not while _debugging_.  
  _(Where am I exactly?)_
* Define the destination to reach thoroughly **and** use a formal way to write it down  
  You will have to do it anyway.  
  But do it now and be as precise as possible.  
  _(Where do I want to go precisely?)_
* Use _something_ to process your formal definition of the desired destination.  
  This _something_ will use and combine standard algorithms and automata to
  transform the initial state into the required final state.

If done correctly and completely, you don't need to work out a transformation strategy anymore.
And consequently, you don't have to implement and debug such transformation.

## Getting started

Create a new project

    lein new core-logic-primer

add an additional dependency in your `project.clj`:

```clojure
    [org.clojure/core.logic "0.8.10"]
```

and start using it in your namespace.

```clojure
    (ns core-logic-primer.core
      (:refer-clojure :exclude [== !=])
      (:use [clojure.core.logic]))
```

Some `clojure.core.logic` symbols collide with `clojure.core` symbols.
When writing pure logic code, simply exclude the corresponding `clojure.core` symbols.

If you also want to use **Facts and Relations** (see `rpssl.clj` example), add

```clojure
    (:require [clojure.core.logic.pldb :as db])
```

And for finite domains like _all numbers between 1 and 9_ (see `sudoku.clj` example) just add

```clojure
    (:require [clojure.core.logic.fd :as fd])
```

And to use arithmetic relational operators, you can add

```clojure
    (:use [clojure.core.logic.arithmetic])
```

and prepare for **more colliding symbols**.

## Recommended Route

1. Start your REPL
2. Work through
   [A Core.logic Primer](https://github.com/clojure/core.logic/wiki/A-Core.logic-Primer)
3. Play around in the REPL, at least using
   [==](http://clojure.github.io/core.logic/#clojure.core.logic/==),
   [!=](http://clojure.github.io/core.logic/#clojure.core.logic/!=),
   [conso](http://clojure.github.io/core.logic/#clojure.core.logic/conso),
   [appendo](http://clojure.github.io/core.logic/#clojure.core.logic/appendo),
   [membero](http://clojure.github.io/core.logic/#clojure.core.logic/membero),
   [distincto](http://clojure.github.io/core.logic/#clojure.core.logic/distincto),
   [fresh](http://clojure.github.io/core.logic/#clojure.core.logic/fresh),
   [everyg](http://clojure.github.io/core.logic/#clojure.core.logic/everyg)
4. Visit
   [core.logic Introduction](http://de.slideshare.net/normanrichards/corelogic-introduction)
   (Slides 8 to 29)
5. Try some larger examples:
   * The `sudoku.clj` of this repository
   * [Solving Logic Puzzles With Clojure core.logic](http://blog.jenkster.com/2013/02/solving-logic-puzzles-with-clojures-corelogic.html)
6. Prepare for Facts And Relations using
   [core.logic Introduction](http://de.slideshare.net/normanrichards/corelogic-introduction)
   (Slides 30 to 35)  
   **Note:** The `clojure.core.logic` syntax has slightly changed. See `rpssl.clj` of this repository.
7. Conclude and recap with
   [A Very Gentle Introduction To Relational & Functional Programming](https://github.com/swannodette/logic-tutorial)
8. Find yourself something to implement.
   Maybe a [Mastermind](http://de.wikipedia.org/wiki/Mastermind_%28Spiel%29) solver using `clojure.core.logic`?
9. Share your thoughts and experiences on the next
   [Clojure User Group Bonn](https://groups.google.com/forum/#!forum/clojure-user-group-bonn)
   session

## Background

* [The Reasoned Schemer](http://mitpress.mit.edu/books/reasoned-schemer)

  "The authors of *The Reasoned Schemer* believe that logic programming
  is a natural extension of functional programming (...)"
* [miniKanren](http://minikanren.org/)
* [cKanren](https://github.com/calvis/cKanren)

## Resources

* [core.logic Project Homepage](https://github.com/clojure/core.logic/)
* [core.logic API Reference](http://clojure.github.io/core.logic/)
* [core.logic Introduction](http://de.slideshare.net/normanrichards/corelogic-introduction)
* [A Core.logic Primer](https://github.com/clojure/core.logic/wiki/A-Core.logic-Primer)
* [A Very Gentle Introduction To Relational & Functional Programming](https://github.com/swannodette/logic-tutorial)
* [Solving Logic Puzzles With Clojure core.logic](http://blog.jenkster.com/2013/02/solving-logic-puzzles-with-clojures-corelogic.html)
* [Game Characters with core.logic](http://ir.lib.uwo.ca/cgi/viewcontent.cgi?article=2646&context=etd)
* [Logic Programming is Overrated ](http://programming-puzzler.blogspot.de/2013/03/logic-programming-is-overrated.html)
* [Logic Programming is Underrated](http://swannodette.github.io/2013/03/09/logic-programming-is-underrated/)
