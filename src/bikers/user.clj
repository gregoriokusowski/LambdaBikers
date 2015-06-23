(ns bikers.user
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [buddy.hashers :as hashers]
            [bikers.redis :as redis :refer (wcar*)]
            [taoensso.carmine :as car]))

(def blood-types
  ["O+" "O-" "A+" "A-" "B+" "B-" "AB+" "AB-"])

(defn user-key [email]
  (str "u:" email))

(defn load-by-email [email]
  (let [user-data (redis/wcar* (car/hgetall (user-key email)))
        user-hash (apply hash-map user-data)]
    (clojure.walk/keywordize-keys user-hash)))

(defn available? [email]
  (= (redis/wcar* (car/sismember "u:all" email)) 0))

(def signup-validations
  {:name v/required
   :email [v/required v/email [available? :message "e-mail in use"]]
   :password v/required})

(defn- save [user]
  (let [email (:email user)
        encrypted-password (hashers/encrypt (:password user))
        user (assoc user :password encrypted-password)]
    (redis/wcar* (car/hmset* (user-key email) user)
           (car/sadd "u:all" email))))

(defn build [req]
  (let [params (:params req)]
    (select-keys params [:email :name :password])))

(defn login [user]
  (let [loaded-user (load-by-email (:email user))]
    (hashers/check (:password user) (:password loaded-user))))

(defn sign-up [user]
  (if (b/valid? user signup-validations)
    (save user)))

(defn sign-up-errors [user]
  (let [errors (first (b/validate user signup-validations))]
    (map first (vals errors))))

