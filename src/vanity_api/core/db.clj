(ns vanity-api.core.db
  (:require [vertx.eventbus :as eb]))

(defn- prepare-response
  [response]
  (str(:results response)))

(defn query
  ([statement callback]
   (eb/send "channel.db" {:action "prepared" :statement statement :values []} #(callback (prepare-response %))))
  ([statement values callback]
   (eb/send "channel.db" {:action "prepared" :statement statement :values values} #(callback (prepare-response %)))))
