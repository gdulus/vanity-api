(ns vanity-api.tracking.routing
  (:require [vertx.http.route :as route]
            [vanity-api.db.core :as db-core]
            [vanity-api.db.utils :as db-utils]))

;; ------------------------------------------------------------------------------

(defn
  ^{ :method route/get :url "/tracking/article/:id" :content-type "image/gif" }
  tracking-article
  [out params]
  (db-core/insert-nextval "popularity"
                          {:day (db-utils/today) :rank 1 :class "vanity.stats.ArticlePopularity" :article_id (params :id)}
                          (fn[result](out ""))))

;; ------------------------------------------------------------------------------

(defn
  ^{ :method route/get :url "/tracking/tag/:id" :content-type "image/gif" }
  tracking-tag
  [out params]
  (db-core/insert-nextval "popularity"
                          {:day (db-utils/today) :rank 1 :class "vanity.stats.TagPopularity" :tag_id (params :id)}
                          (fn[result](out ""))))
