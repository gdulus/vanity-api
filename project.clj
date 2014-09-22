(defproject vanity-api "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-time "0.8.0"]]
  :plugins [[lein-vertx "0.3.0-SNAPSHOT"]]
  :vertx {:main vanity-api.core/init})
