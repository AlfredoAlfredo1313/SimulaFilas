import java.util.ArrayList;
import java.util.HashMap;

public class RedeFilas {
    public Double TempoGlobal;
    private ArrayList<Fila> Filas;
    private HashMap<Integer,Integer> Conexoes;
    private Simulador simulador;

    public RedeFilas(ArrayList<Fila> filas, HashMap<Integer,Integer> conexoes, Simulador sim)
    {
        this.Filas = filas;
        this.Conexoes = conexoes;
        this.simulador = sim;
        // Feio mas pra teste
        for (Fila fila : filas) {
            fila.SetRede(this);
        }
    }

    public Fila getFila(int filaID) {
        return Filas.get(filaID);
    }

    public void Chegada(Evento e)
    {
        TempoGlobal = e.Tempo;
        Filas.get(e.FilaID).Chegada(e);
    }

    public void Saida(Evento e)
    {
        TempoGlobal = e.Tempo;
        Filas.get(e.FilaID).Saida(e);
    }

    public void Passagem(Evento e) {
        TempoGlobal = e.Tempo;
        // Chama o método da fila de início da passagem
        Filas.get(e.FilaID).Passagem(e, true);
        // Chama o método pro outro lado da passagem, presume que é uma conexão 1 pra 1
        Filas.get(Conexoes.get(e.FilaID)).Passagem(e, false);
    }

    public void NovoEvento(Evento.TipoDeEvento tipo, Fila fila)
    {
        double min, max;
        if (tipo == Evento.TipoDeEvento.Chegada) {
            min = fila.MinArrrival;
            max = fila.MaxArrival;
        }
        else if (tipo == Evento.TipoDeEvento.Saida) {
            min = fila.MinServe;
            max = fila.MaxServe;
            if (Conexoes.containsKey(fila.FilaID)) tipo = Evento.TipoDeEvento.Passagem;
        }
        else if (tipo == Evento.TipoDeEvento.Passagem) {
            min = fila.MinArrrival;
            max = fila.MaxArrival;
        }
        else {
            System.err.println("Tipo de evento inválido");
            return;
        }
        double timestamp = simulador.getRandom().GetNext(min, max);
        if (timestamp <= 0)
            return;
        timestamp += TempoGlobal;
        Evento ev = new Evento(fila.FilaID, tipo, timestamp);
        simulador.NovoEvento(ev);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Fila f : Filas) {
            s.append(f.toString());
        }
        return s.toString();
    }

    public void End() {
        for (Fila fila : Filas) {
            fila.MarcaTempo(TempoGlobal);
        }
    }

}
