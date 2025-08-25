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
        MarcaTempo(e.Tempo);   
        Status++;
        if (Status <= Servers)
            NovoEvento(Evento.TipoDeEvento.Saida);
        NovoEvento(Evento.TipoDeEvento.Chegada);
    }

    public void Saida(Evento e)
    {
        MarcaTempo(e.Tempo);
        Status--;
        if (Status >= Servers)
            NovoEvento(Evento.TipoDeEvento.Saida);
    }

    public void MarcaTempo(double updatedTime)
    {
        double delta = updatedTime - TempoGlobal;
        TempoGlobal = updatedTime; 
        if(ServerStatus.containsKey(Status))
            delta += ServerStatus.get(Status);
        ServerStatus.put(Status, delta);
    }

    public void NovoEvento(Evento.TipoDeEvento tipo)
    {
        double min, max;
        if (tipo == Evento.TipoDeEvento.Chegada) {
            min = MinArrrival;
            max = MaxArrival;
        }
        else {
            min = MinServe;
            max = MaxServe;
        }
        double timestamp = CongruenteLinear.Instance.GetNext(min, max);
        if (timestamp <= 0)
            return;
        timestamp += TempoGlobal;
        Evento saida = new Evento(FilaID, tipo, timestamp);
        App.Eventos.add(saida);
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Fila %d: G/G/%d/%d TEMPO GLOBAL: %.2f\n", FilaID, Servers, Capacity, TempoGlobal));
        ServerStatus.forEach((k, v) -> {
            String per = String.format("%.2f",v/TempoGlobal * 100);
            s.append("Status: ").append(k).append(", Tempo: ").
            append(String.format("%.2f -> ",v)).
            append(per).append("% \n");
        });
        s.append(String.format("PERDIDOS: %d\n", Loss));
        return s.toString();
    }
}