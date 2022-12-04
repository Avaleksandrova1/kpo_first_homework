package Reversi;
import java.util.Scanner;

public class Game {
    private GamePanel gamePanel;
    private boolean gameMode;
    private static final String PLAYER_GREETING = "=^ ^= Добро пожаловать в мини-игру РЕВЕРСИ! =^ ^= ";
    private static final String HELP = """
            Для начала игры доступны следующие опции:\s
            "start" - для начала игры;\s
            "help" - для напоминания правил игры;\s
            "best" - наилучший результат игры у игрока;\s
            "end" - закончить игру.""";

    private static final String ERROR = "Неверный формат опции. Введите, пожалуйста, заново. ";
    private static final String GAME_MODES = """
            Выберите один из следующих режимов:
            1 - легкий режим игры с компьютером;\s
            2 - продвинутый режим игры с компьютером;\s
            3 - игрок против игрока.""";
    private static final String ERROR_MODES = "Введен недоступный режим. Повторите выбор режима.";
    private static final String REGULATIONS = """ 
             РЕВЕРСИ – логическая игра, рассчитанная на двух участников,\s
             которые играют на доске размером 8x8 фишками разного цвета.\s
             В игре приняты следующие нормативные правила:\s
             • при очередном ходе фишку можно ставить на свободную клетку в любом\s 
             направлении, но обязательно рядом хотя бы с одной из фишек противника;\s
             • фишка должна ставиться так, чтобы хотя бы одна из фишек противника\s
             оказалась замкнутой своими фишками. При этом замкнутые фишки противника\s
             меняют цвет и становятся своими;\s 
             • фишки могут неоднократно менять цвет, но не могут переставляться на доске;\s
             • игра заканчивается, если доска заполнена, или на ней присутствуют\s
             фишки только одного цвета, или ни один из игроков не может сделать очередной\s
             ход.\s
             Координаты поля задаются в виде двух цифр (от 1 до 8) и имеют вид:<цифра по столбцу> <цифра по строке>\s
             Ход начинает игрок с черными фишками""";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Приветствие игрока и меню.
     */

    //start game
    public void preparationGame() {
        this.gamePanel = new GamePanel();
        System.out.println(PLAYER_GREETING);
        while (true) {
            if (readyToStartPlaying()) {
                System.out.println(GAME_MODES);
                selectGameLevel();
                gamePanel.newGame();
                workWithPlayerCommands();
            } else {
                break;
            }
        }
    }

    /**
     * Опции меню игры. Запуск или завершение.
     *
     * @return true, если игрок начинает игру, в противном случае - false.
     */
    private boolean readyToStartPlaying() {
        String userOptions;
        System.out.println(HELP);
        while (true) {
            userOptions = scanner.nextLine();
            switch (userOptions) {
                case "start" -> {
                    return true;
                }
                case "end" -> {
                    return false;
                }
                case "help" -> {
                    System.out.println(REGULATIONS);
                }
                case "best" -> {
                    if (gamePanel.getBlackBestScore() == 0) {
                        System.out.println("Еще слишком рано, не было игр.");
                    } else {
                        System.out.printf("Лучший результат - %d\n" + ERROR, gamePanel.getBlackBestScore());
                    }
                }
                default -> {
                    System.out.println(ERROR);
                }
            }
        }
    }

    private void selectGameLevel() {
        String userOptions;
        while (true) {
            userOptions = scanner.nextLine();
            if (userOptions.matches("^[1-9]\\d*$")) {
                switch (Integer.parseInt(userOptions)) {
                    case 1 -> {
                        gameMode = false;
                        return;
                    }
                    case 2 -> {
                        System.out.println("Извините, но данная опция отдыхает и пока не готова");
                        System.out.println(ERROR_MODES);
                    }
                    case 3 -> {
                        gameMode = true;
                        return;
                    }
                    default -> {
                        System.out.println(ERROR_MODES);
                    }

                }
            } else {
                System.out.println(ERROR_MODES);
            }
        }
    }

    private void workWithPlayerCommands() {
        int xCoordinate;
        int yCoordinate;
        String[] userCoordinatesInput;

        while (true) {
            System.out.printf("Текущее количество очков у игроков\n Черные - %d\n Белые - %d\n ",
                    gamePanel.getNumberOfChips()[0], gamePanel.getNumberOfChips()[1]);
            System.out.println(gamePanel.renderPlayingFieldIntoString());

            // Валидация координат хода, которые ввел пользователь
            if (this.gameMode == false && gamePanel.getChipColor() == true) {
                gamePanel.calculateAndMakeComputerMove();
                if (isGameEnded()) {
                    System.out.println(gamePanel.renderPlayingFieldIntoString());
                    return;
                }
                continue;
            }
            userCoordinatesInput = scanner.nextLine().split(" ");

            // Проверка на два целых числа
            if ((userCoordinatesInput.length != 2) || (
                    !(userCoordinatesInput[0].matches("^[1-9]\\d*$") &&
                            userCoordinatesInput[1].matches("^[1-9]\\d*$"))
            )) {
                System.out.println("Некорректная команда, введите две координаты\n");
                continue;
            }

            xCoordinate = Integer.parseInt(userCoordinatesInput[0]) - 1;
            yCoordinate = Integer.parseInt(userCoordinatesInput[1]) - 1;

            String moveErrorMessage = gamePanel.makeMoveOnPosition(xCoordinate, yCoordinate, false);
            if (!"right".equals(moveErrorMessage)) {
                System.out.println(moveErrorMessage);
            }

            if (isGameEnded()) {
                System.out.println(gamePanel.renderPlayingFieldIntoString());
                return;
            }

        }
    }

    private boolean isGameEnded() {
        if (!gamePanel.canMakeMove()) {
            gamePanel.changeTurn();
            if (!gamePanel.canMakeMove()) {
                String gameResult;
                if (gamePanel.getNumberOfChips()[0] < gamePanel.getNumberOfChips()[1]) {
                    gameResult = "Победа за белыми.";
                } else if (gamePanel.getNumberOfChips()[0] > gamePanel.getNumberOfChips()[1]) {
                    gameResult = "Победа за черными.";
                } else {
                    gameResult = "Ничья";
                }
                System.out.printf("Игра окончена!\n%s со счетом %d : %d\n",
                        gameResult, gamePanel.getNumberOfChips()[0], gamePanel.getNumberOfChips()[1]);

                gamePanel.setBlackBestScore(gamePanel.getNumberOfChips()[0]);
                return true;
            }
            System.out.println("Не было возможного хода, ход переходит к другому игроку\n");
        }
        return false;
    }

}
