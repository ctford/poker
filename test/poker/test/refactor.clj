(ns poker.test.refactor
  (:use midje.sweet)
  (:require [poker.refactor :as refactor]))

(fact "Unthreading replaces a ->> pipeline with nested forms." 
  (refactor/unthread '(->> [2 3] (map inc) (filter even?))) =>
  '(filter even? (map inc [2 3])))

(fact "Unthreading works with a fully-qualified ->>." 
  (refactor/unthread '(clojure.core/->> [2 3] (map inc) (filter even?))) =>
  '(filter even? (map inc [2 3])))

(fact "Unthreading ->> understands naked fns." 
  (refactor/unthread '(->> [2 3] first)) =>
  '(first [2 3]))

(fact "Threading-last replaces nested forms with a ->> pipeline." 
  (refactor/thread-last '(filter even? (map inc [2 3]))) =>
  '(->> [2 3] (map inc) (filter even?)))

(fact "Threading-last produces naked fns where it can." 
  (refactor/thread-last '(first [2 3])) =>
  '(->> [2 3] first))

(fact "Unthreading replaces a -> pipeline with nested forms." 
  (refactor/unthread '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc))) =>
  '(update-in (assoc {:foo 2} :bar 1) [:foo] inc))

(fact "Unthreading works with a fully-qualified ->." 
  (refactor/unthread '(clojure.core/-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc))) =>
  '(update-in (assoc {:foo 2} :bar 1) [:foo] inc))

(fact "Unthreading -> understands naked fns." 
  (refactor/unthread '(-> 1 inc)) =>
  '(inc 1))

(fact "Threading-first replaces nested forms with a -> pipeline." 
  (refactor/thread-first '(update-in (assoc {:foo 2} :bar 1) [:foo] inc)) =>
  '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc)))

(fact "Threading-first produces naked fns where it can." 
  (refactor/thread-first '(inc 1)) =>
  '(-> 1 inc))

(fact "Thread uses thread-last where the value is sequential."
  (refactor/thread '(filter even? (map inc [2 3]))) =>
  '(->> [2 3] (map inc) (filter even?)))

(fact "Thread uses thread-first where the value isn't sequential."
  (refactor/thread '(update-in (assoc {:foo 2} :bar 1) [:foo] inc)) =>
  '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc)))

(fact "Thread prefers thread-last where both could apply."
  (refactor/thread '(cons (+ 1 1) (map inc [2 3]))) =>
  '(->> [2 3] (map inc) (cons (+ 1 1))))

(fact "Inlining-locally replaces occurances of bindings from a wrapping let."
   (refactor/inline-local
     'double-x
     '(defn foo [x] (let [double-x (+ x x)] (* 3 double-x)))) =>
   '(defn foo [x] (* 3 (+ x x)))
   (refactor/inline-local
     'double-x
     '(defn foo [x] (let [double-x (+ x x)] (* 3 double-x double-x)))) =>
   '(defn foo [x] (* 3 (+ x x) (+ x x))))

(fact "Extracting-locally pulls the targetted form into a let binding."
   (refactor/extract-local
     '(+ x x)
     'double-x '(defn foo [x] (* 3 (+ x x)))) =>
   '(defn foo [x] (let [double-x (+ x x)] (* 3 double-x)))
   (refactor/extract-local
     '(+ x x)
     'double-x '(defn foo [x] (* 3 (+ x x) (+ x x)))) =>
   '(defn foo [x] (let [double-x (+ x x)] (* 3 double-x double-x))))
