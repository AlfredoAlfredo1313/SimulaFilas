import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Queue;

public class Fila {
    public int Servers, Capacity, Status, Loss, FilaID;
    public double MinArrrival, MaxArrival, MinServe, MaxServe, TempoGlobal;
    public HashMap<Integer, Double> ServerStatus = new HashMap<Integer, Double>();


    public Fila(int FilaID, int Servers, int Capacity, double MinArrrival, double MaxArrival, double MinServe, double MaxServe) {
        this.FilaID = FilaID;
        this.Servers = Servers;
        this.Capacity = Capacity;
        this.MinArrrival = MinArrrival;
        this.MaxArrival = MaxArrival;
        this.MinServe = MinServe;
        this.MaxServe = MaxServe;
    }

    public void Chegada(Evento e)
    {
        if(Status >= Capacity) {
            Loss++;
            return;
        }
        double updatedTime = e.Tempo;
        double delta = updatedTime - TempoGlobal; 
        if(ServerStatus.containsKey(Status))
            delta += ServerStatus.get(Status);
        ServerStatus.put(Status, delta);
        Status++;
        if (Status < Servers)
        {
            
            Evento saida = new Evento(Capacity, Evento.TipoDeEvento.Saida, delta);
        }
    }
}