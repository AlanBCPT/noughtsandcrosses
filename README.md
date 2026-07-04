# Noughts and Crosses

A Noughts and Crosses (Tic-Tac-Toe) game built in Java with a Swing GUI. Play against a friend or challenge the computer at one of three difficulty levels.

## Features

- **Two-player mode** — play locally against another person
- **vs Computer mode** with full pre-game setup:
  - Choose your symbol — X or O
  - Choose who goes first — you or the computer
  - Three difficulty levels:
    - **Easy** — picks moves at random
    - **Medium** — wins when it can, blocks your winning moves, otherwise plays randomly
    - **Hard** — plays a perfect game using the minimax algorithm; the best you can do is a draw
- **Play again prompt** — at the end of each game, choose to play again or exit

## Requirements

- Java 8 or later

## Building and Running

Compile (order matters — dependencies first):

```bash
javac -d target/classes \
  src/main/java/noughtsandcrosses/Symbol.java \
  src/main/java/noughtsandcrosses/Difficulty.java \
  src/main/java/noughtsandcrosses/Board.java \
  src/main/java/noughtsandcrosses/ComputerPlayer.java \
  src/main/java/noughtsandcrosses/Game.java \
  src/main/java/noughtsandcrosses/BoardPanel.java \
  src/main/java/noughtsandcrosses/GameWindow.java \
  src/main/java/noughtsandcrosses/Main.java
```

Run:

```bash
java -cp target/classes noughtsandcrosses.Main
```

On startup, a dialog asks whether you want to play against a human or the computer. If you choose the computer, further dialogs let you pick your symbol, the difficulty, and who goes first.

## Project Structure

```
src/main/java/noughtsandcrosses/
├── Symbol.java          # Enum: X / O, with opponent() helper
├── Difficulty.java      # Enum: EASY / MEDIUM / HARD
├── Board.java           # 9-cell board; place, undo, winner detection
├── Game.java            # Turn management; returns IN_PROGRESS / X_WINS / O_WINS / DRAW
├── ComputerPlayer.java  # AI strategies: random, tactical (win/block), minimax
├── BoardPanel.java      # Swing JPanel — 3×3 grid of buttons
├── GameWindow.java      # Swing JFrame — drives the game loop
└── Main.java            # Entry point; shows setup dialogs, launches GameWindow
```

The logic layer (`Symbol`, `Difficulty`, `Board`, `Game`, `ComputerPlayer`) has no dependency on Swing and can be tested independently. The presentation layer (`BoardPanel`, `GameWindow`, `Main`) handles all GUI concerns.
