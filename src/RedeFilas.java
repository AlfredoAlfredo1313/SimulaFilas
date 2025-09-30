import java.util.ArrayList;
import java.util.HashMap;

public class RedeFilas {
    public Double TempoGlobal;
    private ArrayList<Fila> Filas;
    private HashMap<Integer,Integer> Conexoes;
    private HashMap<Integer, MapaConex> MapaConexoes;
    private Simulador simulador;

    public RedeFilas(ArrayList<Fila> filas, HashMap<Integer,Integer> conexoes, Simulador sim, HashMap<Integer, MapaConex> mapaConexoes)
    {
        this.Filas = filas;
        this.Conexoes = conexoes;
        this.simulador = sim;
        this.MapaConexoes = mapaConexoes;
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

        Filas.get(e.TargetID).Passagem(e, false);
    }

    public void NovoEvento(Evento.TipoDeEvento tipo, Fila fila)
    {
        double min, max;
        int target_id = -1;
        if (tipo == Evento.TipoDeEvento.Chegada) {
            min = fila.MinArrrival;
            max = fila.MaxArrival;
        }
        else if (tipo == Evento.TipoDeEvento.Saida) {
            min = fila.MinServe;
            max = fila.MaxServe;
            //int passagem = MapaConex.GetTarget(max, 0)
            target_id = MapaConex.GetTarget(fila.FilaID, simulador.getRandom());
            if (target_id >= 0) {
                tipo = Evento.TipoDeEvento.Passagem;
            }
        }
        else {
            System.err.println("Tipo de evento inválido");
            return;
        }
        double timestamp = simulador.getRandom().GetNext(min, max);
        if (timestamp <= 0)
            return;
        timestamp += TempoGlobal;
        Evento ev;
        if(target_id >= 0)
            ev = new Evento(fila.FilaID, tipo, timestamp, target_id);
        else
            ev = new Evento(fila.FilaID, tipo, timestamp);
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
