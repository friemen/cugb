# Protocols and Records

## The expression problem

For a very readable introduction see this
[post](http://www.ibm.com/developerworks/library/j-clojure-protocols/) from Stuart Sierra.

Basic questions: without(!) changing/recompiling existing code
* how can we add new types to existing functionality?
* how can we add new functionality to existing types?

Plain OO answer for new types is easy: add subclass which inherits existing methods.
But how do we add new functions to existing types? This gives birth to the
[decorator pattern](http://en.wikipedia.org/wiki/Decorator_pattern). This requires us
to have control over instance creation.

Plain FP answer for new functions is easy: just add them. In order to add new types
Scala and Haskell offer Type Classes, a
[less powerful construct](http://debasishg.blogspot.de/2010/08/random-thoughts-on-clojure-protocols.html)
(with regard to dispatching) are Clojure Protocols.
Todo something similar in OO the [Adapter pattern](http://en.wikipedia.org/wiki/Adapter_pattern)
is used. Again, this pattern is only helpful if we can influence how instances are created.


## Protocols

A first example

```clojure
(defprotocol IHello
  (hello [x]))
;= IHello
(extend-type java.lang.String
  IHello
  (hello [s] (str "Hello " s)))
;= nil
(hello "Foo")
;= "Hello Foo"
```

The function symbol ```hello``` belongs to the namespace of where the protocol is defined.
The name of the protocol is secondary, it will usually not appear in most of the code that
refers the functions declared within the protocol.

We can extend numerous existing types individually with a protocol.
But if it suits our need better we can also extend a protocol to multiple types at once:

```clojure
(extend-protocol IHello
  java.lang.String
  (hello [s] (str "Hello " s))
  java.lang.Number
  (hello [n] (str "Hello Number" n)))
;= nil
```

For utmost flexibility use `extend` on a type. It allows you to attach maps of functions to protocols,
which gives you complete freedom regarding implementation reuse
(OO provides only inheritance and delegation).

Use `satisfies?` to find out if an instance supports a protocol. `instance?` will not work.

Use `extend?` to find out if a type directly supports a protocol.

User ```extenders``` on a protocol to get a sequence of supporting types.
```clojure
(extenders IHello)
;= (java.lang.Number java.lang.String)
```

## Records

In order to make use of protocols you need to have types. Types either stem from Java, or
you can create your own types using `defrecord`.

**Be cautious**: using defrecord too early will hinder your work in the REPL. Whenever the
defrecord expr is reevaluated a new class is created underneath. This can lead to
incompatibilities with existing protocols or instances of the record. In consequence you
will face odd runtime errors.

Fortunately records behave almost everywhere like maps. If possible start with maps.
You should encapsulate the creation of instances in factory functions anyway.

This here:
```clojure
(defrecord Person [firstname lastname])
```
gives you a class, that supports Map-like access. Instances are immutable.

```clojure
(def p (Person. "Mickey" "Mouse"))
;= #'user/p
(:firstname p)
;= "Mickey"
(vals p)
;= ("Mickey" "Mouse")
(assoc p :firstname "Mini")
;= #user.Person{:firstname "Mini", :lastname "Mouse"}
(assoc p :city "Duckberg")
;= #user.Person{:firstname "Mickey", :lastname "Mouse", :city "Duckberg"}
```

But, if you remove record defined keys from an instance you'll receive a map.
```clojure
(instance? Person p)
;= true
(dissoc p :lastname)
;= {:firstname "Mickey"}
(instance? Person (dissoc p :lastname))
;= false
```

Example with protocol extension:
```clojure
(defrecord Person [firstname lastname]
  IHello
  (hello [p] (str "Hello " firstname " " lastname)))
;= user.Person
(hello (Person. "Donald" "Duck"))
;= "Hello Donald Duck"
```

To use Clojure records to describe domain data you could choose an
approach like [domaintypes](https://github.com/friemen/domaintypes) did.

