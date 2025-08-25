import java.util.PriorityQueue;

public class App {
    public static PriorityQueue<Evento> Eventos;
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Eventos = new PriorityQueue<>();
        Evento e = new Evento(0, null, 0);
        Eventos.add(e);

    }
}
