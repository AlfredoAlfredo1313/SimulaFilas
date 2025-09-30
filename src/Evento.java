
public class Evento implements Comparable<Evento>
{
    public int FilaID;
    public int TargetID;
    public int RedeID;
    public enum TipoDeEvento{Chegada, Saida, Passagem}
    public TipoDeEvento tipo;
    public double Tempo;

    // Contrutor para eventos em filas NÃO em rede.
    public Evento(int FilaID, TipoDeEvento tipo, double Tempo)
    {
        this.FilaID = FilaID;
        this.tipo = tipo;
        this.Tempo = Tempo;
    }

    // Contrutor para eventos passagem
    public Evento(int FilaID, TipoDeEvento tipo, double Tempo, int Target)
    {
        this.FilaID = FilaID;
        this.tipo = tipo;
        this.Tempo = Tempo;
        this.TargetID = Target;
    }
    
    // Contrutor para eventos em filas em rede.
    public Evento(int RedeID, int FilaID, TipoDeEvento tipo, double Tempo)
    {
        this.RedeID = RedeID;
        this.FilaID = FilaID;
        this.tipo = tipo;
        this.Tempo = Tempo;
    }
    @Override
    public int compareTo(Evento o) {
        Evento e = (Evento) o;
        if (Tempo > e.Tempo)
            return 1;
        else if (Tempo < e.Tempo)
            return -1;
        return 0; 
        
    }

    @Override
    public String toString() {
        return "Rede: " + RedeID + " Fila: " + FilaID + " Tipo: "+ tipo.name() + " Tempo: " + Tempo;
    }
}