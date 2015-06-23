(ns bikers.profile
  (:require [bikers.redis :as redis :refer (wcar*)]
            [taoensso.carmine :as car]
            [bikers.user :as user]))

(defn update [user-key profile-params]
  (redis/wcar* (car/hmset* user-key profile-params)))

