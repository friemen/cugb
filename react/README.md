# ClojureScript with ReactJS: Om and Reagent

## Basics about React

[Official docs](http://facebook.github.io/react/index.html)

Core benefits:
* Much faster rendering
* Component oriented
* DOM as result of a pure transformation from application state to
  HTML

Core concepts
* Components
* Rendering to virtual DOM ("one-way databinding")
* State (mutable) vs Props (immutable)
* Lifecylce methods, for example
  * getInitialState
  * componentDidMount
  * shouldComponentUpdate
  * ...
* Mixins for implementing cross-cutting concerns



## Om

[GitHub](https://github.com/swannodette/om/)

### Get ready

To start hacking you can use the mies-om template:

```
lein new mies-om helloom
cd helloom
lein cljsbuild auto
```

Open in an editor helloom/src/helloom/core.cljs.
Whenever you save this file it will be recompiled.

Open helloom/index.html in a browser.
Refresh the browser to reload the latest compiled JavaScript.

### Om explained with a trivial example

Here's the result of a first modification of the default core.cljs:

```clojure
(def app-state (atom {:text "Hello Om World!"}))

(defn header
  "A header component."
  [state owner options]
  (reify
    om/IRender
    (render [_]
      (dom/h1 nil (:text state)))))


(om/root
  header
  app-state
  {:target (. js/document (getElementById "app"))})
```

Om largely maps the React API 1:1, but there are some important additional ideas:

Om introduces globally shared application state that is organized as
tree. To maintain locality, component functions receive a cursor into
this state which is transferred via React props. The cursor is passed
as `state` parameter via call of component function. Modifications to
the state that the cursor points to can be made via `om/transact!`
(akin to `swap!`) and `om/update!` (akin to `reset!`).


Om components can still have local mutable state (`IInitState`, `om/get-state`,
`om/set-state!`, `om/update-state!`, implementations of `IRenderState`).

In addition, the call to Om root can set immutable shared state that
is accessible by all components (`om/get-shared`).

[Om API overview](https://github.com/swannodette/om/wiki/Documentation)

## User Interaction

Let's modify the example to support user interaction:

```clojure
(def app-state (atom {:text "Hello Om World!"
                      :counter {:clicks 0}}))


(defn click-counter
  [state owner options]
  (om/component
   (dom/div nil
    (dom/input #js {:type "button"
                    :value "Hit me!"
                    :onClick #(om/transact! state :clicks inc)})
    "Hits " (:clicks state))))


(defn header
  "A header component."
  [state owner options]
  (reify
    om/IRender
    (render [_]
      (dom/h1 nil (:text state)))))


(defn page
  [state owner options]
  (om/component
   (dom/div nil
            (om/build header state)
            (om/build click-counter (:counter state)))))


(om/root
  page
  app-state
  {:target (. js/document (getElementById "app"))})
```

This example covers three aspects we didn't encounter so far:
* Instead of `reify` we use the `om/component` macro that helps when
  writing components that only need to implement `IRender`.
* We attach a callback to the button that uses `om/transact!`.
* To reuse an existing component `om/build` is used. We pass a cursor
  `(:counter state)` to click-counter that narrows the global app
  state to what click-counter should be able to act upon.


## Some Improvements

In the prior example we can also spot a few weaknesses:
* The button press is handled using a callback. While this looks harmless
  in this example the general approach is too low-level.
  * It mixes up presentation logic with rendering code.
  * Async server calls require another level of callbacks. The
    resulting code can become quite complicated.
  * Callbacks don't help with inter-component communication.
* It can happen all too easy to pass a wrong cursor to a component due
  to using ubiquitous data structures and no means for validation (or
  static type checking).
* The code based on the Om dom functions doesn't look so nice.

In fact, the rendering part of Om is seen by @swannodette as detail,
and it can be exchanged in Om by using libraries that allow for Hiccup
([sablono](http://github.com/r0man/sablono)) or Enlive
([kioo](http://github.com/ckirkendall/kioo)) style HTML generation.

### om-tools

Let's look at Prismatics
[om-tools](https://github.com/Prismatic/om-tools). They provide
these features:
* Validation of state
* Nicer dom functions
* Better destructuring of state
* Better support for mixins

We can apply the first two features to the example above. Just add the
dependency `[prismatic/om-tools "0.3.6"]` to the project.clj and
restart `lein cljsbuild auto`.

We have to update the default ns declaration:

```clojure
(ns helloom.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))
```

And here's the slighty changed example code, using Schema and om-tools dom functions.

```clojure
(def app-state (atom {:text "Hello Om World!"
                      :counter {:clicks 0}}))


(defcomponent ^:always-validate click-counter
  [state :- {:clicks js/Number} owner options]
  (render [_]
   (dom/div
    (dom/input {:type "button"
                :value "Hit me!"
                :on-click #(om/transact! state :clicks inc)})
    "Hits " (:clicks state))))


(defcomponent header
  "A header component."
  [state owner options]
  (render [_]
          (dom/h1 (:text state))))


(defcomponent page
  [state owner options]
  (render [_]
          (dom/div
           (om/build header state)
           (om/build click-counter (:counter state)))))


(om/root
  page
  app-state
  {:target (. js/document (getElementById "app"))})
```

### core.async

Now we're going to address the callback issue. For this we will use
core.async channels and a `go` block. The idea is that callbacks in
HTML will just put an event to the components channel where the
components `go` based process picks it up and processes it. It seems
as overkill in our example, but channels are a general enabler for a
very clear separation of rendering from any kind of presentation
logic. Furthermore having input channels for components makes a
perfect way for organization of inter-component communication.

We change the ns declaration like so:

```clojure
(ns helloom.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [cljs.core.async :refer [put! chan <!]]))
```

To create a light-weight go process we need a place according
to React/Oms lifecycle model (the `defcomponent` invocations can
happen many, many times.) To create the channel we use `init-state`
and we use `will-mount` to start the controller process.

We change only the click-counter component:

```clojure
(defcomponent ^:always-validate click-counter
  [state :- {:clicks js/Number} owner options]
  (init-state [_]
              {:ch (chan)})
  (will-mount [_]
              (let [ch (om/get-state owner :ch)]
                (go-loop []
                  (let [evt (<! ch)]
                    (om/transact! state :clicks inc))
                  (recur))))
  (render-state [_ {:keys [ch]}]
   (dom/div
    (dom/input {:type "button"
                :value "Hit me!"
                :on-click #(put! ch {:action :click})})
    "Hits " (:clicks state))))
```

Apparently we made the component much more complex, so what sounded compelling in
theory turns out to look awkward when actually implemented...

### Separation of Concerns

Perhaps the idea to give *every* component its own channel is wrong.
What happens if we create a reusable component that delegates rendering to a
function and takes a map for dispatching events to actions?

This component could look like this:

```clojure
;; ---------------------------------------------------------------------------
;; Generic component with a dispatching controller
;; Takes an actions map and a render function

(defcomponent view-with-controller
  [state owner {:keys [actions render-fn] :as options}]
  (init-state [_]
              {:ch (chan)})
  (will-mount [_]
              (let [ch (om/get-state owner :ch)]
                (go-loop []
                  (let [evt       (<! ch)
                        action-fn (get actions (:action evt))]
                    (if action-fn
                      (om/transact! state #(action-fn % evt))
                      (prn (str "WARNING: " (:action evt) " is unknown"))))
                  (recur))))
  (render-state [_ {:keys [ch]}]
   (render-fn state ch)))
```

The original click-counter component will need some refactoring. In
order to keep Schemas validation in our code base we have to directly
require it:

```clojure
(ns helloom.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [schema.macros :as sm])
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [cljs.core.async :refer [put! chan <!]]
            [schema.core :as s :include-macros true]))
```


And finally here's the refactored code of the rest of our example:

```clojure
(def Counter {:clicks js/Number})


(def app-state (atom {:text "Hello Om World!"
                      :counter {:clicks 0}}))


(sm/defn ^:always-validate render-click-counter
  [state :- Counter ch]
  (dom/div
    (dom/input {:type "button"
                :value "Hit me!"
                :on-click #(put! ch {:action :click})})
    "Hits " (:clicks state)))


(sm/defn ^:always-validate inc-clicks
  [state :- Counter evt]
  (update-in state [:clicks] inc))


(defcomponent header
  "A header component."
  [state owner options]
  (render [_]
          (dom/h1 (:text state))))


(defcomponent page
  [state owner options]
  (render [_]
          (dom/div
           (om/build header state)
           (om/build view-with-controller (:counter state)
                     {:opts {:actions {:click inc-clicks}
                             :render-fn render-click-counter}}))))

(om/root
  page
  app-state
  {:target (. js/document (getElementById "app"))})
```

Better. This approach cleanly separates actions from rendering, and it
can be extended to also support handling results of async server
calls.

What we didn't cover so far is inter-component communication...  I'm
currently exploring these and other problems in
[zackzack](https://github.com/friemen/zackzack).


## Reagent

[GitHub](https://github.com/reagent-project/reagent)

[Intro](http://holmsand.github.io/reagent/)

At a glance, Reagent seems to be more approachable than Om. The most striking differences are
* Hiccup style markup instead of React API.
* State management is more based on local (possibly hidden) atoms than global state.
* The direct implementation of lifecycle methods is possible, but seldom needed.

Reagent promotes inter-component communication via shared mutable state.

To start you can create your own helloreagent project and take this
[project.clj contents](https://github.com/friemen/cugb/blob/master/react/helloreagent/project.clj).

Here's an example quite similar to what we started with in Om.

```clojure
(ns helloreagent.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def app-state (r/atom {:text "Hello Reagent World!"}))

(defn header
  []
  [:h1 (:text @app-state)])

(r/render-component
 [header]
 (. js/document (getElementById "app")))
```

The state is kept inside an Reagent atom, which tracks where it was
deref'd to trigger re-rendering of exactly those components that
depend on it. This feature is already a strong indication to
NOT use only one(!) atom to keep global state, as any change would cause
re-rendering of all mounted components.

Since a few days there exists a library bringing
[cursors to Reagent](https://github.com/reagent-project/reagent-cursor)
but I didn't try it yet.

At the moment Reagent promotes to keep state more component-local. Adapting
the example to follow that rule yields

```clojure
(defn header
  []
  (let [state (r/atom {:text "Hello Reagent World!"})]
    (fn []
      [:h1 (:text @state)])))
```

and the `app-state` var is gone. As can be seen, a Reagent component
function is allowed to return a closure instead of Hiccup-style
data. It can take the same parameters as the enclosing function. The
closure is then used for rendering.

### Again: User Interaction

Let's create the click-counter.

```clojure
(defn click-counter
  []
  (let [clicks (r/atom 0)]
    (fn []
      [:div
       [:input {:type "button"
                :value "Hit me!"
                :on-click #(swap! clicks inc)}]
       [:span "Hits " @clicks]])))


(defn header
  []
  (let [state (r/atom {:text "Hello Reagent World!"})]
    (fn []
      [:h1 (:text @state)])))


(defn page
  []
  [:div
   [header]
   [click-counter]])


(r/render-component
 [page]
 (. js/document (getElementById "app")))
```

We can see that usage of existing components happens by just wrapping
the function names in a vector. This vector also allows for an
arbitrary number of parameters which can then be declared within the
component function.


### Again: core.async

Let's see what happens when we bring channels into the game.

```clojure
(ns helloreagent.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [put! chan <!]]))
```

We have to change the click-counter component to start a process
before it returns the rendering closure.

```clojure
(defn click-counter
  []
  (let [clicks (r/atom 0)
        ch     (chan)]
    (go-loop []
      (let [evt (<! ch)]
        (swap! clicks inc))
      (recur))
    (fn []
      [:div
       [:input {:type "button"
                :value "Hit me!"
                :on-click #(put! ch {:action :click})}]
       [:span "Hits " @clicks]])))
```

Looks good so far, but unfortunately it's hard to tell where in Reacts
life-cycle the channel is created. (Remember: in Om we put channel
creation to `init-state` and the process start into
`will-mount`.) Reagent allows us to use `r/create-class` to specify a
map with functions that correspond to the React life-cycle.

Rewriting click-counter for more fine-grained control yields:

```clojure
(defn click-counter
  []
  (r/create-class
   {:get-initial-state
    (fn [_]
      {:ch (chan)
       :clicks (r/atom 0)})
    :component-will-mount
    (fn [this]
      (let [{:keys [ch clicks]} (r/state this)]
        (go-loop []
          (let [evt (<! ch)]
            (swap! clicks inc))
          (recur))))
    :render
    (fn [this]
      (let [{:keys [ch clicks]} (r/state this)]
        [:div
         [:input {:type "button"
                  :value "Hit me!"
                  :on-click #(put! ch {:action :click})}]
         [:span "Hits " @clicks]]))}))
```

Ok, this starts again to become awkward.


### Again: Separation of Concerns

To separate the technical plumbing from the presentation logic we can
apply the same idea as before:

```clojure
;; ---------------------------------------------------------------------------
;; Generic component with a dispatching controller

(defn view-with-controller
  [initial-state-fn actions render-fn]
  (r/create-class
   {:get-initial-state
    (fn [_]
      {:ch (chan)
       :state (r/atom (initial-state-fn))})
    :component-will-mount
    (fn [this]
      (let [{:keys [state ch]} (r/state this)]
        (go-loop []
          (let [evt (<! ch)
                action-fn (get actions (:action evt))]
            (if action-fn
              (swap! state action-fn evt)
              (prn (str "WARNING: " (:action evt) " is unknown"))))
          (recur))))
    :render
    (fn [this]
      (let [{:keys [state ch]} (r/state this)]
        (render-fn @state ch)))}))
```

What remains is again easy to grasp:

```clojure
;; ---------------------------------------------------------------------------
;; App specific code

(defn render-click-counter
  [state ch]
  [:div
   [:input {:type "button"
            :value "Hit me!"
            :on-click #(put! ch {:action :click})}]
   [:span "Hits " (:clicks state)]])


(defn inc-clicks
  [state evt]
  (update-in state [:clicks] inc))


(defn header
  []
  (let [state (r/atom {:text "Hello Reagent World!"})]
    (fn []
      [:h1 (:text @state)])))


(defn page
  []
  [:div
   [header]
   [view-with-controller (constantly {:clicks 0})
    {:click inc-clicks}
    render-click-counter]])


(r/render-component
 [page]
 (. js/document (getElementById "app")))
```


## Wrap up

* I see as most substantial difference between Reagent and Om the way
  state is handled.
* Using channels makes the whole setup more complex and may pay off
  only when inter-component or async remote communication happens
  often. The rules to find a suitable "topology" of channels to enable
  inter-component communication are not clear to me.
* It's possible to centralize technical plumbing code to get a clearer
  separation between rendering, presentation logic and access to technical APIs.


## References

* [Why React is awesome](http://jlongster.com/Removing-User-Interface-Complexity,-or-Why-React-is-Awesome)
* [Local state is harmful](http://scattered-thoughts.net/blog/2014/02/17/local-state-is-harmful/)
* [Quiescent](https://github.com/levand/quiescent) - Another ClojureScript lib for React
* [Reacl](https://github.com/active-group/reacl) - Another ClojureScript lib for React
* [Zackzack](https://github.com/friemen/zackzack) - Exploring Om for creating Enterprise style UIs
