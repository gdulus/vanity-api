(ns vanity-api.db.core
  (:require [vertx.eventbus :as eb]
            [vertx.logging :as log]))

;; ------------------------------------------------------------------------------

(defn- get-results
  [response]
  (do
    (log/debug (str "Response " response))
    (:results response)))

;; ------------------------------------------------------------------------------

(defn select
  ([statement callback]
   (eb/send "channel.db" {:action "prepared" :statement statement :values []} #(callback (get-results %))))
  ([statement values callback]
   (eb/send "channel.db" {:action "prepared" :statement statement :values values} #(callback (get-results %)))))

;; ------------------------------------------------------------------------------

(defn insert
  [table data callback]
  (let [fields (keys data)
        values (vals data)]
      (log/debug (str "insert into " table " values " data))
      (eb/send "channel.db" {:action "insert" :table table :fields fields :values [values]} #(callback (get-results %)))))

;; ------------------------------------------------------------------------------

(defn insert-nextval
  [table data callback]
  (select "select nextval('hibernate_sequence')" #(insert table (assoc data :id (-> % first first)) callback)))
