import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class CongruenteLinear implements IRandom{
    
    long seed;
    long max_generated;
    long count = 0;
    long a = 1140671485;
    long c = 12820163;
    long M = 16777216;

    public static CongruenteLinear Instance;

    public CongruenteLinear(long seed, long max_generated)
    {
        this.seed = seed;
        this.max_generated = max_generated;
        Instance = this;
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

    public boolean HasNext()
    {
        return count < max_generated;
    }

    public double GetNext(double min, double max)
    {
        count++;
        if(count >= max_generated)
            return -1;
        seed = ((seed * a + c)%M);
        double value = (double)seed/M;
        value = min + (max - min) * value;
        return value; 
    }
}