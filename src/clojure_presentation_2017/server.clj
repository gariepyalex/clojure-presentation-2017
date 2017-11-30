(ns clojure-presentation-2017.server
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [clojure-presentation-2017.top10 :refer [top-10]]))


(defn home
  []
  "Hello world")

(defn toto
  [id]
  (println (str "toto called with id " id))
  (str "<h1>" (rand) "</h1>"))

(defn format-top-10
  [top-10]
  (let [li        (map #(str "<li>" % "<li>") top-10)
        li-string (apply str li)]
    (str "<ul>" li-string "</ul>")))

;; Remarquez la macro
(defroutes app
  (GET "/" [] (home))
  (GET "/toto/:id" [id] (toto id))
  (GET "/top10" [] "TODO")
  (route/not-found "Not found"))

;; Pour stocker l'état
(defonce server (atom nil))

(defn start-server
  []
  (reset! server (server/run-server #'app {:port 8080})))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))
