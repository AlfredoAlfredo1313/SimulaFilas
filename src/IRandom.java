/*
 *  Interface que define o que uma classe de números pseudo-aleatórios precisa ter
 *  A ideia é facilitar a implementação de números fixos lidos de arquivo
 */

public interface IRandom {
    public boolean HasNext();
    public double GetNext(double min, double max);
}
