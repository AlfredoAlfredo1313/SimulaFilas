import java.util.ArrayList;
import java.util.PriorityQueue;


public class Simulador {
    
    private ArrayList<Fila> Filas;
    private PriorityQueue<Evento> Eventos;
    private IRandom Random;

    // Construtor temporário até termos leitura de valores de arquivo, presume que todas Filas são iguais,
    // que a primeira chegada é na fila 0, tem 0 trânsito entre Filas, só serve pra uma fila decentemente.
    public Simulador(int NumeroDeFilas, int Servers, int Capacity, double MinArrrival, double MaxArrival, 
                     double MinServe, double MaxServe, IRandom Random, double FirstArrivalTime)
    {
        this.Filas = new ArrayList<>();
        this.Eventos = new PriorityQueue<>();
        this.Random = Random;
        for (int i = 0; i < NumeroDeFilas; i++) {
            Fila f = new Fila(i, Servers, Capacity, MinArrrival, MaxArrival, MinServe, MaxServe, this);
            Filas.add(f);
        }
        Eventos.add(new Evento(0, Evento.TipoDeEvento.Chegada, FirstArrivalTime));

    }

    public Simulador(String SimFilePath)
    {

    }

    public boolean HasNextStep()
    {
        return Random.HasNext() && !Eventos.isEmpty();
    }

    public void Step()
    {
        Evento e = Eventos.poll();
        Fila f = Filas.get(e.FilaID);
        if(e.tipo == Evento.TipoDeEvento.Chegada)
            f.Chegada(e);
        else
            f.Saida(e);
    }

    public void NovoEvento(Evento e) {
        Eventos.add(e);
    }

    public IRandom getRandom() {
        return Random;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Fila f : Filas) {
            s.append(f.toString());
        }
        return s.toString();
    }
}
