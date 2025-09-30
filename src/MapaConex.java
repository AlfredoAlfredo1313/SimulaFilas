import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MapaConex {
    //Integer ID Destino, Float Probabilidade
    public int SourceID;
    public ArrayList<DestinoProbabilidade> DestinosProbabilades;
    public HashSet<Integer> Targets;
    public double ProbabilidadeSum = 0;
    public static HashMap<Integer, MapaConex> ConnectionsPool = new HashMap<>();

    public static int GetTarget(int source_id, IRandom rand)
    {
        MapaConex con = ConnectionsPool.get(source_id);
        if (con != null) 
            return con.GetTarget(rand);
        return -1;
    }

    //Se conexao nao existe, cria e registra
    public static MapaConex GetConnection(int sourceID)
    {
        MapaConex con = ConnectionsPool.get(sourceID);
        if(con != null)
            return con;
        con = new MapaConex(sourceID);
        ConnectionsPool.put(sourceID, con);
        return con;
    }

    //Conexão é consiederada falha se targetID ja foi registrado ou se somatorio de probabilidade já excede 1
    public static boolean SetConnection(int sourceID, int targetID, double probabilidade)
    {
        MapaConex con = GetConnection(sourceID);
        if(con.Targets.contains(targetID))
            return false;
        return con.SetConnection(targetID, probabilidade);
    }

    public int GetTarget(IRandom rand)
    {
        if (DestinosProbabilades.isEmpty())
            return -1;
        double valor = rand.GetNext(0, 1);
        for (DestinoProbabilidade destinoProbabilidade : DestinosProbabilades) {
            if(destinoProbabilidade.Probabilidade >= valor)
                //PASSA PRA PROXIMA
                return destinoProbabilidade.Destino;
        }
        //SAI
        return -1;
    }

    public boolean SetConnection(int targetID, double probabilidade)
    {
        ProbabilidadeSum += probabilidade;
        if (ProbabilidadeSum > 1.0)
            return false;
        Targets.add(targetID);
        DestinosProbabilades.add(new DestinoProbabilidade(targetID, ProbabilidadeSum));
        DestinosProbabilades.sort(null);
        return true;
    }

    public MapaConex(int sourceID)
    {
        this.SourceID = sourceID;
        Targets = new HashSet<>();
        DestinosProbabilades = new ArrayList<>();
    }
}

class DestinoProbabilidade implements Comparable<DestinoProbabilidade> 
{
    public int Destino;
    public double Probabilidade;
    public DestinoProbabilidade(int destino, double probabilidade)
    {
        this.Destino = destino;
        this.Probabilidade = probabilidade;
    }

    @Override
    public int compareTo(DestinoProbabilidade o) {
        if (Probabilidade >= o.Probabilidade)
            return 1;
        else
            return -1; 
    }
}
