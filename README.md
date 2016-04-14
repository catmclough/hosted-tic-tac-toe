# hosted-tic-tac-toe

 A [TicTacToe game] (https://github.com/catmclough/clojure-tictactoe) written in Clojure and hosted by a [Java Server] (https://github.com/catmclough/java-http-server).

## Dependencies

- [Clojure 1.8.0] (http://clojure.org/community/downloads)
- Java 8
- JUnit 4
- [Leiningen] (http://leiningen.org/)

## Usage

To run the program, compile a JAR file from the project's root directory.

    $ lein uberjar

Then run the jar.

    $ java -jar hosted-tic-tac-toe-0.1.0-standalone.jar [args]

Run the hosted-tic-tac-toe app's tests alone.

    $ lein test hosted-tic-tac-toe.core-test

## Options

Run tests for the hosted app and the Java Server together.

    $ lein test

Run the Java Server's JUnit tests.

    $ lein junit
