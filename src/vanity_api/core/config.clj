(ns vanity-api.core.config)


;; ------------------------------------------------------------------------------
;; Confif core functions
;; ------------------------------------------------------------------------------

(def ^:private config { :db-config { :address "channel.db" :host "localhost" :username "vanity" :password "vanity" :database  "vanity" }
                        :server-conf {:port 8080}})

(defn get-db-config
  []
  (config :db-config))
