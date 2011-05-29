# calculator

This is a project to be done for one of the Houston Clojure group meetings.

Build a function `calculate` that parses and evaluates a string.

There are lots of examples of doing this in an imperative or OO language.  Keeping track of the input string and the operator stack might be just stateful enough to be problematic.

Some ideas:

* Write a java library to solve the problem and bind it to clojure
* Write a parser to translate the input into clojure and eval it
* Inject an infix macro around the input and eval it
* Use mutable state in clojure (var/ref/atom/agent)
* Track state as function params
* Applicative Functors/Monads for parsing and state management

## Usage

A basic structure and test file is provided.  It can be run using `lein test`.  I assume cake should work similarly.
