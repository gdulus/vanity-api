(ns vanity-api.tracking.routing
  (:require [vertx.http.route :as route]
            [vertx.eventbus :as eb]
            [vertx.http :as http]
            [vanity-api.core.db :as db]))

(defn
  ^{ :method route/get :url "/articles/count" :content-type "application/json" }
  count-articles
  [out params]
  (db/query "select count(*) from article" out))

