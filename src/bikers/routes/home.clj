(ns bikers.routes.home
  (:require [bikers.layout :as layout]
            [bikers.user :as user]
            [bikers.profile :as profile]
            [bikers.ride :as ride]
            [compojure.core :refer [defroutes GET POST PUT]]
            [clojure.java.io :as io]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated?]]))

(defn- setup-session-and-redirect-to-home [user req]
  (let [session (:session req)
        email (:email user)
        updated-session (assoc session :identity email)]
    (-> (redirect "/")
        (assoc :session updated-session))))

(defn user-from-request [req]
  (-> req :session :identity user/load-by-email))

(defn home-page [req]
  (if (authenticated? req)
    (layout/render "home.html" {:user (user-from-request req)})
    (layout/render "public.html")))

(defn about-page []
  (layout/render "about.html"))

(defn sign-up [req]
  (let [new-user (user/build req)]
    (if (user/sign-up new-user)
      (setup-session-and-redirect-to-home new-user req)
      (layout/render "public.html"
                     {:user new-user
                      :sign-up-errors (user/sign-up-errors new-user)}))))

(defn login [req]
  (let [login-user (select-keys (:params req) [:email :password])]
    (if (user/login login-user)
      (setup-session-and-redirect-to-home login-user req)
      (layout/render "public.html"))))

(defn profile [req]
  (if (authenticated? req)
    (layout/render "profile.html" {:user (user-from-request req)
                                   :blood-types user/blood-types})
    (layout/render "public.html")))

(defn update-profile [req]
  (if (authenticated? req)
    (let [user (user-from-request req)
          profile-params-keys [:name :birth-date :blood-type :phone :bike-brand :bike-model]
          profile-params (select-keys (:params req) profile-params-keys)
          user-key (user/user-key (:email user))]
      (do (profile/update user-key profile-params)
          (layout/render "profile.html" {:message "Saved!"
                                         :user (user-from-request req)
                                         :blood-types user/blood-types})))
    (layout/render "public.html")))

(defn rides [req]
  (layout/render "rides.html" {:authenticated (authenticated? req)
                               :rides (ride/all)}))

(defn create-ride [req]
  (let [user (user-from-request req)
        params (assoc (:params req) :user (:email user))]
    (do (ride/create params)
        (rides req))))

(defn apply-to-ride [req]
  (let [user (user-from-request req)
        email (:email user)
        ride-id (:ride-id (:params req))]
    (do (ride/apply-to email ride-id)
      (rides req))))

(defroutes home-routes
  (GET "/" [] home-page)
  (GET "/about" [] (about-page))
  (POST "/sign-up" [] sign-up)
  (POST "/login" [] login)
  (GET "/profile" [] profile)
  (POST "/profile" [] update-profile)
  (GET "/rides" [] rides)
  (POST "/rides" [] create-ride)
  (GET "/apply" [] apply-to-ride))
