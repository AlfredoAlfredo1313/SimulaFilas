import java.util.ArrayList;
import java.util.HashMap;
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
        // CongruenteLinear random = new CongruenteLinear(0, 100000);
        //Simulador s = new Simulador(1, 1, 5, 2, 5, 3, 5, random, 2);
        Simulador s = new Simulador("model(2).yml");
        s.PrintEventos();
        s.PrintFilas();



        // ArrayList<Fila> filas = new ArrayList<>();
        // HashMap<Integer,Integer> conexoes = new HashMap<>();
        // filas.add(new Fila(0, 2, 3, 1, 4, 3, 4, null));
        // filas.add(new Fila(1, 1, 5, -1, -1, 2, 3, null));
        // conexoes.put(0,1);
        // Simulador s = new Simulador(filas, conexoes, 0, random);

        while (s.HasNextStep())
        {
            s.Step();
        }
        s.End();
        System.out.println(s);
        // return;  
    }

}
