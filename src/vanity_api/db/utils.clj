(ns vanity-api.db.utils)

(defn today
  []
   (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") (new java.util.Date)))

(defn now
  []
   (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm") (new java.util.Date)))
