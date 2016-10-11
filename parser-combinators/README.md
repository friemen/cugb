# parser-combinators

A demonstration how simple and powerful parser combinators are.

## What is a parser?

Informal answer: Something that consumes a sequence of values and builds up some data structure.

A more formal, FP-ish answer:

Every function `p` conforming to the signature `[input] -> [result remaining-input]`

```
                    +-------------+
                    |             |--> result | :invalid
         input -->  |      p      |
                    |             |--> remaining-input
					+-------------+
```


## Let's add higher-order functions that produce parsers

* `pred->parser`: Create a parser from a predicate
* `alt`: Accept input if at least one of the given parsers accepts it
* `many*`: Accept input if the given parser repeatedly accepts values
* `many+`: Like `many*` but expects at least one value
* `optional`: Accept input with given parser, or return [nil input] if parser fails
* `sequence`: Apply given parsers in order, fail if not all parsers succeed
* `transform`: Apply function to result of given parser
* `descend`: Regard first value of input als sub-input and apply given parser to it
...

## What kind grammars can we encode with this?

* Basically everything that a recursive descent parser can recognize.
* Beware non-termination when combining `alt` with `optional`!
* If ambiguities exist in the grammar, it is hard to predict the results.
