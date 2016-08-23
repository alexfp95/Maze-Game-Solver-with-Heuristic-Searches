/* Clase para los Personajes */

package themazerunner;

public class Character 
{
    private int pos_i_;
    private int pos_j_;
    private int camino_;
    private int regreso_;
    
    public void Character (int i, int j)
    {
        pos_i_ = i;
        pos_j_ = j;
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
    
    public int get_cam ()
    {
        return camino_;
    }
    
    public int get_reg ()
    {
        return regreso_;
    }
    
    public void set_cr (int a, int b)
    {
        camino_ = a;
        regreso_ = b;
    }
}
