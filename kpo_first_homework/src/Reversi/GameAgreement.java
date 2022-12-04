package Reversi;

import java.util.ArrayList;
import java.util.Arrays;

public final class GameAgreement {
    private static boolean isPositionOnTheBoard(int positionX, int positionY) {
        return (positionX >= 0) && (positionX < 8) && (positionY >= 0) && (positionY < 8);
    }

    private static boolean isSequenceCorrectByDirection(int[][] playingField, int positionX, int positionY,
                                                        int horizontalDirection, int verticalDirection,
                                                        int enemyChipColor, int unionChipColor) {
        if ((horizontalDirection == verticalDirection) && (verticalDirection == 0)) {
            return false;
        }

        int tmpXPosition = positionX + horizontalDirection;
        int tmpYPosition = positionY + verticalDirection;
        while (isPositionOnTheBoard(tmpXPosition, tmpYPosition) &&
                (playingField[tmpYPosition][tmpXPosition] == enemyChipColor)) {
            tmpYPosition += verticalDirection;
            tmpXPosition += horizontalDirection;
        }
        return (isPositionOnTheBoard(tmpXPosition, tmpYPosition) && playingField[tmpYPosition][tmpXPosition] == unionChipColor
                && !((tmpXPosition == positionX + horizontalDirection) && (tmpYPosition == positionY + verticalDirection)));
    }

    private GameAgreement() {
    }

    private static double calculateChipPossible(int[][] playingField,
                                                int positionX, int positionY,
                                                int enemyChipColor) {
        int unionChipColor = (enemyChipColor == 2) ? 1 : 2;

        double resultSum = 0;

        if (positionX == 0 || positionX == 7) {
            resultSum += 0.4;
        }

        if (positionY == 0 || positionY == 7) {
            resultSum += 0.4;
        }

        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                if (isSequenceCorrectByDirection(playingField, positionX, positionY, x, y, enemyChipColor, unionChipColor)) {
                    continue;
                }
                int tmpXPosition = positionX + x;
                int tmpYPosition = positionY + y;

                while (isPositionOnTheBoard(tmpXPosition, tmpYPosition) &&
                        (playingField[tmpYPosition][tmpXPosition] == enemyChipColor)) {
                    tmpYPosition += y;
                    tmpXPosition += x;

                    if (tmpXPosition == 7 || tmpXPosition == 0 || tmpYPosition == 7 || tmpYPosition == 0) {
                        ++resultSum;
                    }
                    resultSum++;
                }
            }
        }
        return resultSum;
    }

    public static ArrayList<ArrayList<Integer>> getAllPossibleCells(int[][] playingField, boolean colorOfMove) {
        ArrayList<ArrayList<Integer>> resultPossibleCells = new ArrayList<>();

        int enemyChipColor = (colorOfMove == false) ? 1 : 2;
        int unionChipColor = (colorOfMove == false) ? 2 : 1;

        for (int xAvailablePosition = 0; xAvailablePosition < 8; ++xAvailablePosition) {
            for (int yAvailablePosition = 0; yAvailablePosition < 8; ++yAvailablePosition) {
                // Зачем просматривать ячейку, на которой и так кто-то стоит
                if (playingField[yAvailablePosition][xAvailablePosition] != 0) {
                    continue;
                }
                boolean atLeastOneDirectionFound = false;
                for (int xDirection = -1; xDirection <= 1; ++xDirection) {
                    for (int yDirection = -1; yDirection <= 1; ++yDirection) {
                        if (isSequenceCorrectByDirection(playingField, xAvailablePosition, yAvailablePosition, xDirection, yDirection, enemyChipColor, unionChipColor)) {
                            resultPossibleCells.add(new ArrayList<>(Arrays.asList(xAvailablePosition, yAvailablePosition)));
                            atLeastOneDirectionFound = true;
                            break;
                        }
                    }
                    if (atLeastOneDirectionFound) {
                        break;
                    }
                }
            }
        }

        return resultPossibleCells;
    }
    public static int invertEnemyChips(int[][] playingField, boolean colorOfMove, int positionX, int positionY) {
        int enemyChipColor = (colorOfMove == false) ? 1 : 2;
        int unionChipColor = (colorOfMove == false) ? 2 : 1;

        int invertedChipsAmount = 0;
        for (int xDirection = -1; xDirection <= 1; ++xDirection) {
            for (int yDirection = -1; yDirection <= 1; ++yDirection) {
                if (isSequenceCorrectByDirection(playingField, positionX, positionY, xDirection, yDirection, enemyChipColor, unionChipColor)) {
                    int tmpXPosition = positionX + xDirection;
                    int tmpYPosition = positionY + yDirection;
                    while (isPositionOnTheBoard(tmpXPosition, tmpYPosition) &&
                            (playingField[tmpYPosition][tmpXPosition] == enemyChipColor)) {
                        playingField[tmpYPosition][tmpXPosition] = unionChipColor;
                        invertedChipsAmount++;
                        tmpYPosition += yDirection;
                        tmpXPosition += xDirection;
                    }
                }
            }
        }
        return invertedChipsAmount;
    }

    public static int[] getBestMove(int[][] playingField, boolean colorOfMove) {
        ArrayList<ArrayList<Integer>> possibleCellsToMove = getAllPossibleCells(playingField, colorOfMove);
        int[] resultBest = new int[2];
        double resultBestScore = -10000.0;
        if (possibleCellsToMove.isEmpty()) {
            return new int[]{-1, -1};
        }

        double currentScore;
        double maxEnemyScore;
        for (ArrayList<Integer> possibleCell : possibleCellsToMove) {
            maxEnemyScore = 0.0;
            currentScore = calculateChipPossible(
                    playingField, possibleCell.get(0), possibleCell.get(1),
                    colorOfMove == false ? 1 : 2) - maxEnemyScore;

            if (currentScore > resultBestScore) {
                resultBest[0] = possibleCell.get(0);
                resultBest[1] = possibleCell.get(1);
                resultBestScore = currentScore;
            }
        }

        return resultBest;
    }
}

