package noughtsandcrosses;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class GameWindow extends JFrame {

    private static final Font STATUS_FONT       = new Font("SansSerif", Font.BOLD, 18);
    private static final int  WINDOW_SIZE       = 480;
    private static final int  STATUS_AND_BUTTON_BAR_HEIGHT = 90;
    private static final int  COMPUTER_DELAY_MS = 450;

    private final GameOptions options;
    private final JLabel      statusLabel;
    private final BoardPanel  boardPanel;
    private       Game        game;

    public GameWindow(GameOptions options) {
        super("Noughts and Crosses");
        this.options     = Objects.requireNonNull(options, "options");
        this.statusLabel = buildStatusLabel();
        this.boardPanel  = new BoardPanel(this::playMove);
        this.game        = new Game(options.startingPlayer());

        layoutComponents();
        setVisible(true);
        startTurn();
    }

    private void playMove(int cell) {
        Game.Result result = game.takeTurn(cell);
        boardPanel.render(game.board());
        if (result == Game.Result.IN_PROGRESS) {
            advanceTurn();
        } else {
            endGame(result);
        }
    }

    private void startTurn() {
        boardPanel.render(game.board());
        advanceTurn();
    }

    private void advanceTurn() {
        if (isComputersTurn()) {
            boardPanel.disableAll();
            scheduleComputerMove();
        } else {
            boardPanel.enableAvailable(game.board());
            setStatus(humanTurnStatus());
        }
    }

    private boolean isComputersTurn() {
        return options.versusComputer() && game.currentPlayer() == options.computerSymbol();
    }

    private void scheduleComputerMove() {
        setStatus("Computer is thinking…");
        ComputerPlayer computer = options.computer().orElseThrow();
        Timer timer = new Timer(COMPUTER_DELAY_MS, e -> {
            ((Timer) e.getSource()).stop();
            playMove(computer.chooseMove(game.board(), game.currentPlayer()));
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void endGame(Game.Result result) {
        game.board().winningLine().ifPresent(boardPanel::highlightWinners);
        boardPanel.disableAll();
        String message = gameOverMessage(result);
        setStatus(message);
        promptPlayAgain(message);
    }

    private String gameOverMessage(Game.Result result) {
        if (result == Game.Result.DRAW) return "It’s a draw!";
        Symbol winner = result == Game.Result.X_WINS ? Symbol.X : Symbol.O;
        if (!options.versusComputer()) return "Player " + winner + " wins!";
        return winner == options.humanSymbol() ? "You win!" : "Computer wins!";
    }

    private void promptPlayAgain(String message) {
        SwingUtilities.invokeLater(() -> {
            String[] choices = {"Play Again", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                this,
                message + " Would you like to play again?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, choices, choices[0]
            );
            if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            } else {
                resetGame();
            }
        });
    }

    private void resetGame() {
        game = new Game(options.startingPlayer());
        startTurn();
    }

    private String humanTurnStatus() {
        if (options.versusComputer()) return "Your move…";
        return "Player " + game.currentPlayer() + "’s turn";
    }

    private void setStatus(String text) {
        statusLabel.setText(text);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(0, 8));
        add(statusLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(buildButtonBar(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_SIZE, WINDOW_SIZE + STATUS_AND_BUTTON_BAR_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JLabel buildStatusLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(STATUS_FONT);
        label.setBorder(BorderFactory.createEmptyBorder(12, 0, 4, 0));
        return label;
    }

    private JPanel buildButtonBar() {
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> resetGame());
        JPanel bar = new JPanel();
        bar.add(newGame);
        bar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return bar;
    }
}
