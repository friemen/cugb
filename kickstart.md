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
* Projector (we can bring our own)


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

Every `arg` is itself either an expression or a symbol. Before it is
passed to a function invocation it is evaluated, unless it is quoted.

**Exercise**: In the file `src/practising/core.clj` enter your first
hello world expression: `(println "Hello World")`. Evaluate it, you
should see the text "Hello World" printed in the REPL.

**Exercise**: Quoting prevents the evaluation. In the REPL try to
evaluate an expression like `(+ x y)` and then try `'(+ x y)`.


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
  them with _threading_ macros and the use of `[]` and `{}`
  brackets. You'll not have more brackets in your Clojure code than in
  code written in your favorite C-style language.


## Using comments

* The `;;` form is for one line comments, either whole line or rest of
  line.

* The `#_` reader macro helps you let the reader skip the immediate
  following expression, which is very useful inside expressions.

* A `(comment ...)` must still be a well-formed S-expression and is
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

**Exercise**: Concatenate 2 strings using function `str`

**Exercise**: Convert a string to a keyword and vice versa, using
functions `keyword` and `str`.



### Collection types

As part of the core, Clojure offers a small variety of _immutable_
datastructure types:

* Vector: `[1 "foo" :bar]`

* Map: `{:one 1, "Two" 3.0}`

* Set: `#{1 'Two 3.0}`

* List: `'(1 "Two" :three)`


There are quite some common functions that work on all datastructures
in a sensible way.


**Exercise**: Define an example datastructure in namespace
`practising.core` for each of the types shown above using an
expression like `(def myvector ...)`. Evaluate the whole namespace,
inspect the contents of your definitions in the REPL, change one the
definitions and re-evaluate it.

**Exercise**: Value lookup in maps can be done in three ways. You can
use `get`, or a map as function on keywords or a keyword as a function
on maps. Define a map whose keys are keywords. Try out
each of these ways to lookup a value.

**Exercise**: Try to apply the following functions to each of your data structures:

* first

* rest

* last

* conj

* count

* get

* seq

* empty

**Exercise**: Visit the official
[cheatsheet](https://clojure.org/api/cheatsheet), read in the section
for "Collections" about the datastructure type-specific functions for
maps, vectors and sets. Try out functions like `conj`, `assoc`,
`dissoc`, `disj` etc. on your example data. To use typical functions
for sets (like `union` or `difference`) you'll need to learn a bit
about namespaces, see the upcoming section.

**Exercise**: Be aware that vectors are associative, with an index
being the lookup key. This allows us to apply certain map-like
operations to them. Use this idea to replace an existing value in a
vector.

## Namespaces

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

You can inspect a namespace at runtime, and the symbol `*ns*` always
refers to your current namespace. The expression `(ns-interns *ns*)`
results in a map of all these definitions. In our example, this would
return the same as `(ns-interns 'practising.core)`.

To use public definitions located in other namespaces a namespace must
require them first. The typical way is like this:

```clj
(ns my.beautiful.ns
  "Contains my best code ever."
  (require [clojure.string :as str]))```

(defn first-funny-function
  [s]
  (str/split s #","))
```

The `str` here is used as an alias for anything reachable in
`clojure.string` namespace. Please note, that this alias does not
clash with the `clojure.core` function `str`.

**Exercise**: Require namespace `clojure.set` with alias `set` and try
out functions like `set/difference` or `set/intersection`.


## Functions

Clojure is a functional programming (FP) language. While
object-oriented programming uses the _object_ (together with its
blueprint _class_) as the smallest building block, FP languages are
based on _functions_ operating on a small spectrum of datastructures.


Functions are values. This means

* we can create them anywhere with an expression `(fn [x] ...)`.

* we can pass them to other functions (promoting these other functions to *higher-order*).

* we can return them as values.


### Forms to create functions

There are two ways to define a function:

* The first is a combination of `def` and `fn` and results in a
  **top-level function definition** in a namespace, making it available for any
  other function:

```clj
(defn average-age
  [persons]
  ...)
```

This is the equivalent of writing:

```clj
(def average-age
  (fn [persons]
    ...))
```


* The other is the **anonymous form** (a.k.a *lambda expression*),
  occurring often within a surrounding function:

```clj
(defn wrap-logging
  [handler]
  (fn [request]                        ;; <-- creates an anonymous function
    (log/debug "REQUEST:" request)
    (let [response (handler request)]
	  (log/debug "RESPONSE:" response)
	  response)))
```


For the anonymous form there is an even more compact notation. For example, instead of

```clj
(map (fn [x] (/ x 2)) numbers)
```

you're allowed to write

```clj
(map #(/ % 2) numbers)
```

Please note that the latter form can make your code much harder to
understand if the anonymous function becomes more complex.

### Closures

Anonymous functions can close over symbols visible in their
surrounding scope, making them *closures* that carry values:

```clj
(defn make-adder
  "Returns a 1-arg function adding x to its argument."
  [x]
  (fn [y]
    (+ x y)))

=> (def add-3 (make-adder 3))
#'add-3

=> (add-3 2)
5
```

### Function arguments

Formal arguments are defined in a vector of symbols after the
docstring of a function:

```clj
(defn round
  "Round down a double to the given precision (number of significant digits)"
  [d precision]
  ...)
```

A single function can support *multiple arities*. In addition, you can
define a *variadic* function that accepts any number of arguments.
For our workshop goals, we don't need to go into the details here. If
you are curious there is guidance in the [Clojure docs on
functions](http://clojure-doc.org/articles/language/functions.html).


Clojure functions support a nifty way to bind data pieces in complex
datastructures to local symbols, widely known as *destructuring*. The
exact same tool is also available in `let` and `for` expressions.

Just as a glance, suppose you need to process a map entry, represented
as a pair `[key value]` in one of your functions. Instead of writing

```clj
(defn uppercase-value
  [map-entry]
  [(first map-entry) (str/upper-case (second map-entry)))])
```

you can write

```clj
(defn uppercase-value
  [[key value]]
  [key (str/upper-case value)])
```

This is called *positional destructuring*.

There is also support for *map destructuring*, useful for the very
common case of processing a map like this:

```clj
(def track {:title "Be True"
            :artist "Commix"
            :genre "Drum & Bass"})

(defn track->str
  [{:keys [artist title]}]
  (str artist " - " title))
```

These examples provide only a first idea. Destructuring in Clojure is
much more powerful, and can be extended further by libraries like
[plumbing](https://github.com/plumatic/plumbing). It very much leads
to more readable code, therefore it is used quite often. For more
detail, you should visit the [Clojure docs on
destructuring](http://clojure-doc.org/articles/language/functions.html#destructuring-of-function-arguments)


### Visibility

Functions defined with `defn` are *public*, which means any code
outside the namespace can depend on it. It is good style to have per
namespace a sharp distinction between the set of functions comprising
the API and internal implementation details.

In order to limit what a namespace offers to the rest of the world
Clojure allows us to attach metadata to any Var in a namespace:

```clj

(def ^:private a-constant 42)

(defn ^:private some-intermediate-calculations
  [...]
  ...)
```

Since private functions are very common there is a macro `defn-` to
reduce visual clutter:

```clj
(defn- some-intermediate-calculations
  [...]
  ...)
```


### Functions on functions

This section is not really necessary to follow the workshop. It shows
to the curious a little of the power that Clojure offers when working
with functions.

* `(partial f a b ...)` allows you to apply a function `f` to a subset
  of the required arguments resulting in a new function that has those
  arguments fixed:

```clj
(defn add
  [x y z]
  (+ x y z))

(def add-12 (partial add 5 7))

=> (add-12 3)
15
```

* `(apply f coll)` helps when we have an n-arity function `f` and an n-element
  collection `coll`, and want to invoke `f` with the elements of
  `coll` as arguments:

```clj
(defn add
  [x y z]
  (+ x y z))

(def numbers [1 2 3])

=> (add numbers)
;; will throw an ArityException

=> (apply add numbers)
6
```

* `(comp f g)` composes two functions `f` und `g` (or more) so
  that the resulting function behaves on `x` like `(f (g x))`:

```clj
(def str->id
  (comp str/trim str/lower-case))

=> (str->id "  ABC ")
"abc"
```

* `(memoize f)` produces a function that caches results of a function `f`:

```clj
(defn- my-really-costly-calculation-impl
  [a b]
  ...)

(def my-really-costly-calculation
  (memoize my-really-costly-calculation-impl))

```

* `(juxt k1 k2 ...)` returns a function that looks up values for the
  provided keys `k1`, `k2` etc. and delivers them in one vector:

```clj
(def persons [{:firstname "Peter" :lastname "Pan"}
              {:firstname "Daisy" :lastname "Duck}])

=> (map (juxt :firstname :lastname) persons)
(["Peter" "Pan"]
 ["Daisy" "Duck"])
```

* `(fnil f initial-value)` returns a function that replaces its first
  argument with `initial-value` in case it is nil. The benefit becomes
  clearer when recognizing that most "modification" functions like
  `conj` or `assoc` expect a collection as their first argument. When
  building up new datastructures `fnil` is an elegant tool to handle
  initialization cases.

```clj
(def db {})

=> (update db :persons (fnil conj []) {:firstname "Donald" :lastname "Duck"})
{:persons [{:firstname "Donald" :lastname "Duck"}]}

```


## System design in functional programming

An important property of a function is *purity*. A function is called
*pure* if its result depends only on its arguments and if it does not
change anything in its environment (in other words: it has no
side-effects). Pure functions are pleasant because they are

* easy to reuse.

* easy to test.

* thread safe.

* candidates for memoization.

As a result, we want to have as many of them around us as possible.
However, a system created of 100% pure functions is useless: no access
to any input, no place to write any output to. We need to have some of
our code do the "dirty job".

So the fundamental principle of program design in FP is:

1. Build as much of the system as possible as a pure transformation
   of data into other data.

2. Allow only very few pieces of code to interact with the world
   outside (that is: read and write data).


## Conditional evaluation

Clojure offers many ways to express conditional evaluation: `if`,
`if-let`, `when`, `when-let`, `cond`, `case`, `condp`, and on top of
this there are conditional threading operators (introduced in the
upcoming section). But don't be daunted, most of the time `if` or
`cond` will do, all others offer more or less syntactic sugar.

Here's the grammar of `if`, which does not offer any surprises:

```
(if <test-expr>
  <then-expr>
  <else-expr>?)
```

As the else-expr is optional the if will return nil if the test-expr
fails to return a truthy value.


If you need more than two branches then `cond` will help:

```
(cond
  <test-expr1>  <then-expr1>
  <test-expr2>  <then-expr2>
  ...
  :else <else-expr>)
```

The first then-expr whose preceding test-expr returns a truthy value
will be the evaluation result of the cond, otherwise the else-expr
when present, otherwise nil.


The `case` expression is more akin to the `switch` in C-style
languages:

```
(case <expr>
  <value1>  <then-expr1>
  <value2>  <then-expr2>
  ...
  <else-expr>)
```

The values can be any literals, even vectors or maps. If there is no
else-expr and none of the values matches the result of expr then an
exception is thrown.

To learn about `condp`, a nifty macro that reduces clutter in a
special case of branching, you should try the next exercise.

**Exercise**: Look at the following function and consult the [docs on
condp](https://clojuredocs.org/clojure.core/condp). Replace `cond`
with `condp`.


```
(defn score->grade
  [score]
  (cond
    (<= 90 score) "A"
    (<= 80 score) "B"
    (<= 70 score) "C"
    (<= 60 score) "D"
    :else "F")))
```


## Local symbols with let

Suprisingly many functions work well as just one pipeline of function
invocations. (The section about threading explains how we can limit
the nesting of expressions.)

Of course, there are numerous situations where we wish to bind an
intermediate result within a function to a local symbol. In imperative
languages we use local variables for this job, and it may look and
feel as we can do the same in Clojure, but conceptually symbols just
refer to evaluation results, `let` does not give us "boxes with
varying content".


To introduce local symbols we use `let`, as in this example:

```
(defn path->filename
  [path]
  (let [parts (remove str/blank? (str/split path #"\/"))]
    (if (not (empty? parts))
      (last parts))))
```

In one `let` you can have as many symbol-expression pairs as you like,
and you can use destructuring where you would normally place the
symbols.

There is also `if-let`, which is helpful when your let body should be
evaluated only if a test yields a truthy (non-nil, non-false) value:

```
(defn path->filename
  [path]
  (if-let [parts (seq (remove str/blank? (str/split path #"\/")))]
    (last parts)))
```

The `seq` here is like a test, because it returns either a sequence
(truthy) or nil if the resulting sequence would be empty. It's the
idiomatic way of writing `(not (empty? ...))`.


## Threading macros

Excessive nesting of expressions makes it much harder to read and
understand what is going on within a function. One mitigation is the
use of `let` to introduce descriptive symbols, the other is
*threading*.

Compare these examples, whose result is the same:

```clj
(assoc-in
  (assoc-in person [:employer :name] "doctronic")
  [:address :street]
  "Frankenstrasse 6")
```


```clj
(let [person-1
      (assoc-in person [:employer :name] "doctronic")

      person-2
      (assoc-in person-1 [:address :street] "Frankenstrasse 6")
  person-2)
```


```clj
(-> person
    (assoc-in [:employer :name] "doctronic")
    (assoc-in [:address :street] "Frankenstrasse 6"))
```

Threading macros reorganize your code at compile time. The operator
`->` (called *thread-first*) takes the initial expression and inserts
it as the *first* argument into the second expression, continuing
until everything is nested.

It has a sibling `->>` (called *thread-last*) doing the analogue with
the *last* argument, which is often needed for sequence processing
chains.

And both have cousins like `cond->`, `cond->>`, `some->` and `some->>`
that help with conditional processing steps.

**Exercise**: Use `macroexpand` and apply it to the thread-first
example show above. (Don't forget to quote the expression to prevent
the evaluation.)

**Exercise**: Use `some->>` to rewrite the `path->filename` function


## Sequence processing

Clojure excels in data processing tasks. One of the reasons is the
sequence abstraction that can be applied to all datastructures and a
well-designed set of core functions that transform seqs into other
seqs.

A consequence is that idiomatic Clojure code contains almost no
looping. Another consequence is that programmers accustomed to
imperative `for` and `while` loops need to re-learn how to process
data on a significantly higher level. On this level, the brain is no
more bothered with irrelevant details, however it is confronted with
unfamiliar tools and solution strategies.

**Exercise**: Most sequence processing functions like `map`, `filter`
etc. expect a sequence and ensure this by using `seq` on their
argument. Apply `seq` to datastructures like a map, a set or a vector.

**Exercise**: Define a vector of persons, each with a name and an
age. Write a `filter` expression that selects all persons in the age
between 20 and 29.

Clojure offers the following different approaches for processing
sequences:

* `map`, `mapcat`, `filter`, `reverse`, `sort` and friends are usually
  good when you target a sequence as result. Building up a chain of
  these operations in combination with the thread-last macro `->>`
  often yields the most elegant and maintainable solution. By far the
  biggest portion of sequence processing in idiomatic Clojure code is
  done on this basis.

* `for` is Clojure's list comprehension operator. Be careful to not
  confuse it with the C-style for loop. It is great for traversing
  nested datastructures, when your goal is a one-dimensional result
  sequence. It is well suited for templating code, for example when
  rendering HTML or XML elements. It includes destructuring, local
  symbols and conditions.

* `reduce` is a swiss army knife that can produce almost anything,
  sometimes leading to convoluted solutions. A reduction is often the
  terminal step of a sequence processing chain (for example `into` is
  only a special purpose reduction).

* `doseq` is the imperative variant of a list comprehension. It
  provides the same traversal power as `for` and should be used
  exclusively for side-effects, for example writing out a bunch of
  files to disk.

* Old school function recursion is still a valid approach, and may
  yield the cleanest code in some situations, but be aware that your
  call-stack may limit your problem size. The raw mechanics of full
  tree traversion is already provided in
  [clojure.walk](https://clojure.github.io/clojure/clojure.walk-api.html).

* `loop-recur` is a manual tail call optimization for recursive
  operations, and effectively the most low-level construct. It's
  sometimes unavoidable, for example if you build a parser, or need to
  consume or produce several distinct results. While learning Clojure
  you might often find yourself longing for a quick loop-recur sin,
  but you should then ask yourself twice if there is no better tool
  for the job at hand.


Coming to a decision on how to approach a data transformation problem
boils down to looking at the list above from top to bottom and picking
the tool that yields the nicest code.

Rules of thumb

* If you need to traverse a more-dimensional data structure then give
`for` a try, otherwise see if a combination of `map`, `filter` and
others, perhaps terminated with a `reduce`, does the job.

* If you need side-effects then `doseq` is probably the best bet.

* To aggregate data into a single value (which can be a map) a
  reduction is typically ideal.
