(ns hosted-tic-tac-toe.gameboard-responder-test
  (:require [clojure.test :refer :all]
            [hosted-tic-tac-toe.gameboard-responder :as gameboard-responder]
            [hosted-tic-tac-toe.gameboard-html :as gameboard-html]
            [tictactoe.board :as board]
            ))

(import '(http_messages Response Request Request$RequestBuilder HTTPStatus ResponseHeader HTMLContent)
        '(java.net URLEncoder))

(def responder (gameboard-responder/new-gameboard-responder))

(def get-request (.build (Request$RequestBuilder. "GET /gameboard")))

(def get-request-response (.getResponse responder get-request))

(def unallowed-request (.build (Request$RequestBuilder. "HEAD /gameboard")))

(def request-with-error (.build (Request$RequestBuilder. (str "POST /gameboard" (Request/newLine) (Request/newLine) "unrecognizable data"))))

(def current-board-data "012345678")

(def move-data (str "board=" current-board-data "&choice=0&marker=X"))

(def valid-post-request
  (.build (Request$RequestBuilder. (str "POST /gameboard" (Request/newLine) (Request/newLine) move-data))))

(def valid-post-request-response
  (.getResponse responder valid-post-request))

(deftest gets-valid-response-status
  (is (= (.getStatusLine HTTPStatus/OK) (.getStatusLine get-request-response))))

(deftest gets-unallowed-response-status
  (is (= (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED) (.getStatusLine (.getResponse responder unallowed-request)))))

(deftest catches-exceptions-and-returns-response
  (is (= (.getStatusLine HTTPStatus/NOT_FOUND) (.getStatusLine (.getResponse responder request-with-error)))))

(deftest response-has-html-header
  (is (= (.getLine (get (.getHeaders get-request-response) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest valid-get-request-returns-html-template
  (is (true? (and (.contains (.getBody get-request-response) (HTMLContent/openHTMLAndBody gameboard-html/page-name))
                  (.contains (.getBody get-request-response) (HTMLContent/closeBodyAndHTML))))))

(deftest get-gameboard-returns-empty-board
  (is (true? (.contains (.getBody get-request-response) (gameboard-html/get-page (board/make-board))))))

(deftest invalid-get-request-gets-empty-response-body
  (is (empty? (.getBody (.getResponse responder unallowed-request)))))

(deftest valid-post-response-code
  (is (= (.getStatusLine HTTPStatus/OK) (.getStatusLine valid-post-request-response))))

(deftest post-response-html-header
  (is (= (.getLine (get (.getHeaders valid-post-request-response) 0))
         (str (.getKeyword ResponseHeader/CONTENT_TYPE) (HTMLContent/contentType) ";"))))

(deftest valid-user-post-updates-board-with-user-and-ai-choice
  (is (true? (.contains (.getBody valid-post-request-response) (gameboard-html/get-page ["X" 1 2 3 "O" 5 6 7 8])))))

(deftest redirects-user-to-game-over-route-with-winner-params
  (let [winning-move (str "board=XX2O45O78&choice=2&marker=X")]
    (let [end-game-request (.build (Request$RequestBuilder. (str "POST /gameboard" (Request/newLine) (Request/newLine) winning-move)))]
      (is (= (.getLine (get (.getHeaders (.getResponse responder end-game-request)) 0))
         (str (.getKeyword ResponseHeader/REDIRECT) "/game-over" (URLEncoder/encode "?winner=X" "UTF-8")))))))

(deftest winner-param-is-nil-if-game-was-tie
  (let [tying-move (str "board=OXO3XOXOX&choice=3&marker=X")]
    (let [tie-game-request (.build (Request$RequestBuilder. (str "POST /gameboard" (Request/newLine) (Request/newLine) tying-move)))]
      (is (= (.getLine (get (.getHeaders (.getResponse responder tie-game-request)) 0))
             (str (.getKeyword ResponseHeader/REDIRECT) "/game-over" (URLEncoder/encode "?winner=nil" "UTF-8")))))))
