import java.util.ArrayList;
import java.util.PriorityQueue;

/*
            G/G/1/5, chegadas entre 2...5, atendimento entre 3...5;
            G/G/2/5, chegadas entre 2...5, atendimento entre 3…5.
            Para ambas simulações, considere inicialmente a fila vazia e o primeiro cliente chegando no tempo 2,0. Realize a simulação com 100.000 aleatórios, 
            ou seja, ao se utilizar o 100.000 aleatório, sua simulação deve se encerrar e a distribuição de probabilidades, 
            bem como os tempos acumulados para os estados da fila devem ser reportados. 
            Além disso, indique o número de perda de clientes (caso tenha havido perda) e o tempo global da simulação.
 */

public class App {
    public static PriorityQueue<Evento> Eventos;
    public static void main(String[] args) throws Exception {
        CongruenteLinear random = new CongruenteLinear(0, 100000);
        Simulador s = new Simulador(1, 1, 5, 2, 5, 3, 5, random, 2);
        while (s.HasNextStep())
        {
            s.Step();
        }
        System.out.println(s);
        return;  
    }

    // Não vai funcionar no estado atual
    public static void old_main(String[] args) {
        Fila f0 = new Fila(0, 1, 5, 2, 5, 3, 5, null);
        //Fila f1 = new Fila(0, 2, 5, 2, 5, 3, 5);
        ArrayList<Fila> filas = new ArrayList<>(java.util.Arrays.asList(f0));
        CongruenteLinear cong = new CongruenteLinear(0, 100000);
        Eventos = new PriorityQueue<>();
        Evento e1 = new Evento(0, Evento.TipoDeEvento.Chegada, 2);
        Eventos.add(e1);
        //Evento e2 = new Evento(1, Evento.TipoDeEvento.Chegada, 2);
        //Eventos.add(e2);
        
        while (cong.HasNext() && !Eventos.isEmpty()) {
            Evento e = Eventos.poll();
            Fila f = filas.get(e.FilaID);
            if(e.tipo == Evento.TipoDeEvento.Chegada)
                f.Chegada(e);
            else
                f.Saida(e);
        }

        filas.forEach(System.out::print);   
    }
}
