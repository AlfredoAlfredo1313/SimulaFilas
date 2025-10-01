import java.util.HashMap;

public class Fila {
    public int Servers, Capacity, Status, Loss, FilaID;
    public double MinArrrival, MaxArrival, MinServe, MaxServe, TempoGlobal;
    public String nome;
    public HashMap<Integer, Double> ServerStatus = new HashMap<Integer, Double>();
    private RedeFilas Rede;

    public Fila(int FilaID, int Servers, int Capacity, double MinArrrival, double MaxArrival, double MinServe, double MaxServe, RedeFilas rede, String nome) {
        this.FilaID = FilaID;
        this.Servers = Servers;
        this.Capacity = Capacity;
        this.MinArrrival = MinArrrival;
        this.MaxArrival = MaxArrival;
        this.MinServe = MinServe;
        this.MaxServe = MaxServe;
        this.Rede = rede;
        this.nome = nome;
    }

    public void Chegada(Evento e)
    {
        MarcaTempo(e.Tempo);
        if(Status >= Capacity && Capacity > 0) {
            Loss++;
        }
        else{
            Status++;
            // Acho que podia estar fora do else mas só pra garantir
            if (Status <= Servers)
                Rede.NovoEvento(Evento.TipoDeEvento.Saida,this);
        }
        Rede.NovoEvento(Evento.TipoDeEvento.Chegada,this);
    }

    public void Saida(Evento e)
    {
        MarcaTempo(e.Tempo);
        Status--;
        if (Status >= Servers)
            Rede.NovoEvento(Evento.TipoDeEvento.Saida,this);
    }

    public void Passagem(Evento e, boolean inicio)
    {
        MarcaTempo(e.Tempo);
        if (inicio)
        {
            Status--;
            if (Status >= Servers)

                //Fila lida apenas com Chegada e Saida. E RedeFilas determina se a Saida é mesmo uma Saida ou passagem (basicamente como ja tava sendo feito)  
                //Rede.NovoEvento(Evento.TipoDeEvento.Passagem, this);
                Rede.NovoEvento(Evento.TipoDeEvento.Saida, this);
        }
        else
        {
            if(Status >= Capacity && Capacity > 0) Loss++;
            else{
                Status++;
                // Acho que podia estar fora do else mas só pra garantir
                if (Status <= Servers)
                    Rede.NovoEvento(Evento.TipoDeEvento.Saida, this);
            }
        }
    }

    public void MarcaTempo(double updatedTime)
    {
        double delta = updatedTime - TempoGlobal;
        TempoGlobal = updatedTime; 
        if(ServerStatus.containsKey(Status))
            delta += ServerStatus.get(Status);
        ServerStatus.put(Status, delta);
    }
    /*
    public void NovoEvento(Evento.TipoDeEvento tipo)
    {
        double min, max;
        if (tipo == Evento.TipoDeEvento.Chegada) {
            min = MinArrrival;
            max = MaxArrival;
        }
        else if (tipo == Evento.TipoDeEvento.Saida) {
            min = MinServe;
            max = MaxServe;
        }
        else {
            System.err.println("Tipo de evento inválido");
            return;
        }
        double timestamp = simulador.getRandom().GetNext(min, max);
        if (timestamp <= 0)
            return;
        timestamp += TempoGlobal;
        Evento saida = new Evento(FilaID, tipo, timestamp);
        simulador.NovoEvento(saida);
    }
     */

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Fila %s: G/G/%d%s TEMPO GLOBAL: %.2f\n", nome, Servers, Capacity > 0? String.format("/%d", Capacity) : "", TempoGlobal));
        ServerStatus.forEach((k, v) -> {
            String per = String.format("%.2f",v/TempoGlobal * 100);
            s.append("Status: ").append(k).append(", Tempo: ").
            append(String.format("%.2f -> ",v)).
            append(per).append("% \n");
        });
        s.append(String.format("PERDIDOS: %d\n", Loss));
        return s.toString();
    }

    public void SetRede(RedeFilas redeFilas) {
        this.Rede = redeFilas;
    }
}