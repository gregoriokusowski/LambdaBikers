(ns bikers.user
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [taoensso.carmine :as car :refer (wcar)]
            [buddy.hashers :as hashers]))

(def redis-config
  {:pool {}
   :spec {:db 3}})

(defmacro wcar* [& body] `(car/wcar redis-config ~@body))

(def blood-types
  ["O+" "O-" "A+" "A-" "B+" "B-" "AB+" "AB-"])

(defn- user-key [email]
  (str "u:" email))

(def signup-validations
  {:name v/required
   :email [v/required v/email]
   :password v/required})

(defn load-by-email [email]
  (let [user-data (wcar* (car/hgetall (user-key email)))
        user-hash (apply hash-map user-data)]
    (clojure.walk/keywordize-keys user-hash)))

(defn- save [user]
  (let [email (:email user)
        encrypted-password (hashers/encrypt (:password user))
        user (assoc user :password encrypted-password)]
    (wcar* (car/hmset* (user-key email) user)
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
  (b/validate user signup-validations))

