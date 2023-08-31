import com.gilead.main.Calculator;
import com.gilead.main.Operations;
import com.gilead.main.StringCalculator;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        //Runtime Polymorphism
        Operations normalCalculator = new Calculator();
        Operations stringCalculator = new StringCalculator();
        System.out.println(normalCalculator.add(1,2));
        System.out.println(stringCalculator.add(1,2));
        System.out.println(normalCalculator.add("Hai","Calculator"));
        System.out.println(stringCalculator.add("Hai","Calculator"));
        }
    }