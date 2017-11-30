(ns clojure-presentation-2017.top10
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(defn load-csv-resource
  [filename]
  (-> (io/resource filename)
      (slurp)
      (csv/read-csv)))


(defn parse-movie-row
  [csv-row]
  (let [[id title _] csv-row]
    {:id (Integer/parseInt id) :title title}))


(defn parse-movies
  [csv]
  (let [movies (rest csv)] ;discard header
    (->> movies
         (map parse-movie-row)
         (reduce #(assoc %1 (:id %2) %2) {}))))


(defn parse-rating-row
  [csv-row]
  (let [[_ id rating _] csv-row]
    {:id     (Integer/parseInt id)
     :rating (Float/parseFloat rating)}))

(defn parse-ratings
  [csv]
  (let [ratings (rest csv)]
    (mapv parse-rating-row ratings)))


(def movies (parse-movies (load-csv-resource "movies.csv")))
(def ratings (parse-ratings (load-csv-resource "ratings.csv")))


(defn compute-average-rating
  [[id rating-maps]]
  (let [ratings        (mapv :rating rating-maps)
        average-rating (/ (apply + ratings)
                          (count ratings))]
    [id average-rating]))

(defn top-10
  []
  (->> ratings
       (group-by :id)
       (map compute-average-rating)
       (sort-by second)
       (reverse)
       (map #(get movies (first %)))
       (map :title)
       (take 10)))
