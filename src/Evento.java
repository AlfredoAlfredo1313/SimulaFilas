
public class Evento implements Comparable
{
    public int FilaID;
    public enum TipoDeEvento{Chegada, Saida}
    public TipoDeEvento tipo;
    public double Tempo;
    public Evento(int FilaID, TipoDeEvento tipo, double Tempo)
    {
        this.FilaID = FilaID;
        this.tipo = tipo;
        this.Tempo = Tempo;
    }
    @Override
    public int compareTo(Object o) {
        Evento e = (Evento) o;
        if (Tempo > e.Tempo)
            return 1;
        else if (Tempo < e.Tempo)
            return -1;
        return 0; 
        
    }
}