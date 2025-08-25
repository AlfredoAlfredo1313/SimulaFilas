import java.util.PriorityQueue;
import java.util.Queue;

public class Fila {
    public int Servers, Capacity;
    public float MinArrrival, MaxArrival, MinServe, MaxServe;
    public PriorityQueue<Evento> Fila = new PriorityQueue<>();
}