(ns poker.core-test
  (:use midje.sweet)
  (:require [poker.core :as refactor]))

(fact "Unthreading replaces a ->> pipeline with nested forms." 
  (refactor/unthread '(->> [2 3] (map inc) (filter even?))) =>
  '(filter even? (map inc [2 3])))

(fact "Threading-last replaces nested forms with a ->> pipeline." 
  (refactor/thread-last '(filter even? (map inc [2 3]))) =>
  '(->> [2 3] (map inc) (filter even?)))

(fact "Unthreading replaces a -> pipeline with nested forms." 
  (refactor/unthread '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc))) =>
  '(update-in (assoc {:foo 2} :bar 1) [:foo] inc))

(fact "Threading-first replaces nested forms with a -> pipeline." 
  (refactor/thread-first '(update-in (assoc {:foo 2} :bar 1) [:foo] inc)) =>
  '(-> {:foo 2} (assoc :bar 1) (update-in [:foo] inc)))
