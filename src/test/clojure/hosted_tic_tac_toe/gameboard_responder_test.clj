(ns hosted-tic-tac-toe.gameboard-responder-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.gameboard-responder :as responder]
            [tictactoe.board :as board]))

(import '(http_messages Response Request$RequestBuilder HTTPStatus ResponseHeader HTMLContent))

(defn valid-gameboard-request []
  (.build (Request$RequestBuilder. "GET /gameboard")))

(defn response-body [request]
  (.getBody (responder/get-board-response request)))

(defn invalid-gameboard-request []
  (.build (Request$RequestBuilder. "POST /gameboard")))

(deftest returns-response-object
  (is (= Response (type (responder/get-board-response (valid-gameboard-request))))))

(deftest valid-response-code-is-200
  (is (= (.getStatusLine HTTPStatus/OK) (.getStatusLine (responder/get-board-response (valid-gameboard-request))))))

(deftest invalid-response-code-is-405
  (is (= (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED) (.getStatusLine (responder/get-board-response (invalid-gameboard-request))))))

(deftest response-has-html-header
  (is (= (.getLine (get (.getHeaders (responder/get-board-response (valid-gameboard-request))) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest valid-request-returns-html-template
    (is (true? (and (.contains (response-body (valid-gameboard-request)) (HTMLContent/openHTMLAndBody responder/page-name))
                    (.contains (response-body (valid-gameboard-request)) (HTMLContent/closeBodyAndHTML))))))

(deftest valid-request-html-response-has-empty-board
  (is (true? (.contains (response-body (valid-gameboard-request)) (str (board/make-board))))))

(deftest invalid-request-does-not-get-response-body
  (is (empty? (response-body (invalid-gameboard-request)))))

