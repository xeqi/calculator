(ns calculator.test.core
  (:use [calculator.core])
  (:use [clojure.test]))

(deftest numbers
  (is (= 3 (calculate "3")))
  (is (= 2 (calculate "2")))
  (is (= 10002 (calculate "10002"))))

(deftest addition
  (is (= 3 (calculate "1 + 2")))
  (is (= 4 (calculate "2 + 2"))))

(deftest multiplication
  (is (= 16 (calculate "4 * 4")))
  (is (= 6 (calculate "1 * 6"))))

(deftest division
  (is (= 1 (calculate "4 / 4")))
  (is (= 0 (calculate "1 / 2"))))

(deftest order-of-operations
  (is (= 19 (calculate "4 + 3 * 5"))))

(deftest parenthesis
  (is (= 3 (calculate "(5 - 2)")))
  (is (= 3 (calculate "9 / (5 - 2)")))
  (is (= 2 (calculate "26 / (3 * (7 - 4 + 1))"))))

(deftest white-space-is-meaningless
  (is (= 3 (calculate "  ( 4+23  )-24   "))))

(deftest error-handling
  (is (= "Error at character 2: expected ) ; got eol" (calculate "(")))
  
  (is (= "Error at character 1: expected number or ( ; got +" (calculate "+")))
  (is (= "Error at character 1: expected number or ( ; got eol" (calculate "")))
  (is (= "Error at character 1: expected number or ( ; got x" (calculate "x")))
  (is (= "Error at character 3: expected operator or eol ; got )"
         (calculate "1 + 3)")))
  (is (= "Error at character 3: expected operator or eol ; got 3"
         (calculate "1 3")))
  (is (= "Error at character 3: expected operator or eol ; got ("
         (calculate "1 (")))
  (is (= "Error at character 5: expected number or ( ; got +"
         (calculate "1 + +"))))
