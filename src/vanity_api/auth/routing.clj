(ns vanity-api.auth.routing
  (:require [vertx.http.route :as route]
            [vanity-api.db.core :as db-core]
            [vanity-api.db.utils :as db-utils]))

;; ------------------------------------------------------------------------------

(defn
  ^{ :method route/get :url "/auth/facebook/:token" :content-type "image/json" }
  auth-facebook
  [out params]
  (out {:status "ok"}))
