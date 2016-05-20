(ns hosted-tic-tac-toe.end-game-view
  (:require [hosted-tic-tac-toe.copy-en-us :as copy]))

(import '(http_messages HTMLContent))

(def content-type "text/html")

(defn- winner-info [winner]
  (if (= "nil" winner)
    copy/tie-game-text
    (copy/winner-text winner)))

(def new-game-button
  "<form action=\"/\" method=\"/get\">
    <input type=\"submit\" value=\"New Game\" />
   </form>")

(defn get-page [winner]
  (str (HTMLContent/openHTMLAndBody copy/game-over-page-title)
       (winner-info winner)
       new-game-button
       (HTMLContent/closeBodyAndHTML)))


