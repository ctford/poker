(ns poker.refactor)

(defn- splice-second [x [y & ys]] (cons y (cons x ys)))
(defn- append [x ys] (concat ys (list x)))

(defn unthread
  "If form is either threaded-first or threaded-last, returns
  the equivalent nested form."
  [[arrow value & transformations :as form]]
  (let [single? #{'-> 'clojure.core/->}
        double? #{'->> 'clojure.core/->>}
        listify (fn [xs] (if (list? xs) xs (list xs)))
        inner-forms (map listify transformations)]
    (cond
      (single? arrow) (reduce splice-second value inner-forms)
      (double? arrow) (reduce append value inner-forms)
      :otherwise form)))

(defn- de-nest-with [split form]
  (let [[inner outer] (split form)]
    (if (list? inner)
      (-> (de-nest-with split inner)
          (update-in [:transformations] (partial append outer)))
      {:value inner :transformations (list outer)})))

(defn- delistify [[x & xs :as form]] (if xs form x))

(defn thread-last
  "Returns the ->> equivalent of the nested form."
  [form]
  (let [split-last (fn [form] [(last form) (drop-last form)])
        {:keys [value transformations]} (de-nest-with split-last form)
        inner-forms (map delistify transformations)]
    (cons '->> (cons value inner-forms))))

(defn thread-first
  "Returns the -> equivalent of the nested form."
  [form]
  (let [split-second (fn [[one two & others]] [two (cons one others)])
        {:keys [value transformations]} (de-nest-with split-second form)
        inner-forms (map delistify transformations)]
    (cons '-> (cons value inner-forms))))
