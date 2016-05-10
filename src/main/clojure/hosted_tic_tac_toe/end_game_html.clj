(ns hosted-tic-tac-toe.end-game-html)

(import '(http_messages HTMLContent))

(def content-type "text/html")

(def page-title "Game Over")

(defn winner-text [winner] (str "<h3>Winner: Player " winner "</h3>"))

(def tie-game-text "<h3>Cat's Game :(</h3>")

(defn- winner-info [winner]
  (if (= "nil" winner)
    tie-game-text
    (winner-text winner)))

(def new-game-button
  "<form action=\"/\" method=\"/get\">
    <input type=\"submit\" value=\"New Game\" />
   </form>")

(defn get-page [winner]
  (str (HTMLContent/openHTMLAndBody page-title)
       (winner-info winner)
       new-game-button
       (HTMLContent/closeBodyAndHTML)))


