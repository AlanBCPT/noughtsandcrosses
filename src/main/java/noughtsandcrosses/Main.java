package noughtsandcrosses;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::launchGame);
    }

    private static void launchGame() {
        boolean vsComputer = askOpponentType();
        Symbol humanSymbol = vsComputer ? askSymbol() : Symbol.X;
        ComputerPlayer computer = vsComputer ? new ComputerPlayer(humanSymbol.opponent(), askDifficulty()) : null;
        boolean humanFirst = !vsComputer || askWhoFirst();
        new GameWindow(vsComputer, computer, humanSymbol, humanFirst);
    }

    private static boolean askOpponentType() {
        return showDialog("Who would you like to play against?", new String[]{"Human", "Computer"}, 0) == 1;
    }

    private static Symbol askSymbol() {
        return showDialog("Which symbol would you like to play as?", new String[]{"X", "O"}, 0) == 1
            ? Symbol.O : Symbol.X;
    }

    private static boolean askWhoFirst() {
        return showDialog("Who goes first?", new String[]{"Me", "Computer"}, 0) == 0;
    }

    private static Difficulty askDifficulty() {
        switch (showDialog("Select difficulty:", new String[]{"Easy", "Medium", "Hard"}, 1)) {
            case 0:  return Difficulty.EASY;
            case 2:  return Difficulty.HARD;
            default: return Difficulty.MEDIUM;
        }
    }

    private static int showDialog(String message, String[] options, int defaultIndex) {
        int choice = JOptionPane.showOptionDialog(
            null, message, "Noughts and Crosses",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[defaultIndex]
        );
        if (choice == JOptionPane.CLOSED_OPTION) System.exit(0);
        return choice;
    }
}
