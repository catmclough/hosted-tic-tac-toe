(ns hosted-tic-tac-toe.gameboard-html
  (require [tictactoe.board :as board]))

(import '(http_messages HTMLContent))

(def page-name "Game Board")

(defn get-button [board space marker]
  (str "<form action=\"/gameboard\" method=\"post\">
          <input type=\"hidden\" name=\"board\" value=\"" board "\"/>
          <input type=\"hidden\" name=\"choice\" value=\"" space "\"/>
          <input type=\"hidden\" name=\"marker\" value=\"" marker "\"/>
          <input type=\"submit\" value=\"Click Me!\" />
        </form>"))

(defn- get-board-space [gameboard space]
  (if (some #{space} (board/available-spaces gameboard))
    (get-button (apply str gameboard) space (board/active-player gameboard))
    (get gameboard space)))

(defn- get-ttt-board [board]
    (str "<table style=\"width:25%\">
    <caption>TicTacToe Board</caption>
    <tr>
      <td>" (get-board-space board 0) "</td>
      <td>" (get-board-space board 1) "</td>
      <td>" (get-board-space board 2) "</td>
    </tr>
    <tr>
      <td>" (get-board-space board 3) "</td>
      <td>" (get-board-space board 4) "</td>
      <td>" (get-board-space board 5) "</td>
    </tr>
    <tr>
      <td>" (get-board-space board 6) "</td>
      <td>" (get-board-space board 7) "</td>
      <td>" (get-board-space board 8) "</td>
    </tr>
  </table>"))

(defn get-page [board]
  (str
    (HTMLContent/openHTMLAndBody page-name)
    (get-ttt-board board)
    (HTMLContent/closeBodyAndHTML)))
