package noughtsandcrosses;

import javax.swing.*;

public final class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::launchGame);
    }

    private static void launchGame() {
        new GameWindow(chooseOptions());
    }

    private static GameOptions chooseOptions() {
        if (!askVersusComputer()) {
            return GameOptions.humanVsHuman();
        }
        Symbol humanSymbol = askSymbol();
        Difficulty difficulty = askDifficulty();
        boolean humanFirst = askHumanFirst();
        return GameOptions.versusComputer(humanSymbol, humanFirst, new ComputerPlayer(difficulty));
    }

    private static boolean askVersusComputer() {
        return showDialog("Who would you like to play against?", new String[]{"Human", "Computer"}, 0) == 1;
    }

    private static Symbol askSymbol() {
        return showDialog("Which symbol would you like to play as?", new String[]{"X", "O"}, 0) == 1
            ? Symbol.O : Symbol.X;
    }

    private static boolean askHumanFirst() {
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
