(ns hosted-tic-tac-toe.end-game-responder-test
  (require [clojure.test :refer :all]
           [hosted-tic-tac-toe.end-game-responder :as responder]
           [hosted-tic-tac-toe.cookie-manager :as cookie-manager]
           [hosted-tic-tac-toe.end-game-html :as end-game-view]))

(import '(http_messages Request$RequestBuilder HTTPStatus ResponseHeader Request)
        '(java.net URLEncoder))

(def end-game-responder (responder/new-end-game-responder))

(def end-game-request
  (.build (Request$RequestBuilder. (str "GET /game-over" (URLEncoder/encode "?winner=X" "UTF-8")))))

(def unallowed-request (.build (Request$RequestBuilder. (str "POST /game-over"))))

(def request-that-throws-error (.build (Request$RequestBuilder. "GET /game-over?winner=")))

(deftest valid-end-game-response-status
  (with-redefs [responder/request-has-session-cookie (fn [request] true)]
    (is (= (.getStatusLine (.getResponse end-game-responder end-game-request)) (.getStatusLine HTTPStatus/OK)))))

(deftest invalid-method-response-status
  (is (= (.getStatusLine (.getResponse end-game-responder unallowed-request)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(deftest request-without-cookie-gets-method-not-allowed-status
  (with-redefs [responder/request-has-session-cookie (fn [request] false)]
    (is (= (.getStatusLine (.getResponse end-game-responder end-game-request)) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))))

(deftest catches-errors-and-responds
  (with-redefs [responder/request-has-session-cookie (fn [request] true)]
  (is (= (.getStatusLine (.getResponse end-game-responder request-that-throws-error)) (.getStatusLine HTTPStatus/NOT_FOUND)))))

(deftest end-game-response-has-content-type-header
  (with-redefs [responder/request-has-session-cookie (fn [request] true)]
    (is (= (.getLine (first (.getHeaders (.getResponse end-game-responder end-game-request))))
           (str (.getKeyword ResponseHeader/CONTENT_TYPE) end-game-view/content-type ";")))))

(deftest response-deletes-cookies-to-prevent-page-reload-with-different-params
  (with-redefs [responder/request-has-session-cookie (fn [request] true)]
    (is (= (.getLine (second (.getHeaders (.getResponse end-game-responder end-game-request))))
           (.getLine cookie-manager/remove-cookies-header)))))

(deftest end-game-response-has-end-game-view
  (with-redefs [responder/request-has-session-cookie (fn [request] true)]
    (is (= (.getBody (.getResponse end-game-responder end-game-request)) (end-game-view/get-page "X")))))

(deftest request-without-cookie-does-not-display-a-winner
  (with-redefs [responder/request-has-session-cookie (fn [request] false)]
    (is (false? (.contains (.getBody (.getResponse end-game-responder end-game-request)) (end-game-view/get-page "X"))))))

