(ns bikers.redis
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def redis-config
  {:pool {}
   :spec {:db 3}})

(defmacro wcar* [& body] `(car/wcar redis-config ~@body))
