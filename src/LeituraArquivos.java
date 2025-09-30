import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LeituraArquivos {
    private BufferedReader fileReader;

    private Boolean initialized;
    private ArrayList<Fila> filas;
    private RedeFilas redeFilas;
    private Simulador sim;
    private HashMap<String,Integer> nomesfilas;
    private HashMap<Integer,Integer> conexoes;  // indice Fila para indice Fila
    private Integer nextID = 0;
    private IRandom rng;
    private ArrayList<Long> rng_seeds = new ArrayList<>();
    private Long rng_numbers_per_seed = -1L;
    private HashMap<String, Double> Arrivals;

    public LeituraArquivos(String caminho, Simulador sim)
    {
        this.sim = sim;
        this.nomesfilas = new HashMap<>();
        this.conexoes = new HashMap<>();
        this.Arrivals = new HashMap<>();
        this.filas = new ArrayList<>();
        try {
            this.fileReader = new BufferedReader(new FileReader(caminho));
            this.initialized = true;
            String line = this.fileReader.readLine();
            boolean started = false;
            while(line != null)
            {
                if (!started && line.contains("!PARAMETERS")) started = true;
                if (!started || line.contains("#")) 
                {
                    line = this.fileReader.readLine();
                    continue;
                }
                if (line.contains("arrivals:")) chegadasIniciais();
                if (line.contains("queues:")) filas();
                if (line.contains("network:")) rede();
                if (line.contains("rndnumbers:") ||
                    line.contains("rndnumbersPerSeed:") ||
                    line.contains("seeds:")) rng(line);
                line = this.fileReader.readLine();
                
            }
            this.redeFilas = new RedeFilas(filas, conexoes, sim, null);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            this.initialized = false;
        }
    }

    public void chegadasIniciais() throws IOException
    {
        System.out.println("Arrivals");
        String line = this.fileReader.readLine();
        while(line.contains(":"))
        {
            String name = line.split(":")[0].trim();
            double tempo = Double.parseDouble(line.split(":")[1].trim());
            if (nomesfilas.keySet().contains(name))
            {
                sim.NovoEvento(new Evento(nomesfilas.get(name), Evento.TipoDeEvento.Chegada, tempo));
            } else
                Arrivals.put(name, tempo);
            line = this.fileReader.readLine();
            if (line == null) break;
        }
        
    }

    public void filas() throws IOException
    {
        System.out.println("Filas");
        String line = this.fileReader.readLine();
        FilaFactory filaAtual = new FilaFactory();
        Boolean ready = false;
        String nome = "";
        while(line.contains(":"))
        {
            if (line.contains("servers")) filaAtual.setServers(Integer.parseInt(line.trim().split(":")[1].trim()));
            else if (line.contains("capacity")) filaAtual.setCapacity(Integer.parseInt(line.trim().split(":")[1].trim()));
            else if (line.contains("minArrival")) filaAtual.setMinArrrival(Double.parseDouble(line.trim().split(":")[1].trim()));
            else if (line.contains("maxArrival")) filaAtual.setMaxArrival(Double.parseDouble(line.trim().split(":")[1].trim()));
            else if (line.contains("minService")) filaAtual.setMinServe(Double.parseDouble(line.trim().split(":")[1].trim()));
            else if (line.contains("maxService")) filaAtual.setMaxServe(Double.parseDouble(line.trim().split(":")[1].trim()));
            else
            {
                if (ready)
                {
                    filaAtual.setFilaID(filas.size());
                    Fila novaFila = filaAtual.getFila();
                    
                    if (!nomesfilas.keySet().contains(nome))
                    {
                        nomesfilas.put(nome, filas.size());
                        filas.add(novaFila);
                        if (Arrivals.containsKey(nome))
                        {
                            sim.NovoEvento(new Evento(novaFila.FilaID, Evento.TipoDeEvento.Chegada, Arrivals.get(nome)));
                            Arrivals.remove(nome);
                        }
                        nextID++;
                    }

                    // while (filas.size() <= nomesfilas.get(nome))
                    // {
                        // filas.add(null);
                    // }
                    // filas.set(nomesfilas.get(nome), novaFila);
                    filaAtual = new FilaFactory();
                }
                else ready = true;
                nome = line.trim().split(":")[0];
            }
            line = this.fileReader.readLine();
            if (line == null) break;
        }
        if (ready)
        {
            filaAtual.setFilaID(filas.size());
            Fila novaFila = filaAtual.getFila();

            if (!nomesfilas.keySet().contains(nome))
            {
                nomesfilas.put(nome, filas.size());
                filas.add(novaFila);
                if (Arrivals.containsKey(nome))
                {
                    sim.NovoEvento(new Evento(novaFila.FilaID, Evento.TipoDeEvento.Chegada, Arrivals.get(nome)));
                    Arrivals.remove(nome);
                }
                nextID++;
            }

            // while (filas.size() <= nomesfilas.get(nome))
            // {
            //     filas.add(null);
            // }
            // filas.set(nomesfilas.get(nome), novaFila);
            filaAtual = new FilaFactory();
        }
        System.out.println(filas);
    }

    public void rede() throws IOException
    {
        System.out.println("Network");
        // Socorro
        String line = this.fileReader.readLine();
        String source = "";
        String target = "";
        Integer source_id = -1;
        Integer target_id = -1;
        Double probability = -1.0;
        Integer idx = 0;
        Boolean ready = false;
        boolean invalida = false;
            while (line.contains(":")) {
                if (line.contains("-") && !ready) ready = true;
                else if (line.contains("-"))
                {
                    if(!invalida) {
                        idx++;
                        MapaConex.SetConnection(source_id, target_id, probability);
                    }
                    invalida = false;
                    // conexoes.put(nomesfilas.get(source), nomesfilas.get(target));
                    // probabilidades.put(idx, probability);
                }
                if (line.contains("source") && !invalida){ 
                    source = line.split(":")[1].trim();
                    source_id = nomesfilas.get(source);
                    if (source_id == null) 
                        invalida = true;
                }
                else if (line.contains("target") && !invalida){ 
                    target = line.split(":")[1].trim();
                    target_id = nomesfilas.get(target);
                    if (source_id == null)
                        invalida = true;
                }
                else if (line.contains("probability") && !invalida) probability = Double.parseDouble(line.split(":")[1].trim());
                
                line = this.fileReader.readLine();
                if (line == null) break;
            }
    }

    public void rng(String line) throws IOException
    {
        System.out.println("RNG");
        if (line.contains("rndnumbers:"))
        {
            ArrayList<Double> nums = new ArrayList<>();
            line = this.fileReader.readLine();
            while (line.contains("-")) {
                nums.add(Double.parseDouble(line.split("-")[1].trim()));
                line = this.fileReader.readLine();
                if (line == null) break;
            }
            rng = new RNGFixo(nums);
        }
        else if (line.contains("rndnumbersPerSeed:"))
        {
            rng_numbers_per_seed = Long.parseLong(line.split(":")[1].trim());
        }
        else if (line.contains("seeds:"))
        {
            line = this.fileReader.readLine();
            while (line.contains("-")) {
                rng_seeds.add(Long.parseLong(line.split("-")[1].trim()));
                line = this.fileReader.readLine();
                if (line == null) break;
            }
        }
        if (!rng_seeds.isEmpty() && rng_numbers_per_seed != -1L)
        {
            rng = new CongruenteLinear(rng_seeds.toArray(new Long[1]), rng_numbers_per_seed);
        }
    }

    public boolean initialized()
    {
        return initialized;
    }

    public RedeFilas getRedeFilas() {return redeFilas;}
    public IRandom getRandom() {
        return rng;
    }
}
