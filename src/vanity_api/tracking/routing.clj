(ns vanity-api.tracking.routing
  (:require [vertx.http.route :as route]))

(defn ^{ :method route/get :url "/pixel/vip/:id" }
  pixel-vip
  [params]
  (str "hello from pixel vip " (params :id)))

(defn ^{ :method route/get :url "/pixel/article/:id" }
  pixel-article
  [params]
  (str "hello from pixel article " (params :id)))

