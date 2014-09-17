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
  (assoc meta :handler fn))

;; ------------------------------------------------------------------------------

(defn get-all-handler-descriptors
  "Transforms function from specified namespace into handelr descriptors"
  [namespace]
  ;; we need to use inplementation of IParsistenList (to use pop)
  (reduce #(conj %1 (get-handler-descriptor %2 (get-fn-meta namespace %2))) '() (get-all-fn namespace)))

;; ------------------------------------------------------------------------------

(defn- build-request-wrapper
  "Wraps custom handlers in function providing request parameters"
  [content-type executor]
  (fn [req]
    (let [resp (-> (http/server-response req)
                   (http/add-header "Content-Type" content-type))]
      (executor (partial http/end resp)))))

;; ------------------------------------------------------------------------------

(defn- build-route
  "Builds one single vertx route matcher"
  [namepace handler-descriptor route]
  (let [executor (ns-resolve namepace (handler-descriptor :handler))
        method (handler-descriptor :method)
        url (handler-descriptor :url)
        content-type (handler-descriptor :content-type)]
    (do
      (log/info (str "Registering route " url))
      (if (nil? route)
        (method url (build-request-wrapper content-type executor))
        (method route url (build-request-wrapper content-type executor))))))

;; ------------------------------------------------------------------------------

(defn build-ns-routes
  "Builds for specific namespace chain of vertx route matchers"
  [namepace]
  (let [descriptors (get-all-handler-descriptors namepace)
        parent-route (build-route namepace (first descriptors) nil)]
    (reduce #(build-route namepace %2 %1) parent-route (pop descriptors))))



