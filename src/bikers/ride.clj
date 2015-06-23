(ns bikers.ride
  (:require [bikers.redis :as redis :refer (wcar*)]
            [taoensso.carmine :as car]))

(defn- load-and-normalize [key]
  (let [values (redis/wcar* (car/hgetall key))
        hash (apply hash-map values)
        ride (clojure.walk/keywordize-keys hash)
        participants-key (str "participants:" key)
        participants (redis/wcar* (car/smembers participants-key))]
    (assoc ride :participants participants)))

(defn all []
  ;; (let [[_ ride-keys] (redis/wcar* (car/scan 0 "MATCH" "rides:*"))] works only on Redis 2.8+
  (let [ride-keys (redis/wcar* (car/keys "rides:*"))]
    (map load-and-normalize ride-keys)))

(defn create [params]
  (let [id (redis/wcar* (car/incr "ride-id"))
        ride-params (assoc params :id id)
        ride-key (str "rides:" id)]
    (redis/wcar* (car/hmset* ride-key ride-params))))

(defn apply-to [user-email ride-id]
  (redis/wcar* (car/sadd (str "participants:rides:" ride-id) user-email)))
