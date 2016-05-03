(ns hosted-tic-tac-toe.gameboard-responder-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.gameboard-responder :as responder]
            [tictactoe.board :as board]))

(import '(http_messages Response Request Request$RequestBuilder HTTPStatus ResponseHeader HTMLContent))

(def updated-board (board/fill-space 1 "X" (board/make-board)))

(def get-request (.build (Request$RequestBuilder. (str "GET /gameboard"))))

(def invalid-get-request (.build (Request$RequestBuilder. "POST /gameboard")))

(def put-data (str "[0 1 2 3 4 5 6 7 8]" (Request/newLine) "1" (Request/newLine) "X"))

(def valid-put-request
  (.build (Request$RequestBuilder. (str "PUT /gameboard" (Request/newLine) (Request/newLine) put-data))))

(defn response-body [request]
  (.getBody (responder/get-board-response request)))

(deftest returns-response-object
  (is (= Response (type (responder/get-board-response get-request)))))

(deftest valid-response-code-is-200
  (is (= (.getStatusLine HTTPStatus/OK) (.getStatusLine (responder/get-board-response get-request)))))

(deftest invalid-response-code-is-405
  (is (= (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED) (.getStatusLine (responder/get-board-response invalid-get-request)))))

(deftest response-has-html-header
  (is (= (.getLine (get (.getHeaders (responder/get-board-response get-request)) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest valid-get-request-returns-html-template
  (is (true? (and (.contains (response-body get-request) (HTMLContent/openHTMLAndBody responder/page-name))
                  (.contains (response-body get-request) (HTMLContent/closeBodyAndHTML))))))

(deftest get-gameboard-returns-empty-board
  (is (true? (.contains (response-body get-request) (str (board/make-board))))))

(deftest invalid-get-request-gets-empty-response-body
  (is (empty? (response-body invalid-get-request))))

(deftest valid-put-response-code
  (is (= (.getStatusLine HTTPStatus/OK) (.getStatusLine (responder/get-board-response valid-put-request)))))

(deftest put-response-has-html-header
  (is (= (.getLine (get (.getHeaders (responder/get-board-response valid-put-request)) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest parses-and-formats-request-board-for-evalueation
  (is (= (vec '("X" 1 2 3 4 5 6 7 8)) (responder/parse-board-data "[X 1 2 3 4 5 6 7 8]"))))

(deftest valid-put-updates-board
  (is (true? (.contains (response-body valid-put-request) (str updated-board)))))
