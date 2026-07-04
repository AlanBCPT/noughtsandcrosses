# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

A Noughts and Crosses (Tic-Tac-Toe) game built in Java with a Swing GUI. Maven is configured but cannot reach Maven Central due to an SSL trust-store issue on this machine — use `javac`/`java` directly instead.

## Commands

**Compile:**
```bash
javac -d target/classes src/main/java/noughtsandcrosses/*.java
```

**Run:**
```bash
java -cp target/classes noughtsandcrosses.Main
```

**Test** (JUnit 5 console launcher is vendored in `lib/`, git-ignored — see README for the one-time download; use `;` between classpath entries on Windows, `:` elsewhere):
```bash
javac -cp "target/classes;lib/junit-platform-console-standalone.jar" -d target/test-classes src/test/java/noughtsandcrosses/*.java
java -jar lib/junit-platform-console-standalone.jar execute -cp "target/classes;target/test-classes" --scan-classpath
```

## Architecture

The codebase separates game logic from presentation cleanly. Cells are addressed by a 0-based index (0–8) consistently across every layer.

**Pure logic layer** (no Swing, no I/O):
- `Symbol` — enum `X`/`O` with `opponent()` helper
- `Difficulty` — enum `EASY`/`MEDIUM`/`HARD`
- `Board` — immutable; holds a `Symbol[]` (`null` = empty). `place(cell, symbol)` returns a new `Board` and rejects taken/out-of-range cells. Also exposes `isAvailable`, `symbolAt`, `availableCells`, `isFull`, `winner` (`Optional<Symbol>`), and `winningLine` (`Optional<int[]>`).
- `ComputerPlayer` — `chooseMove(board, toMove)` dispatches on `Difficulty`: random (`EASY`), win/block/random (`MEDIUM`), minimax (`HARD`). It searches over immutable `Board` copies and stores no symbol of its own.
- `Game` — owns a `Board` and `currentPlayer`; `takeTurn(cell)` returns a `Game.Result` enum (`IN_PROGRESS`, `X_WINS`, `O_WINS`, `DRAW`). `board()` exposes the current immutable board.
- `GameOptions` — immutable match setup built via `humanVsHuman()` or `versusComputer(humanSymbol, humanFirst, computer)`; a game is versus a computer exactly when a `ComputerPlayer` is present, so the two can never disagree.

**Presentation layer** (Swing):
- `BoardPanel` — `JPanel` with 9 `JButton` cells in a `GridLayout`; calls back via `MoveListener`. `render(Board)` draws state, `enableAvailable`/`disableAll` control input, `highlightWinners(int[])` marks the winning line. Rendering and input-enablement are separate concerns.
- `GameWindow` — `JFrame` that owns a `Game` and drives the loop: click → `game.takeTurn` → `render` → if it is the computer's turn, fires a `javax.swing.Timer` (450 ms) for its move. Whose turn it is comes solely from `game.currentPlayer()`.
- `Main` — entry point; calls `SwingUtilities.invokeLater`, shows `JOptionPane` setup dialogs, builds a `GameOptions`, and constructs `GameWindow`.

**Key wiring detail:** the human plays `GameOptions.humanSymbol()` (default `X`); the computer plays its opponent. There is a single source of truth for whose turn it is — `Game.currentPlayer()` — which `GameWindow` passes to `ComputerPlayer.chooseMove`.
