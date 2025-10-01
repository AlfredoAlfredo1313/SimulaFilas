import java.util.ArrayList;

public class RNGFixo implements IRandom{
    private ArrayList<Double> valores;
    private Integer currentIDX = 0;

    public RNGFixo(ArrayList<Double> a)
    {
        this.valores = a;
    }

    @Override
    public HasNext HasNext() {
        return !(currentIDX >= valores.size())? HasNext.HasNext : HasNext.Ended;
    }

    @Override
    public double GetNext(double min, double max) {
        double value = valores.get(currentIDX);
        currentIDX++;
        value = min + (max - min) * value;
        return value; 
    }
}
