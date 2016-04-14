(ns tictactoe.copy-en-us)

(def welcome-message "Hello and Welcome to TicTacToe!")

(def two-ai-game-description "Game: Two Computer")

(def player-ai-game-description "Game: Player (X) vs. Computer (O)")

(defn game-type-prompt [player-ai-game-type two-ai-game-type]
  (str "Enter " player-ai-game-type " to play an AI. \n"
       "Enter " two-ai-game-type " to see 2 AIs play eachother: "))

(defn player-turn-prompt [marker]
 (str "Player " marker ", please choose your spot: "))

(def ai-choosing-message "AI is Considering the Options")

(defn winner-message [winner]
  (str "Game Over.\n" winner " has won the game."))

(def cats-game-message "Game of Cats, this is a Cats Game")

