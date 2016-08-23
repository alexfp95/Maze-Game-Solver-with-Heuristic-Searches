/* Clase para los Nodos de la Lista */

package themazerunner;

public class Nodo 
{
    private int x_;
    private int y_;
    public Boolean vecIz_;
    public Boolean vecAr_;
    public Boolean vecDe_;
    public Boolean vecAb_;

    Nodo (int x, int y)
    {
        x_ = x;
        y_ = y;
        vecIz_ = false;
        vecAr_ = false;
        vecDe_ = false;
        vecAb_ = false;
    }
    
    public int get_i ()
    {
        return x_;
    }
    
    public int get_j ()
    {
        return y_;
    }
}
