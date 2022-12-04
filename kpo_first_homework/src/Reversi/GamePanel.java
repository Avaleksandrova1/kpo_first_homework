package Reversi;

import java.util.ArrayList;

/**
 * Создание и взаимодействие с игровой панелью.
 */
public class GamePanel {
    /**
     * Игровое поле.
     */
    private int[][] gamePanel;
    private boolean chipColor;

    private ArrayList<ArrayList<Integer>> possibleMove;

    private int[] numberOfChips;
    private int blackBestScore;

    public void newGame() {
        blackBestScore = 0;
        chipColor = false;
        gamePanel = new int[8][8];
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 8; ++x) {
                gamePanel[y][x] = 0;
            }
        }

        gamePanel[3][3] = 1;
        gamePanel[4][4] = 1;
        gamePanel[3][4] = 2;
        gamePanel[4][3] = 2;
        numberOfChips = new int[]{2, 2};
        possibleMove = GameAgreement.getAllPossibleCells(gamePanel, chipColor);
    }

    public int[] getNumberOfChips() {
        return numberOfChips;
    }

    /**
     * Может ли игрок сделать ход.
     */
    public boolean canMakeMove() {
        return !possibleMove.isEmpty();
    }

    /**
     * Следующий игрок.
     */
    public void changeTurn() {
        chipColor = !chipColor;
        possibleMove = GameAgreement.getAllPossibleCells(gamePanel, chipColor);
    }
    public boolean getChipColor() {
        return chipColor;
    }

    public int getBlackBestScore() {
        return blackBestScore;
    }


    /**
     * Лучшее значение.
     */
    public void setBlackBestScore(int blackBestScore) {
        if (this.blackBestScore < blackBestScore) {
            this.blackBestScore = blackBestScore;
        }
    }

    public String renderPlayingFieldIntoString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append('\t');

        for (int i = 1; i <= 8; ++i) {
            resultString.append(i);
            resultString.append('\t');
        }
        resultString.append('\n');
        for (int i = 0; i < 8; ++i) {
            resultString.append(i + 1);
            for (int j = 0; j < 8; ++j) {
                resultString.append('\t');
                ArrayList<Integer> arrayToFind = new ArrayList<>();
                arrayToFind.add(j);
                arrayToFind.add(i);

                if (possibleMove.contains(arrayToFind)) {
                    resultString.append(new String(Character.toChars(0x2714)));
                    continue;
                }
                switch (gamePanel[i][j]) {
                    case 0 -> {
                        resultString.append(new String(Character.toChars(0x25AA)));
                    }
                    case 1 -> {
                        resultString.append(new String(Character.toChars(0x1F518)));
                    }
                    case 2 -> {
                        resultString.append(new String(Character.toChars(0x26AB)));
                    }
                    default -> {
                        resultString.append('?');
                    }
                }
            }
            resultString.append('\n');
        }
        return resultString.toString();
    }

    public String makeMoveOnPosition(int positionX, int positionY, boolean isComputerTurn) {
        ArrayList<Integer> arrayToFind = new ArrayList<>();
        arrayToFind.add(positionX);
        arrayToFind.add(positionY);
        if (!possibleMove.contains(arrayToFind)) {
            return "На эту клетку нельзя ходить по правилам игры.";
        }

        int scoreChange = GameAgreement.invertEnemyChips(gamePanel, chipColor, positionX, positionY);
        if (chipColor == false) {
            numberOfChips[0] += scoreChange + 1;
            numberOfChips[1] -= scoreChange;
        } else {
            numberOfChips[0] -= scoreChange;
            numberOfChips[1] += scoreChange + 1;
        }

        gamePanel[positionY][positionX] = (chipColor == false) ? 2 : 1;
        chipColor = !chipColor;
        possibleMove = GameAgreement.getAllPossibleCells(gamePanel, chipColor);

        return "right";
    }

    public void calculateAndMakeComputerMove() {
        int[] moveCoordinates = GameAgreement.getBestMove(gamePanel, chipColor);

        makeMoveOnPosition(moveCoordinates[0], moveCoordinates[1], true);
    }
}
