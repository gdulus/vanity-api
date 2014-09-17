(ns vanity-api.tracking.routing
  (:require [vertx.http.route :as route]
            [vertx.eventbus :as eb]
            [vertx.http :as http]
            [vanity-api.core.db :as db]))

(defn
  ^{ :method route/get :url "/tracking/vip/:id" :content-type "application/json" }
  pixel-vip
  [out]
  (db/query "select count(*) from article" out))


;;(defn
;;  ^{ :method route/get :url "/tracking/article/:id" :content-type "application/json" }
;;  pixel-article
;;  [params]
;;  (str "hello from pixel article " (params :id)))
