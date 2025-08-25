import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Locale.Category;

public class CongruenteLinear {
    
    public static void main(String[] args) {
        try {
            FileWriter myFile = new FileWriter("resultado_congruente_linear_seed_"+args[0]+".txt");
            congruenteLinear(Integer.parseInt(args[0]), myFile);
            myFile.close();
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }
    public static void congruenteLinear(long seed, FileWriter fileWriter) {
        NumberFormat formatter = new DecimalFormat("0.0000000000000000000");
        long a = 1140671485;
        long c = 12820163;
        long M = 16777216;
        int counter = 0;
        while (counter < 1000) {
            seed = ((seed * a + c)%M);
            // System.out.println(seed);
            // if (seed > M) {
            //     continue;
            // }
            // if ((double)seed/M == Double.MAX_VALUE || (double)seed/M == Double.MIN_VALUE) {
            //     continue;
            // }
            try {
                fileWriter.write(""+formatter.format((double)seed/M)+"\n");
            } catch (IOException e) {
                return;
            }
            counter++;
        }
    }
}