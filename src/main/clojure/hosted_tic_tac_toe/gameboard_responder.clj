(ns hosted-tic-tac-toe.gameboard-responder
  (:require [clojure.string :as string]
            [tictactoe.board :as board]
			[hosted-tic-tac-toe.gameboard-html :as gameboard-html]))

(import '(responders Responder)
        '(http_messages HTTPStatus HTMLContent Request
                        Response$ResponseBuilder Header$HeaderBuilder
                        Header ResponseHeader)
        '(decoders UTF8Decoder))

(def page-name "Game Board")

(def supported-methods ["GET" "POST"])

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-status-line [request]
  (if (request-is-supported request)
    (.getStatusLine HTTPStatus/OK)
    (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))

(defn- get-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
    (into-array Header [content-type])))

(defn- get-board-data [request]
  (let [board (.getData request)]
      board))

(defn parse-board-data [board-string]
  (let [symbolic-board (map read-string (clojure.string/split board-string #""))]
    (vec (map #(if (not (number? %)) (name %) %) symbolic-board))))

(defn- update-board [request-data]
  (let [data-parts (string/split (UTF8Decoder/decode request-data) #"&")]
    (let [board-string (get (string/split (get data-parts 0) #"=") 1)]
      (let [current-board (parse-board-data board-string)]
        (let [choice (read-string (get (string/split (get data-parts 1) #"=") 1))]
          (let [marker (get (string/split (get data-parts 2) #"=") 1)]
            (board/fill-space choice marker current-board)))))))

(defn- get-body [request]
  (if (request-is-supported request)
    (cond
      (= (.getMethod request) "GET")
        (str (HTMLContent/openHTMLAndBody page-name) (gameboard-html/formatted-board (board/make-board)) (HTMLContent/closeBodyAndHTML))
      (= (.getMethod request) "POST")
        (str (HTMLContent/openHTMLAndBody page-name) (gameboard-html/formatted-board (update-board (.getData request))) (HTMLContent/closeBodyAndHTML))
      :else "")
    ""))

(defn get-board-response [request]
  (let [status-line (get-status-line request)]
    (let [headers (get-headers)]
      (let [body (get-body request)]
        (.build (.body (.headers (.statusLine (Response$ResponseBuilder.) status-line) headers) body))))))

(defn new-gameboard-responder []
  (reify
    Responder
      (getResponse [this request] (get-board-response request))))
