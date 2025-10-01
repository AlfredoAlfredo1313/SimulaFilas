import java.util.HashMap;

public class FilaFactory {

    public int Servers, Capacity, FilaID;
    public double MinArrrival, MaxArrival, MinServe, MaxServe;
    private RedeFilas Rede;
    public String nome;

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }
    public void setFilaID(int filaID) {
        FilaID = filaID;
    }
    public void setMaxArrival(double maxArrival) {
        MaxArrival = maxArrival;
    }
    public void setMaxServe(double maxServe) {
        MaxServe = maxServe;
    }
    public void setMinArrrival(double minArrrival) {
        MinArrrival = minArrrival;
    }
    public void setMinServe(double minServe) {
        MinServe = minServe;
    }
    public void setRede(RedeFilas rede) {
        Rede = rede;
    }
    public void setServers(int servers) {
        Servers = servers;
    }
    public Fila getFila()
    {
        return new Fila(FilaID, Servers, Capacity, MinArrrival, MaxArrival, MinServe, MaxServe, Rede, nome);
    }
}
