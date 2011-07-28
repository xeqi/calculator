(ns calculator.core)

(defn curry [n f]
  (if (= n 1)
    f
    #(curry (- n 1) (partial f %))))

(defn pure [f]
  #(list (list f %)))

(defn <*> [f g]
  (fn [string]
    (for [[v1 s1] (f string)
          [v2 s2] (g s1)]
      [(v1 v2) s2])))

(defn sequ [f & parsers]
  (reduce <*> (pure f) parsers))

(defn bind [f]
  (fn [r]
    (apply concat (map (fn [[v s]] ((f v) s)) r))))

(defn pipe [m & bindable]
  (apply comp (reverse (cons m (map bind bindable)))))

(defn choice [f g]
  (fn [string]
    (concat (f string) (g string))))

(defn choices [& parsers]
  (reduce choice parsers))

(defn pred [f]
  (fn [[head & rest]]
    (if (f head)
      (list [head rest]))))

(defn character [c]
  (pred (partial = c)))

(defn many [parser]
  (choices (fn [s] (list [nil s]))
           (sequ (curry 2 cons) parser #((many parser) %))))

(defn many1 [parser]
  (sequ (curry 2 cons) parser (many parser)))

(def whitespace (character \space))

(defn ignoring-whitespace [parser]
  (sequ (curry 2 (fn [x y] y)) (many whitespace) parser))

(defn digits [string]
  (let [[take rest :as all] (split-with (fn [x] (Character/isDigit x)) string)]
    (if (not (empty? take))
      (list all))))

(def lparen (character \())

(def rparen (character \)))

(defn parens [parser]
  (sequ (curry 3 (fn [l x r] x))
        (ignoring-whitespace lparen)
        parser
        (ignoring-whitespace rparen)))

(def integer
  (sequ (fn [x] (Integer/parseInt (apply str x)))
        digits))

(defn chainl1 [p op]
  (letfn [(more [l]
                (choices (pipe op
                               (fn [o] (pipe p
                                            (fn [r] (more (o l r))))))
                         (pure l)))]
      (comp (bind more) p))) 

(def addop
  (choices (sequ (constantly +) (character \+))
           (sequ (constantly -) (character \-))))

(def mulop
  (choices (sequ (constantly *) (character \*))
           (sequ (constantly (fn [x y] (int (/ x y)))) (character \/))))

(declare factor term expr)

(def factor
  (choices (parens #(expr %))
           (ignoring-whitespace integer)))

(def term
  (chainl1 factor
           (ignoring-whitespace mulop)))

(def expr
  (chainl1 term
           (ignoring-whitespace addop)))

(defn eol [string]
  (if (empty? string)
    (list ['eol string])))

(def lex
  (sequ (curry 2 (fn [x y] x))
        expr
        (ignoring-whitespace eol)))

(defn calculate [string]
  (first (first (lex string))))
