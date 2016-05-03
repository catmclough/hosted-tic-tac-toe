(ns hosted-tic-tac-toe.gameboard-responder-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.gameboard-responder :as responder]
            [hosted-tic-tac-toe.gameboard-html :as gameboard-html]
            [tictactoe.board :as board]))

(import '(http_messages Response Request Request$RequestBuilder HTTPStatus ResponseHeader HTMLContent))

(def updated-board (board/fill-space 1 "X" (board/make-board)))

(def get-request (.build (Request$RequestBuilder. (str "GET /gameboard"))))

(def invalid-get-request (.build (Request$RequestBuilder. "HEAD /gameboard")))

(def current-board ["X" 1 2 3 4 5 6 7 8])

(def current-board-data "X12345678")

(def move-data (str "board=" current-board-data "&choice=1&marker=O"))

(def updated-board ["X" "O" 2 3 4 5 6 7 8])

(def valid-post-request
  (.build (Request$RequestBuilder. (str "POST /gameboard" (Request/newLine) (Request/newLine) move-data))))

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
  (is (true? (.contains (response-body get-request) (gameboard-html/formatted-board (board/make-board))))))

(deftest invalid-get-request-gets-empty-response-body
  (is (empty? (response-body invalid-get-request))))

(deftest valid-post-response-code
  (is (= (.getStatusLine HTTPStatus/OK) (.getStatusLine (responder/get-board-response valid-post-request)))))

(deftest post-response-html-header
  (is (= (.getLine (get (.getHeaders (responder/get-board-response valid-post-request)) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest parses-and-formats-request-board-for-evalueation
  (is (= (responder/parse-board-data current-board-data) current-board)))

(deftest valid-post-updates-board
  (is (true? (.contains (response-body valid-post-request) (gameboard-html/formatted-board updated-board)))))
