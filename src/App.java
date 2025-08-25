import java.util.PriorityQueue;

public class App {
    public static PriorityQueue<Evento> Eventos;
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Eventos = new PriorityQueue<>();
        Evento e = new Evento(0, null, 0);
        Eventos.add(e);
        CongruenteLinear cong = new CongruenteLinear(0, 100);
        double val;
        while (true) {
            val = cong.congruenteLerp(3, 5);
            if (val <= 0) 
                break;
            e = new Evento(0, null, val);
            Eventos.add(e);
            
        }
        while (!Eventos.isEmpty()) {
            System.out.printf("%.2f\n", Eventos.poll().Tempo);
        }

    }
}
