(ns cljwd.core
  (:require [clj-webdriver.taxi :refer :all]))


;; doesn't work, due to https://github.com/semperos/clj-webdriver/issues/137

(set-driver! {:browser :firefox} "http://www.falkoriemenschneider.de/zackzack/index.html")
