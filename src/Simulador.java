import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;


public class Simulador {
    
    private ArrayList<RedeFilas> Redes;
    private PriorityQueue<Evento> Eventos;
    private IRandom Random;

    // Construtor temporário até termos leitura de valores de arquivo, presume que todas Filas são iguais,
    // que a primeira chegada é na fila 0, tem 0 trânsito entre Filas, só serve pra uma fila decentemente.
    public Simulador(int NumeroDeFilas, int Servers, int Capacity, double MinArrrival, double MaxArrival, 
                     double MinServe, double MaxServe, IRandom Random, double FirstArrivalTime)
    {
        this.Redes = new ArrayList<>();
        ArrayList<Fila> filas = new ArrayList<>();
        this.Eventos = new PriorityQueue<>();
        this.Random = Random;
        for (int i = 0; i < NumeroDeFilas; i++) {
            Fila f = new Fila(i, Servers, Capacity, MinArrrival, MaxArrival, MinServe, MaxServe, null);
            filas.add(f);
        }
        Redes.add(new RedeFilas(filas, null, this));
        Eventos.add(new Evento(0, Evento.TipoDeEvento.Chegada, FirstArrivalTime));
    }

    // Construtor temporário SOMENTE PARA TESTES DO M6, trocar por leitura de arquivo o mais cedo possível
    public Simulador(ArrayList<Fila> filas, HashMap<Integer,Integer> conexoes, Integer fila_de_entrada, IRandom Random)
    {
        this.Redes = new ArrayList<>();
        this.Eventos = new PriorityQueue<>();
        this.Random = Random;
        Redes.add(new RedeFilas(filas, conexoes, this));
        Eventos.add(new Evento(0,fila_de_entrada, Evento.TipoDeEvento.Chegada, 1.5));
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
        RedeFilas r = Redes.get(e.RedeID);
        if(e.tipo == Evento.TipoDeEvento.Chegada)
            r.Chegada(e);
        else if (e.tipo == Evento.TipoDeEvento.Saida)
            r.Saida(e);
        else if (e.tipo == Evento.TipoDeEvento.Passagem)
            r.Passagem(e);
        else
            System.err.println("Tipo de Evento inválido");
    }

    public void End() {
        for (RedeFilas r : Redes) {
            r.End();
        }
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
        s.append("--------------------------------------------\n");
        for (RedeFilas r: Redes) {
            s.append(r.toString());
            s.append("--------------------------------------------\n");
        }
        return s.toString();
    }
}
