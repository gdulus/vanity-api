(ns vanity-api.core
  (:require [vanity-api.core.routing :as vanity-routing]
            [vanity-api.core.config :as vanity-config]
            [vertx.core :as vertx]
            [vertx.http :as http]
            [vertx.logging :as log]
            [vanity-api.tracking.routing]
            [vanity-api.auth.routing]))


;; ------------------------------------------------------------------------------
;; Initialize HTTP server
;; ------------------------------------------------------------------------------

(defn- start-server []
  (-> (http/server)
    (http/on-request (vanity-routing/build-routes ['vanity-api.auth.routing
                                                   'vanity-api.tracking.routing]))
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
