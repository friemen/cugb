# Clojure Kickstart

Most people with a background in static imperative OO programming face
initial difficulties in getting started with a dynamic Lisp-style
functional language like [Clojure](http://clojure.org/). Which is sad
because programming in Clojure is a great experience!

The first hurdle is setting up a decent development environment that
lets you enjoy the interactive nature of Clojure.

The next step would be to have a toy project. It should do
significantly more than print `Hello World`, and it must have a direct
connection to everyday programming tasks.

And finally, novices should get a well-chosen list of
[hints and links](https://github.com/friemen/cugb/blob/master/getting-started.md)
to continue learning on their own.

# Scope of the workshop

The three things listed above are exactly what we try to accomplish in
a 3-hour workshop. Don't expect having mastered the language
afterwards, but you can expect to be well-prepared for learning
Clojure and deep-dive into it's ecosystem.

And this is how we get you started:
 * Setup a development environment. We decided to
   offer [VS Code with a Clojure extension](https://marketplace.visualstudio.com/items?itemName=avli.clojure)
   as out-of-the-box-copy-deployment-package.
 * Create your own Clojure project with [Leiningen](http://leiningen.org/).
 * Learn to work with a
   [REPL](http://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop)
   and structural editing (a.k.a Paredit)
 * Introduction to the syntax and some important functions.
 * Jump right into a ready-made webapp based on Clojure and ClojureScript.

And if you want to continue learning Clojure afterwards, you can also
join local [user group for Cologne/Bonn area on Meetup](https://www.meetup.com/Clojure-User-Group-Bonn/).

## Prerequisites for participation

Each participant should have an own notebook with at least 8 GB RAM,
ready to run [Java](http://docs.oracle.com/javase/8/) (version >=
8).

Prior amateur knowledge of at least one programming language (for
example C++, Python, Ruby, Java, any Lisp, Scala) is required.

## Organizational requirements

* Max. number of participants: 20
* A room with decent power supply
* Internet access
* Beamer (we can bring our own)


# Curriculum

**Exercise**: Create a new project called practising: `lein new practising`. Change
directory into the new folder. Then start a REPL using `lein repl` and
connect to it with your editor.

Open the file `src/practising/core.clj`.

## The S-expression

Clojure is a Lisp. Code is organised in possibly nested expressions of
the form:

```
(operator arg1 arg2 arg3 ...)
```

The `operator` is something we can invoke, usually a _function_. There
are also _special forms_ and _macros_.

Every `arg` is itself either an expression or a symbol.

**Exercise**: In the file `src/practising/core.clj` enter your first
hello world expression: `(println "Hello World")`. Evaluate it, you
should see the text "Hello World" printed in the REPL.


There are some notable facts about this way of using brackets:

* Code is not structured as a sequence of text lines, but a tree of
  expressions. This changes the way we can navigate and manipulate our
  code. Lisp leverages _Paredit_, which is elsewhere known as
  structural editing. Paredit manages the balancing of brackets for
  you. This gives you more power after you learned to use these new
  tools.

* Code organization is very uniform: It's always prefix
  notation. There is no need for operator precedence rules. Arithemtic
  operators can be handled just if they were functions. On the other
  hand, arithmetic expressions in prefix notation look unusual and this
  takes some practise.

* The syntactic basis of expressions are in effect lists, in other
  words: the code of Clojure is expressed in terms of Clojure's data
  representation. This idea is called "code-is-data", or
  "homoiconicity" for those who want to sound very smart. Since a Lisp
  is best at manipulating lists it can easily be used to create
  code. _Macros_ look just like functions but are in effect embedded
  code generators, written in plain Clojure, executed at compile
  time. All of this means: You can morph your language in almost any
  direction that helps you better describe solutions for your problem
  domain.

* Excessive nesting of expressions and overloading in meaning of
  parentheses are typical drawbacks of Lisps, but Clojure mitigates
  them but _threading_ macros and the use of `[]` and `{}`
  brackets. You'll not have more brackets in your Clojure code than in
  code written in your favorite C-style language.


## Using comments

* The `;;` form is for one line comments, either whole line or rest of
  line.

* The `#_` reader macro helps you let the reader skip the following expression, very
  useful inside expressions.

* The `(comment ...)` must still be a well-formed S-expression and is
  used to encapsulate blocks of code that is used only for
  experimentation in development time.


## Working with data

### Scalar types

* _Numbers_ map to Java and JS number types

  * Automatic coercion

  * Rational numbers

* _Strings_ are Java or JS strings

* _Boolean_ values are `true` and `false`. Non-nil values are considered 'truthy'.

* _nil_ is null, it's the only 'falsey' value beside `false`.

* _Symbols_ are used for identifiers in code

* _Keywords_ are similar to Strings or Symbols, but can be used with namespace scoping.


**Exercise**: Calculate the average of the numbers 32, 23 and 1 with the
functions `+` and `/`.

**Exercise**: Concatenated 2 strings using function `str`

**Exercise**: Convert a string to a keyword and vice versa, using
functions `keyword` and `str`.



### Collection types

As part of the core, Clojure offers a small variety of _immutable_
datastructure types:

* Vector: `[1 "foo" :bar]`

* Map: `{:one 1, :two "Two"}`

* Set: `#{1 'Two 3.0}`

* List: `'(1 "Two" :three)`


There are quite some common functions that work on all datastructures
in a sensible way.


**Excercise**: Define an example datastructure in namespace
`practising.core` for each of the types shown above using an
expression like `(def myvector ...)`. Evaluate the whole namespace,
inspect the contents of your definitions in the REPL, change one the
definitions and re-evaluate it.

**Exercise**: Try to apply the following functions to each of your data structures:

* first

* rest

* last

* conj

* count

* get

* seq

**Exercise**: Visit the official
[cheatsheet](https://clojure.org/api/cheatsheet), read in the section
for "Collections" about the datastructure type-specific functions for
maps, vectors and sets. Try out functions like `conj`, `assoc`,
`dissoc`, `disj` etc. on your example data. To use typical functions
for sets (like `union` or `difference`) you'll need to learn a bit
about namespaces, see the upcoming section.



### Namespaces

Clojure data and function definitions are organized in
_namespaces_. Imagine a namespace as a dynamic map of symbols to
_Vars_, and think of a _Var_ as a box holding a piece of data or a
function.  (It is tempting to think of a Var as the same as a variable
in imperative languages, and there are indeed similarities. However,
the concept "variable" has no real meaning in functional languages. Be
patient.)

Your file `src/practising/core.clj` has a namespace declaration at the top.

Each `def` or `defn` inside it is effectively a mutation to this map,
executed when the Clojure runtime loads and compiles your namespace.

You can always inspect a namespace at runtime, and the symbol `*ns*`
always refers to your current namespace. The expression `(ns-interns
*ns*)` results in a map of all these definitions. In our example, this
would return the same as `(ns-interns 'practising.core)`.

To use public definitions located in other namespaces a namespace must
require them first. The typical way is like this:

```
(ns my.beautiful.ns
  "Contains my best code ever"
  (require [clojure.string :as str]))```

(defn first-funny-function
  [s]
  (str/split s #","))
```

The `str` here is used as an alias for anything reachable in
`clojure.string` namespace. By the way: this alias does not clash with
the `clojure.core` function `str`.

**Exercise**: Require namespace `clojure.set` with alias `set` and try
out functions like `set/difference` or `set/intersection`.
