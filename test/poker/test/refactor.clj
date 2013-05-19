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
  (refactor/unthread '(clojure.core/->> [2 3] first)) =>
  '(first [2 3]))

(fact "Threading-last replaces nested forms with a ->> pipeline." 
  (refactor/thread-last '(filter even? (map inc [2 3]))) =>
  '(->> [2 3] (map inc) (filter even?)))

(fact "Unthreading replaces a -> pipeline with nested forms." 
  (refactor/unthread '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc))) =>
  '(update-in (assoc {:foo 2} :bar 1) [:foo] inc))

(fact "Unthreading works with a fully-qualified ->." 
  (refactor/unthread '(clojure.core/-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc))) =>
  '(update-in (assoc {:foo 2} :bar 1) [:foo] inc))

(fact "Unthreading -> understands naked fns." 
  (refactor/unthread '(clojure.core/-> 1 inc)) =>
  '(inc 1))

(fact "Threading-first replaces nested forms with a -> pipeline." 
  (refactor/thread-first '(update-in (assoc {:foo 2} :bar 1) [:foo] inc)) =>
  '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc)))
