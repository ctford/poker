(ns poker.refactor)

(defn threaded-first? [form] (-> form first #{'-> 'clojure.core/->}))
(defn threaded-last? [form] (-> form first #{'->> 'clojure.core/->>}))

(defn append [x xs] (concat xs (list x)))
(defn splice-second [x xs] (concat (take 1 xs) (list x) (rest xs)))
(defn listify [xs] (if (list? xs) xs (list xs)))
(defn delistify [xs] (if (rest xs) xs (first xs)))

(defn unthread [[maybe-arrow value & transformations :as form]]
  (cond
    (threaded-first? form) (reduce splice-second value (map listify transformations))
    (threaded-last? form) (reduce append value (map listify transformations))
    :otherwise form))

(defn thread-last [form]
  (if-not (list? (last form))
    (list '->> (last form) (drop-last form)) 
    (concat (thread-last (last form)) (list (drop-last form)))))

(defn thread-first [form]
  (cons '->
    ((fn inner [form]
      (if-not (list? (second form))
        (list (second form) (cons (first form) (drop 2 form))) 
        (concat (inner (second form))(list (cons (first form) (drop 2 form))))))
     form)))
