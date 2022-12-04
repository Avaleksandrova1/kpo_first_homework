import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        String[] array = new String[5];
        array[0] = "Александр Кучук";
        array[1] = "Иван Иванов";
        array[2] = "Александр Иванов";
        array[3] = "Андрей Иванов";
        array[4] = "Иван Кучук";
        String[] key = new String[5];
        int[] mark = new int[5];
        System.out.println("^_^--Who answer in class?--^_^");
        Scanner in = new Scanner(System.in);
        while (true) {
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            switch (line) {
                case "/h":
                    System.out.println("1. /r - choose random student");
                    System.out.println("2. /l - list of student with grades");
                    break;
                case "/r":
                    int x;
                    boolean fl = false;
                    while (fl!=true) {
                        System.out.print("Отвечает ");
                        x = (int)(Math.random()*((4-0)+1));
                        if (key[x]!="y"){
                            fl = true;
                            Scanner s = new Scanner(System.in);
                            int num = s.nextInt();
                            mark[x] = num;
                            key [x] = "y";
                        }
                    }
                case "/l":
                    for (int i = 0; i < 5; ++i){
                        if (key[i] == "y") {
                            System.out.print(array[i] + mark[i]);
                        }
                    }
                    System.exit(0);
            }
        }

    }
}