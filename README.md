# hosted-tic-tac-toe

 A TicTacToe game written in Clojure and hosted by a Java Server.

## Dependencies

- [Clojure Tic-Tac-Toe Jar](https://github.com/catmclough/clojure-tictactoe) (on [Clojars] (https://clojars.org/speclj_ttt))
- [Java Server Project](https://github.com/catmclough/java-http-server) (included in this repo)
- [Clojure 1.8.0](http://clojure.org/community/downloads)
- Java 8
- [Leiningen](http://leiningen.org/)

## Usage

To run the program, compile a JAR file from the project's root directory.

    $ lein uberjar

Then run the program.

    $ lein run

## Options

You may specify any port on which you'd like to run the server using the -D flag (the default port is 5000).

    $ lein run -D 9090

Run the hosted-tic-tac-toe app's tests.

    $ lein test

(Unit tests for the tictactoe logic and java server are included in their respective repositories)
