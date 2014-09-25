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
  [fn namespace]
  (assoc (get-fn-meta namespace fn) :handler fn :ns namespace))

;; ------------------------------------------------------------------------------

(defn- get-all-handler-descriptors
  "Transforms function from specified namespace into handelr descriptors"
  [namespace]
  (reduce #(conj %1 (get-handler-descriptor %2 namespace)) '() (get-all-fn namespace)))

;; ------------------------------------------------------------------------------

(defn- build-request-wrapper
  "Wraps custom handlers in function providing request parameters"
  [content-type executor]
  (fn [req]
    (let [resp (-> (http/server-response req)
                   (http/add-header "Content-Type" content-type))]
      (executor (partial http/end resp) (http/params req)))))

;; ------------------------------------------------------------------------------

(defn- build-route
  "Builds one single vertx route matcher"
  [handler-descriptor route]
  (let [namespace (handler-descriptor :ns)
        executor (ns-resolve namespace (handler-descriptor :handler))
        method (handler-descriptor :method)
        url (handler-descriptor :url)
        content-type (handler-descriptor :content-type)]
    (do
      (log/info (str "Registering route " url))
      (if (nil? route)
        (method url (build-request-wrapper content-type executor))
        (method route url (build-request-wrapper content-type executor))))))

;; ------------------------------------------------------------------------------

(defn build-routes
  "Builds for specific namespace chain of vertx route matchers"
  [namespaces]
  (let [descriptors (vec (reduce #(concat %1 (get-all-handler-descriptors %2)) '() namespaces))
        parent-descriptor (last descriptors)
        parent-route (build-route parent-descriptor nil)]
    (reduce #(build-route %2 %1) parent-route (pop descriptors))))
