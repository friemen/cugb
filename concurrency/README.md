# Clojure Concurrency


## Concurrency vs. Parallelism

Concurrency and parallelism are distinct concepts. Concurrency is concerned with managing access to shared state from different threads that cooperate, whereas parallelism is concerned with utilizing multiple processors/cores to improve the performance of a computation.


## Concurrency classified by type of communication

* Shared state communication
  * Bare locks and synchronization (Javas default)
  * In Clojure: 4 ref-types with defined concurrency semantics (including Software Transactional Memory)
* Message passing communication
  * Actor style (supported by [pulsar](https://github.com/puniverse/pulsar) on top of [Quasar](https://github.com/puniverse/quasar))
  * CSP style (supported by [core.async](https://github.com/clojure/core.async))

## Delays, Futures and Promises

### Delay

Postpones the execution of a code block until the delay is dereferenced.

```clojure
(def d (delay (do (Thread/sleep 3000) 42)))
;= #'user/d
(realized? d)
;= false
d
;= #<Delay@2f223554: :pending>
@d
;; takes 3 seconds
;= 42
```

### Future

Starts computation in a different thread. Dereferencing blocks if value is not yet available.

```clojure
(defn compute-hard-stuff []
	(println "Starting...")
	(Thread/sleep 1000)
	(println "Still working...")
	(Thread/sleep 1000)
	(println "Done")
	42)
;= #'user/compute-hard-stuff
(def f (future (compute-hard-stuff)))
;= #'user/f
; "Starting..."
; "Still working..."
; "Done"
(realized? f)
;= true
@f
;= 42
```

### Promise

One-time slot for some value to be delivered in the future. Dereferencing blocks until value is delivered.

```clojure
(def p (promise))
;= #'user/p
(future (do (Thread/sleep 5000) (deliver p 42)))
;= #<core$future_call$reify__6267@6ed70792: :pending>
@p
;; this takes a few seconds before value is printed
;= 42
```

## The Clojure approach to concurrency

Recommended reading: [Values and Change](http://clojure.org/state)

Separate _state_ from _identity_ to accurately reflect how humans and machines perceive the world.
State at a certain point in time is represented by immutable data.
Identity is represented by one of the 4 ref-types (Var, Atom, Ref, Agent).

## Common functionality on ref-types

The ref-types Var, Atom, Ref and Agent hold a value and are updated by functions
```clojure
(<changer> reference f & args)
```

The "changers" are
* Var: alter-var-root 
* Atom: swap!
* Ref: alter, commute
* Agent: send, send-off

f is a function that takes at least the current value of the reference as argument.
For Atoms and Refs f is retried in case of any conflicts.
Therefore f must be free of side-effects.


The value from the reference can be obtained by `@reference` or `(deref reference)`.


### Watches

`(add-watch ref key f)` registers a listener function with ref. This works with all 4 types.
The listener function takes four arguments:
* The _key_ the listener was registered with,
* the _ref_ itself,
* the _old value_, and
* the _new value_.


```clojure
(def v "A var")
;= #'user/v
(add-watch #'v :notify (fn [k r o n]
			     (println (str r " changed '" o "' ->' " n "'"))))
;= #'user/v
(def v 42)
; "#'user/v changed '42' ->' 42'"
(def v 42)
; "#'user/v changed '42' ->' 42'"
(alter-var-root #'v (constantly 4711))
; "#'user/v changed '42' ->' 4711'"
;= 4711
```

`(remove-watch ref key)` removes the listener function that key points to from the reference.


### Validators

Attach one validator function to ensure on a low level that the value of a reference complies to constraints.

```clojure
(def a (atom "foo"))
;= #'user/a
(set-validator! a (fn [x] (not= x "")))
;= nil
(reset! a "")
;= IllegalStateException Invalid reference state  clojure.lang.ARef.validate (ARef.java:33)
@a
;= "foo"
(reset! a "bar")
;= "bar"
@a
;= "bar"
```

For an Atom, Ref or Agent the validator function can be passed upon creation, for example

```clojure
(def a (atom "Foo" :validator #(not= % "")))
```


## Ref types in more detail

### Var

Thread-bound global references to either immutable data, other ref-types or functions.

`(def symbol expr)` creates a new var in the current namespace.

`(var symbol)` returns the var for the given symbol.

`(var-get var)` returns the value of the given var.


```
(def v "some value")
;= #'user/v
v
;= "some value"
(var v)
;= #'user/v
(var-get #'v)
;= "some value"
``` 

Vars support _dynamic scope_ and thread-local rebinding of values.

`(alter-var-root v f & args)` changes the root binding of a var.

To make clear that a var can be rebound use ^:dynamic and 'earmuffs' in the declaration, for example:

```clojure
(def ^:dynamic *http-status* 0)
```
`(binding bindings & body)` creates new thread-local bindings for existing vars.

`(set! v value)` sets the vars value thread-locally. The var must be dynamic and set! must be used within a binding expression.



### Atom

Supports **synchronous**, **uncoordinated** changes per atom.

`(reset! a new-value)` sets a new value into the atom a.

`(swap! a f & args)` applies f to the value of atom a and optional args and sets the result as new value.

`(compare-and-set! a old-value new-value)` sets a to new-value if a contains old-value. Returns true if successful.


### Ref

Supports **synchronous**, **coordinated** changes on a set of refs through Software Transactional Memory (STM).

`(dosync body)` the code of the body accesses refs in a transaction.
Derefs outside of dosync are not coordinated and will lead to inconsistent results.

`(ref-set r value)` sets the value in r.

`(alter r f & args)` applies f to the value of r and the optional args and stores the result in r.

`(commute r f & args)` similar to alter, but assumes that order of concurrent transactions isn't important (last wins). Beware, this optimization is suitable only in certain situations.


### Agent

Supports **asynchronous**, **uncoordinated** changes per agent.
Each agent has a queue of action functions and a current value. Actions are processed sequentially in a different thread. There is no fixed assignment between agents and threads.
To schedule an action use either `send` or `send-off`.

`(send a f & args)` adds a function to the queue, assuming that it's execution time is only CPU-bound. Send uses a fixed size thread pool.

`(send-off a f & args)` adds a function to the queue, assuming that it may wait for IO or other blocking resources. Send-off uses a thread from an unbounded thread pool (the same as `future` uses).

`(send-via executor a f & args)` adds a function to the queue. The function is executed by the executor.

Calculations for agents that are sent during a STM transaction will start only when transactions succeeds. Agents are therefore ideal for transaction bound side-effects.

An exception thrown in a action function stops the agent from processing further actions. It is then in a failed state. It's value is the result of the last successful action function execution.

`(agent-error a)` returns the exception that was last recently thrown.

`(set-error-handler a f)` registers a function with two arguments (agent and exception) that is called upon catching an exception. Nonetheless a restart is necessary.

`(restart-agent a new-value & options)` sets agent to an unfailed state.

```clojure
(def a (agent 10))
;= #'user/a
(set-validator! a pos?)
;= nil
(send a (constantly -5))
;= #<Agent@1ac3e29: 10>
a
;= #<Agent@1ac3e29 FAILED: 10>
(def a (agent 10))
;= #'user/a
(def a (agent 10 :validator pos?))
;= #'user/a
@a
;= 10
(send a (constantly -5))
;= #<Agent@1995b74d FAILED: 10>
(agent-error a)
;= #<IllegalStateException java.lang.IllegalStateException: Invalid reference state>
(send a (constantly 1))
;= IllegalStateException Invalid reference state  clojure.lang.ARef.validate (ARef.java:33)
(restart-agent a 1)
;= 1
(send a (constantly 5))
;= #<Agent@1995b74d: 5>
@a
;= 5
```
