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

- Java 8 or later (developed and tested on Java 21)

## Building and Running

Compile:

```bash
javac -d target/classes src/main/java/noughtsandcrosses/*.java
```

Run:

```bash
java -cp target/classes noughtsandcrosses.Main
```

On startup, a dialog asks whether you want to play against a human or the computer. If you choose the computer, further dialogs let you pick your symbol, the difficulty, and who goes first.

## Testing

The tests use JUnit 5. Maven Central is unreachable on this machine, so the JUnit
console launcher is vendored into `lib/` (git-ignored). Download it once:

```bash
mkdir -p lib
curl -o lib/junit-platform-console-standalone.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
```

Compile and run the suite (use `;` between classpath entries on Windows, `:` on macOS/Linux):

```bash
javac -cp "target/classes;lib/junit-platform-console-standalone.jar" \
  -d target/test-classes src/test/java/noughtsandcrosses/*.java

java -jar lib/junit-platform-console-standalone.jar execute \
  -cp "target/classes;target/test-classes" --scan-classpath
```

The logic layer (`Board`, `Game`, `ComputerPlayer`, `GameOptions`, `Symbol`) and
`BoardPanel` are covered to 100% of lines (the sole exception being one
unreachable defensive branch). `GameWindow` and `Main` are thin Swing
orchestration — window setup, dialogs, and timers — and are exercised by playing
the game rather than by unit tests.

## Project Structure

```
src/main/java/noughtsandcrosses/
├── Symbol.java          # Enum: X / O, with opponent() helper
├── Difficulty.java      # Enum: EASY / MEDIUM / HARD
├── Board.java           # Immutable 9-cell board; place, winner detection
├── Game.java            # Turn management; returns IN_PROGRESS / X_WINS / O_WINS / DRAW
├── ComputerPlayer.java  # AI strategies: random, tactical (win/block), minimax
├── GameOptions.java     # Immutable match setup: human-vs-human or vs a computer
├── BoardPanel.java      # Swing JPanel — 3×3 grid of buttons
├── GameWindow.java      # Swing JFrame — drives the game loop
└── Main.java            # Entry point; shows setup dialogs, launches GameWindow
```

The logic layer (`Symbol`, `Difficulty`, `Board`, `Game`, `ComputerPlayer`,
`GameOptions`) has no dependency on Swing and can be tested independently. The
presentation layer (`BoardPanel`, `GameWindow`, `Main`) handles all GUI concerns.

Cells are addressed by a 0-based index (0–8, left to right, top to bottom)
consistently across the logic and presentation layers. `Board` is immutable:
`place` returns a new `Board`, so the AI can explore moves without mutating game
state.
