(ns clojure-presentation-2017.basic)


;; ===========================================================================
;; Évaluation et REPL
(comment
  (+ 1 2 3 4) ;; Qu'est-ce qui ce passe ici?

  (def a (quote (+ 1 2 3 4))) ;; quote retire l'évaluation, sinon on aurait a = 10
  '(+ 1 2 3 4)
  (type :toto)
  (type a)
  (type (quote a))
  (eval a))


;; ===========================================================================
;; Évaluation et REPL
(def a-function (fn [a b c] (+ a b c)))

(defn another-function
  [a b c]
  (+ a b c))


(defmacro infix
  [operation]
  (let [operator (second operation)
        arg1     (first operation)
        arg2     (last operation)]
    (list operator arg1 arg2)))

(comment
 (macroexpand '(infix (1 + 2)))
 (infix (3 + 5))
 )


;; ===========================================================================
;; Immutabilité

(def a-map {:a 10
            :b 20
            :c 30})

(comment
  a-map
  (assoc a-map :b 40)
  a-map)


;; ===========================================================================
;;Fonctions d'ordre supérieur
(comment
  (defn random-account
    []
    {:id   (java.util.UUID/randomUUID)
     :cash (* 100 (rand))})

  (defn broke?
    [account]
    (let [cash (get account :cash)]
      (< cash 10)))

  (def accounts (take 100 (repeatedly random-account)))

  (filter broke? accounts)

  (filter (fn [account] (not (broke? account))) accounts)
  (filter #(not (broke? %)) accounts)
  (filter (complement broke?) accounts)


  ;; Retourner tous les ID des comptes qui ont peu d'argent dans une string
  ;; dans l'ordre du plus pauvre au plus riche
  (->> accounts
       (filter broke?)
       (sort-by :cash)
       (map :id)
       (map #(str % " has not that much cash")))

  (macroexpand '(->> accounts
                     (filter broke?)
                     (sort-by :cash)
                     (map :id)
                     (map #(str % " has not that much cash"))))
)
