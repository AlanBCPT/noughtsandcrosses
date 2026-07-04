package noughtsandcrosses;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private static final Font STATUS_FONT       = new Font("SansSerif", Font.BOLD, 18);
    private static final int  WINDOW_SIZE       = 480;
    private static final int  WINDOW_CHROME     = 90;
    private static final int  COMPUTER_DELAY_MS = 450;

    private final boolean        vsComputer;
    private final ComputerPlayer computer;
    private final Symbol         humanSymbol;
    private final boolean        humanFirst;
    private final JLabel         statusLabel;
    private final BoardPanel     boardPanel;
    private       Game           game;

    public GameWindow(boolean vsComputer, ComputerPlayer computer, Symbol humanSymbol, boolean humanFirst) {
        super("Noughts and Crosses");
        this.vsComputer  = vsComputer;
        this.computer    = computer;
        this.humanSymbol = humanSymbol;
        this.humanFirst  = humanFirst;
        this.game        = createGame();

        statusLabel = buildStatusLabel();
        boardPanel  = new BoardPanel(this::onCellClicked);

        setLayout(new BorderLayout(0, 8));
        add(statusLabel,    BorderLayout.NORTH);
        add(boardPanel,     BorderLayout.CENTER);
        add(buildButtonBar(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_SIZE, WINDOW_SIZE + WINDOW_CHROME);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        startTurn();
    }

    private void onCellClicked(int square) {
        Game.Result result = game.takeTurn(square);
        boardPanel.refresh(game.getBoard());

        if (result != Game.Result.IN_PROGRESS) {
            handleGameOver(result);
            return;
        }

        if (vsComputer) {
            boardPanel.disableAll();
            scheduleComputerMove();
        } else {
            setStatus(humanTurnStatus());
        }
    }

    private void scheduleComputerMove() {
        setStatus("Computer is thinking\u2026");
        Timer timer = new Timer(COMPUTER_DELAY_MS, e -> {
            ((Timer) e.getSource()).stop();
            int square = computer.chooseMove(game.getBoard());
            Game.Result result = game.takeTurn(square);
            boardPanel.refresh(game.getBoard());

            if (result != Game.Result.IN_PROGRESS) {
                handleGameOver(result);
            } else {
                boardPanel.enableAvailable(game.getBoard());
                setStatus(humanTurnStatus());
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void handleGameOver(Game.Result result) {
        game.getBoard().winningLine().ifPresent(boardPanel::highlightWinners);
        boardPanel.disableAll();
        String message = gameOverMessage(result);
        setStatus(message);
        promptPlayAgain(message);
    }

    private String gameOverMessage(Game.Result result) {
        switch (result) {
            case X_WINS: return vsComputer ? (humanSymbol == Symbol.X ? "You win!" : "Computer wins!") : "Player X wins!";
            case O_WINS: return vsComputer ? (humanSymbol == Symbol.O ? "You win!" : "Computer wins!") : "Player O wins!";
            case DRAW:   return "It\u2019s a draw!";
            default:     return "";
        }
    }

    private void promptPlayAgain(String message) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Play Again", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                this,
                message + " Would you like to play again?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
            );
            if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            } else {
                resetGame();
            }
        });
    }

    private void resetGame() {
        game = createGame();
        boardPanel.reset();
        startTurn();
    }

    private Game createGame() {
        return new Game(humanFirst ? humanSymbol : humanSymbol.opponent());
    }

    private void startTurn() {
        if (vsComputer && !humanFirst) {
            boardPanel.disableAll();
            scheduleComputerMove();
        } else {
            setStatus(humanTurnStatus());
        }
    }

    private String humanTurnStatus() {
        return vsComputer ? "Your move\u2026" : "Player " + game.getCurrentPlayer() + "\u2019s turn";
    }

    private void setStatus(String text) {
        statusLabel.setText(text);
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
