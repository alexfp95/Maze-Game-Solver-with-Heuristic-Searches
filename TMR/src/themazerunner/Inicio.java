/* Clase para la Casilla de Inicio */

package themazerunner;

public class Inicio 
{
    private int pos_i_;
    private int pos_j_;
    
    public void Inicio ()
    {
        pos_i_ = 0;
        pos_j_ = 0;
    }
    
    public void set (int i, int j)
    {
        pos_i_ = i;
        pos_j_ = j;
    }
    
    public int get_i ()
    {
        return pos_i_;
    }
    
    public int get_j ()
    {
        return pos_j_;
    }
}
