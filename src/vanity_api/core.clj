(ns vanity-api.core
  (:require [vanity-api.core.routing :as vanity-routing]
            [vanity-api.core.config :as vanity-config]
            [vanity-api.tracking.routing]
            [vertx.core :as vertx]
            [vertx.eventbus :as eb]
            [vertx.http :as http]
            [vertx.http.route :as route]
            [vertx.logging :as log]))


;; ------------------------------------------------------------------------------
;; Initialize HTTP server
;; ------------------------------------------------------------------------------

(defn- start-server []
  (-> (http/server)
    (http/on-request (vanity-routing/build-ns-routes 'vanity-api.tracking.routing))
    (http/listen 8080)))

;; ------------------------------------------------------------------------------
;; Initialize vert.x app
;; ------------------------------------------------------------------------------

(defn init
  []
  (vertx/deploy-module "io.vertx~mod-mysql-postgresql~0.3.0-SNAPSHOT"
                     :config (vanity-config/get-db-config)
                     :handler (fn [err deploy-id]
                                (when-not err
                                  (do
                                    (log/info "Starting API")
                                    (start-server)
                                    (log/info "API startup finished"))))))
