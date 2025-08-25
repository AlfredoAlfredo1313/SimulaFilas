
public class Evento implements Comparable
{
    public int FilaID;
    public enum TipoDeEvento{Chegada, Saida}
    public TipoDeEvento tipo;
    public float Tempo;
    public Evento(int FilaID, TipoDeEvento tipo, float Tempo)
    {
        this.FilaID = FilaID;
        this.tipo = tipo;
        this.Tempo = Tempo;
    }
    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
}