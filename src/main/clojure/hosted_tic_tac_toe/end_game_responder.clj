(ns hosted-tic-tac-toe.end-game-responder
  (require [hosted-tic-tac-toe.end-game-view :as end-game-view]
           [hosted-tic-tac-toe.copy-en-us :as copy]
           [hosted-tic-tac-toe.ttt-round :as ttt-round]))

(import '(responders Responder)
        '(http_messages Response$ResponseBuilder Header$HeaderBuilder
                        ResponseHeader Header HTTPStatus)
        '(text_parsers ParameterParser))

(def supported-methods ["GET"])

(defn request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn get-response-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) end-game-view/content-type ";")))]
    (into-array Header [content-type])))

(defn- get-winner-from-params [request]
  (let [request-line (str (.getMethod request) " " (.getURI request))]
    (let [winner ((clojure.string/split (first (ParameterParser/getDecodedParams request-line)) #"= ") 1)]
      winner)))

(defn get-response-body [request]
  (let [winner (get-winner-from-params request)]
    (if (= winner ttt-round/user-marker)
      end-game-view/get-user-cannot-win-page
      (end-game-view/get-game-over-page winner))))

(defn get-game-over-response [request]
  (try
    (if (or (not (request-is-supported request)))
      (.build (.body (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)) copy/illegal-action-message))
      (.build (.body (.headers (.statusLine (Response$ResponseBuilder.)
          (.getStatusLine HTTPStatus/OK)) (get-response-headers)) (get-response-body request))))
    (catch Exception e
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/NOT_FOUND))))))

(defn new-end-game-responder []
  (reify
    Responder
      (getResponse [this request]
        (get-game-over-response request))))
