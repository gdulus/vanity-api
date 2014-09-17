(ns vanity-api.core.routing
  (:require [vertx.http :as http]
            [vertx.logging :as log]))

;; ------------------------------------------------------------------------------
;; Routring core functions
;; ------------------------------------------------------------------------------

(defn- get-all-fn
  "Retruns all functions from specified namespace"
  [namespace]
  (keys (ns-publics namespace)))

;; ------------------------------------------------------------------------------

(defn- get-fn-meta
  "Retruns function metadata"
  [namespace fn]
  (meta (ns-resolve namespace fn)))

;; ------------------------------------------------------------------------------

(defn- get-handler-descriptor
  "Transforms raw data into handler descriptor"
  [fn meta]
  {:url (:url meta) :method (:method meta) :handler fn})

;; ------------------------------------------------------------------------------

(defn get-all-handler-descriptors
  "Transforms function from specified namespace into handelr descriptors"
  [namespace]
  ;; we need to use inplementation of IParsistenList (to use pop)
  (reduce #(conj %1 (get-handler-descriptor %2 (get-fn-meta namespace %2))) '() (get-all-fn namespace)))

;; ------------------------------------------------------------------------------

(defn- build-request-wrapper
  "Wraps custom handlers in function providing request parameters"
  [executor]
  (fn [req]
    (-> (http/server-response req)
        (http/end (executor (http/params req))))))

;; ------------------------------------------------------------------------------

(defn- build-route
  "Builds one single vertx route matcher"
  [namepace handler-descriptor route]
  (let [executor (ns-resolve namepace (handler-descriptor :handler))
        method (handler-descriptor :method)
        url (handler-descriptor :url)]
    (do
      (log/info (str "Registering route " url))
      (if (nil? route)
      (method url (build-request-wrapper executor))
      (method route url (build-request-wrapper executor))))))

;; ------------------------------------------------------------------------------

(defn build-ns-routes
  "Builds for specific namespace chain of vertx route matchers"
  [namepace]
  (let [descriptors (get-all-handler-descriptors namepace)
        parent-route (build-route namepace (first descriptors) nil)]
    (reduce #(build-route namepace %2 %1) parent-route (pop descriptors))))



