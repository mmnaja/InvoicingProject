import java.util.ArrayList;
import java.util.Scanner;

public class Utility {

    public static int getChoice(ArrayList<Integer> selections) {
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        try {
            int selection = Integer.parseInt(choice);
            if (selections.contains(selection)) {
                return selection;
            } else {
                System.out.println("Provided number is not a valid choice.");
                getChoice(selections);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a number with the correct choice.");
            getChoice(selections);
        }
        return 0;
    }
}
