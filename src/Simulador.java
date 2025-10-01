import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;



public class Simulador {
    
    private RedeFilas Rede;
    private PriorityQueue<Evento> Eventos;
    private IRandom Random;
    private ArrayList<Double> GlobalTimesPerSeed;
    private Double LastGlobalTime = .0;

    // Construtor temporário até termos leitura de valores de arquivo, presume que todas Filas são iguais,
    // que a primeira chegada é na fila 0, tem 0 trânsito entre Filas, só serve pra uma fila decentemente.
    public Simulador(int NumeroDeFilas, int Servers, int Capacity, double MinArrrival, double MaxArrival, 
                     double MinServe, double MaxServe, IRandom Random, double FirstArrivalTime)
    {
        ArrayList<Fila> filas = new ArrayList<>();
        GlobalTimesPerSeed = new ArrayList<>();
        this.Eventos = new PriorityQueue<>();
        this.Random = Random;
        for (int i = 0; i < NumeroDeFilas; i++) {
            Fila f = new Fila(i, Servers, Capacity, MinArrrival, MaxArrival, MinServe, MaxServe, null, "");
            filas.add(f);
        }
        this.Rede = new RedeFilas(filas, null, this, null);
        Eventos.add(new Evento(0, Evento.TipoDeEvento.Chegada, FirstArrivalTime));
    }

    // Construtor temporário SOMENTE PARA TESTES DO M6, trocar por leitura de arquivo o mais cedo possível
    public Simulador(ArrayList<Fila> filas, HashMap<Integer,Integer> conexoes, Integer fila_de_entrada, IRandom Random)
    {
        GlobalTimesPerSeed = new ArrayList<>();
        this.Eventos = new PriorityQueue<>();
        this.Random = Random;
        this.Rede = new RedeFilas(filas, conexoes, this, null);
        Eventos.add(new Evento(0,fila_de_entrada, Evento.TipoDeEvento.Chegada, 1.5));
    }

    public Simulador(String SimFilePath)
    {
        GlobalTimesPerSeed = new ArrayList<>();
        this.Eventos = new PriorityQueue<>();
        LeituraArquivos la = new LeituraArquivos(SimFilePath, this);
        this.Rede = la.getRedeFilas();
        this.Random = la.getRandom();
    }

    public boolean HasNextStep()
    {
        IRandom.HasNext hn = Random.HasNext(); 
        if (hn == IRandom.HasNext.NextSeed) {
            GlobalTimesPerSeed.add(Rede.TempoGlobal - LastGlobalTime);
            LastGlobalTime = Rede.TempoGlobal;
        }
        return hn != IRandom.HasNext.Ended && !Eventos.isEmpty();
    }

    public void Step()
    {
        Evento e = Eventos.poll();
        if(e.tipo == Evento.TipoDeEvento.Chegada)
            Rede.Chegada(e);
        else if (e.tipo == Evento.TipoDeEvento.Saida)
            Rede.Saida(e);
        else if (e.tipo == Evento.TipoDeEvento.Passagem)
            Rede.Passagem(e);
        else
            System.err.println("Tipo de Evento inválido");
    }

    public void End() {
        Rede.End();
    }

    public void NovoEvento(Evento e) {
        Eventos.add(e);
    }

    public IRandom getRandom() {
        return Random;
    }

    public void PrintEventos()
    {
        for (Evento evento : Eventos) {
            System.out.println(evento);
        }
    }

    public void PrintFilas()
    {
        System.out.println(Rede);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("--------------------------------------------\n");
        s.append(Rede.toString());
        s.append("--------------------------------------------\n");
        s.append(String.format("TEMPO GLOBAL MEDIO POR SEMENTE %.4f", GlobalTimesPerSeed.stream().reduce(.0, (a, b) -> a + b)/GlobalTimesPerSeed.size()));
        return s.toString();
    }
}
