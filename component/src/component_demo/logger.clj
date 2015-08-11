(ns component-demo.logger)



(defprotocol ILogger
  (log [this message]))
