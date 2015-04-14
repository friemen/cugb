# Selenium Webdriver with Clojure

## Starting from scratch

Include an additional dependency in your `project.clj`:

```clojure
[org.seleniumhq.selenium/selenium-java "2.45.0" :scope "test"]
```

Create a namespace that will contain your utility functions, for example

```clojure
(ns zackzack.webdriver
  (:import [org.openqa.selenium By WebDriver WebElement]
           [org.openqa.selenium.firefox FirefoxDriver]))
```

(Alternatively you can use ChromeDriver, SafariDriver or any other supported browser.)

Evaluate the ns form.
In the REPL define a var to hold your driver instance, this should immediately open a browser window:

```clojure
(def d (FirefoxDriver.))
```

To close the browser use `(.close d)`.

Use the following resources to find out about the Webdriver API.

* [Examples how to use Java Webdriver API](http://www.seleniumhq.org/docs/03_webdriver.jsp#selenium-webdriver-api-commands-and-operations)
* [Webdriver Javadoc](http://selenium.googlecode.com/git/docs/api/java/index.html?overview-summary.html)

In the REPL try with an open browser:

```clojure
(.get d "http://www.falkoriemenschneider.de/zackzack/index.html")
; nil

(.findElement d (By/id "playground"))
; #<RemoteWebElement [[FirefoxDriver: firefox on LINUX (a07da5fd-8391-4b63-919a-87793fd33cb8)]
;                     -> id: playground]>

(.click (.findElement d (By/id "playground")))
; nil
```

That's it! You're ready to control your browser remotely from a Clojure REPL.


## Creating utility functions and using them in your tests

You could now start to create some utility functions like the following:

```clojure
(def ^:dynamic *driver*)

(defn firefox-driver
  []
  (FirefoxDriver.))


(defn get!
  [url]
  (.get *driver* url))

(defn close!
  []
  (.close *driver*))

(defn element-by-id
  [id]
  (.findElement *driver* (By/id id)))


(defn click!
  [id]
  (.click (element-by-id id)))

(defn send-keys!
  [id text]
  (let [chsa (into-array java.lang.CharSequence [text])]
    (.sendKeys (element-by-id id) chsa)))


(defn clear-value!
  [id]
  (.clear (element-by-id id)))


(defn set-value!
  [id new-text]
  (clear-value! id)
  (send-keys! id new-text))


(defn attribute
  [id attr-key]
  (.getAttribute (element-by-id id) (name attr-key)))


(defn value
  [id]
  (attribute id :value))


(defn enabled?
  [id]
  (.isEnabled (element-by-id id)))
```


Then use them in a test namespace:

```clojure
(ns zackzack.demo.addressbook-test
  (:require [clojure.test :refer [deftest testing is are use-fixtures run-tests]]
            [zackzack.webdriver :as browser :refer [click! get! enabled? set-value! close!]]
            [zackzack.backend :as server]))


(defn setup
  [f]
  (server/start!)
  (Thread/sleep 100)
  (binding [browser/*driver* (browser/firefox-driver)]
    (f)
    (close!))
  (server/stop!))


(use-fixtures :once setup)


(deftest addressbook-test
  (get! "http://localhost:8080/index.html")
  (click! "addressbook")
  (click! "private")
  (is (not (enabled? "add")) "add button is disabled")
  (set-value! "company" "doctronic")
  (click! "add"))
```

## clj-webdriver

You don't need to create your own wrapper around Selenium Webdriver,
there exists a library called [clj-webdriver](https://github.com/semperos/clj-webdriver).

It provides:

* High-level API called *Taxi*
* Core API with functions for
  * Browser handling and basic driver actions (`get-url`, `back`, `forward`, `refresh`)
  * Finding elements
  * Waiting
  * Window handling
  * Cookie handling


(Caveat: by time of this writing, April 2015, the 0.6.1 version
doesn't work with the latest Firefox release, see
https://github.com/semperos/clj-webdriver/issues/137)
