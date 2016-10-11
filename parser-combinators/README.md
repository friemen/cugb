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


## How does an example application look like?

```clojure
;; define some parsers

(def paragraph-parser
  (pc/pred->parser #{:p}))

(def section-parser
  (pc/transform
           into-first
           (pc/sequence (pc/pred->parser #{:h1})
                        (pc/many* paragraph-parser))))

(def tr-parser
  (pc/transform into-first
                (pc/sequence
                 (pc/pred->parser #{:tr})
                 (pc/many+ (pc/pred->parser #{:td})))))

(def table-parser
  (pc/transform into-first
                (pc/sequence
                 (pc/pred->parser #{:table})
                 (pc/many+ (pc/descend tr-parser)))))

(def content-parser
  (pc/many+ (pc/alt section-parser paragraph-parser (pc/descend table-parser))))

;; invoke one an input sequence

(content-parser [:p :p :h1 :p [:table [:tr :td :td] [:tr :td]]])
;= [[:p :p [:h1 :p] [:table [:tr :td :td] [:tr :td]]]
    nil]
```

## What kind of grammars can we encode with this?

* Basically everything that a
  [recursive descent parser](https://en.wikipedia.org/wiki/Recursive_descent_parser)
  can recognize. AFAIK it's an [LL(1) parser](https://en.wikipedia.org/wiki/LL_parser).
* Beware non-termination when combining `alt` with `optional`!
* If ambiguities exist in the grammar it is hard to predict the results.
