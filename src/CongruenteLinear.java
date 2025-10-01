import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class CongruenteLinear implements IRandom{
    
    Long[] seeds;
    long current_seed;
    int current_seed_index = 0;
    long numbers_per_seed;
    long count = 0;
    long total_count;
    long a = 1140671485;
    long c = 12820163;
    long M = 16777216;

    public static CongruenteLinear Instance;

    public CongruenteLinear(Long[] seeds, long numbers_per_seed)
    {
        this.seeds = seeds;
        this.current_seed = seeds[0];
        this.numbers_per_seed = numbers_per_seed;
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

    public HasNext HasNext()
    {
        if (total_count >= (numbers_per_seed * seeds.length))
            return HasNext.Ended;
        if (count >= numbers_per_seed)
            return HasNext.NextSeed;
        return HasNext.HasNext;
    }

    public double GetNext(double min, double max)
    {
        count++;
        total_count++;
        if(total_count >= (numbers_per_seed * seeds.length))
            return -1;
        if(count > numbers_per_seed){
            if(current_seed_index >= seeds.length)
                return -1;
            count = 1;
            current_seed_index++;
            current_seed = seeds[(int)(current_seed_index)];
        }
        current_seed = ((current_seed * a + c)%M);
        double value = (double)current_seed/M;
        value = min + (max - min) * value;
        return value; 
    }

}