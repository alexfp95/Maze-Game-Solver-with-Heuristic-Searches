/* Clase Principal del Juego */

package themazerunner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import java.util.LinkedList;

import themazerunner.Meta;
import themazerunner.Character;
import themazerunner.Inicio;
import themazerunner.Nodo;


public class Principal extends javax.swing.JFrame {
    
    /* Atributos de la clase */
    
    // Dimension del tablero inicial
    private int tamañoColumna = 20;             
    private int tamañoFila = 20;
    private int middlei = tamañoColumna/2;
    private int middlej = tamañoFila/2;
    
    // Objetos del juego
    private Meta meta_ = new Meta ();
    private Inicio inicio_ = new Inicio ();
    private Character c1 = new Character();
    private Character c2 = new Character();
    private Character c3 = new Character();
    private Character c4 = new Character();
       
    // Permite ajustar la dimension de cada casilla dependiendo del tamaño total  
    int escalable = Math.min( ((10*70)/(tamañoColumna)) , (10*70)/(tamañoFila) );
    
    public int tamañoCuadrito = escalable; 
    
    // Matriz con los datos de cada posicion del tablero
    // La correspondencia de cada valor de la mLogica se explicara en getImagen
    private int[][] mLogica = new int[tamañoColumna][tamañoFila];
    
    // Matriz de JLabels que representara de forma grafica lo que hay dentro de mLogica
    private JLabel[][] mGrafica = new JLabel[tamañoColumna][tamañoFila];
    
    
    public Principal() {
        // Inicializa los elementos del JFrame
        initComponents();
        // Personalzacion de los botones
        bGo1.setBackground(java.awt.Color.blue);
        bGo2.setBackground(java.awt.Color.red);
        bGo3.setBackground(java.awt.Color.pink);
        bGo4.setBackground(java.awt.Color.yellow);
        bSalir.setBackground(java.awt.Color.black);
        bRedim.setBackground(java.awt.Color.white);
        // Inicializacion del tablero inicial
        crearMapaLogico();
        // Muestra el tablero
        desplegarMatriz();
    }
    
    /* Creacion del tablero inicial por defecto*/
    private void crearMapaLogico()
    {
        int [][] temp = new int [tamañoColumna][tamañoFila];
        
        int numrand;
        
        for(int i=0; i<tamañoColumna; i++)
        {
            for(int j=0; j<tamañoFila; j++)
            {
                // Limitamos los bordes del tablero con muros. Estos no se podran atravesar.
                if((i==0) || (j==0) || (i==tamañoColumna-1) || (j==tamañoFila-1))
                    temp[i][j] = 1;
                else
                {
                    // Establecemos un 30% de obstaculos iniciales de forma aleatoria por el tablero.
                    numrand = ThreadLocalRandom.current().nextInt(1, 10 + 1);
                    if( (numrand == 1) || (numrand == 2) || (numrand == 3) )
                        temp[i][j] = 1;
                    else
                        temp[i][j] = 0;
                }
            }
        }
        
        // Random que nos dice en que pared aparecerá la meta (Superior, Inferior, o Laterales)
        numrand = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        
        // Pondremos en dicha pared la meta, eligiendo la posicion (i ó j) restante con otro random.
        switch(numrand)
        {
            case 0: meta_.set(ThreadLocalRandom.current().nextInt(1, tamañoColumna-2 +1), 0);
                    temp[meta_.get_i()][meta_.get_j()] = 3; 
                    temp[meta_.get_i()][meta_.get_j()+1] = 0;
                    break;
            case 1: meta_.set(ThreadLocalRandom.current().nextInt(1, tamañoColumna-2 +1), tamañoFila-1);
                    temp[meta_.get_i()][meta_.get_j()] = 3; 
                    temp[meta_.get_i()][meta_.get_j()-1] = 0;
                    break;
            case 2: meta_.set(0, ThreadLocalRandom.current().nextInt(1, tamañoFila-2 +1));
                    temp[meta_.get_i()][meta_.get_j()] = 3;
                    temp[meta_.get_i()+1][meta_.get_j()] = 0;
                    break;
            case 3: meta_.set(tamañoColumna-1, ThreadLocalRandom.current().nextInt(1, tamañoFila-2 +1));
                    temp[meta_.get_i()][meta_.get_j()] = 3; 
                    temp[meta_.get_i()-1][meta_.get_j()] = 0;
                    break;
            default: break;
        }
        
        // Punto del que partiran los jugadores
        inicio_.set(middlei, middlej);
        
        temp[inicio_.get_i()][inicio_.get_j()] = 2;  
        
        c1.set(inicio_.get_i(), inicio_.get_j()+1);
        c2.set(inicio_.get_i(), inicio_.get_j()-1);
        c3.set(inicio_.get_i()-1, inicio_.get_j());
        c4.set(inicio_.get_i()+1, inicio_.get_j());
        
        // Cada jugador avanzara poniendo piedritas de su color, y marcaran casillas 'a ciegas' con 99
        c1.set_cr(10, 99);
        c2.set_cr(20, 99);
        c3.set_cr(30, 99);
        c4.set_cr(40, 99);
        
        // Colocacion de cada jugador
        temp[c1.get_i()][c1.get_j()] = 9;
        temp[c2.get_i()][c2.get_j()] = 9;
        temp[c3.get_i()][c3.get_j()] = 9;
        temp[c4.get_i()][c4.get_j()] = 9;
        
        // Guardamos en mLogica el tablero resultante
        mLogica = temp;
    }
    
    /* A partir de la mLogica muestra la mGrafica */
    private void desplegarMatriz()
    {
        for(int i=0; i< tamañoColumna; i++)
        {
            for(int j=0; j < tamañoFila; j++)
            {
                mGrafica[i][j] = new JLabel();
                mGrafica[i][j].setOpaque(true);
                // Establece para cada Jlabel su posicion x, y, el alto, y el ancho
                mGrafica[i][j].setBounds((i*tamañoCuadrito)+15, (j*tamañoCuadrito)+15, tamañoCuadrito, tamañoCuadrito);
                
                // Marca como visible el JLabel
                mGrafica[i][j].setVisible(true);
                mGrafica[i][j].setBackground(Color.red);
                // Le añade la imagen correspondiente con la mLogica
                mGrafica[i][j].setIcon(getImagen(mLogica[i][j]));
                
                final int x = i; final int y = j;
                
                // Listener del mouse para hacer interactiva la personalizacion del mapa por el usuario
                mGrafica[i][j].addMouseListener(new MouseAdapter ()
                {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        int aux = mLogica[x][y];
                        // Se podrá cambiar la casilla clickeada si no es los límites del tablero, un personaje, o la casilla de inicio 
                        if((x!=0) && (y!=0) && (x!=tamañoColumna-1) && (y!=tamañoFila-1) && (aux != 9) && (aux != 2))
                        {
                            JLabel element = (JLabel) e.getSource();
                            /* Cambia un muro por un cesped */
                            if(aux == 1)
                            {
                                element.setIcon(getImagen(0));
                                mLogica[x][y] = 0;
                            }
                            /* Cambia un cesped por un muro*/
                            if(aux == 0)
                            {
                                element.setIcon(getImagen(1));
                                mLogica[x][y] = 1;
                            }
                            /* Desmarca una casilla 'a ciega' */
                            if(aux == 99)
                            {
                                element.setIcon(getImagen(0));
                                mLogica[x][y] = 0;
                            }
                        }
                        // Muestra por consola la casilla en cuestión para llevar un control.
                        System.out.println("X: "+x+" Y: "+y);
                    }
                });
                
                // Añade la mGrafica al panel para su visualizacion
                panelContenedor.add(mGrafica[i][j]);
            }
        }
    }
    
    // Permite la correspondencia visual con los datos de mLogica
    public ImageIcon getImagen(int tipo)
    {
        // Cada imagen la redimensionamos de acuerdo al tamañoCuadrito
        switch(tipo)
        {
            // Cesped
            case 0: ImageIcon floor = new ImageIcon(getClass().getResource("/imagenes/suelo.png"));
                    return new ImageIcon(floor.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Muro
            case 1: ImageIcon wall = new ImageIcon(getClass().getResource("/imagenes/pared.png"));
                    return new ImageIcon(wall.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Casilla de inicio
            case 2: ImageIcon start = new ImageIcon(getClass().getResource("/imagenes/start.png"));
                    return new ImageIcon(start.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Meta
            case 3: ImageIcon meta = new ImageIcon(getClass().getResource("/imagenes/end.png"));
                    return new ImageIcon(meta.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Camino estandar
            case 4: ImageIcon camino = new ImageIcon(getClass().getResource("/imagenes/camino.png"));
                    return new ImageIcon(camino.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Personaje
            case 9: ImageIcon ch1 = new ImageIcon(getClass().getResource("/imagenes/player.png"));
                    return new ImageIcon(ch1.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Camino de piedritas del PJ1
            case 10: ImageIcon camino1 = new ImageIcon(getClass().getResource("/imagenes/camino_c1.png"));
                    return new ImageIcon(camino1.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Camino de piedritas del PJ2
            case 20: ImageIcon camino2 = new ImageIcon(getClass().getResource("/imagenes/camino_c2.png"));
                    return new ImageIcon(camino2.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Camino de piedritas del PJ3
            case 30: ImageIcon camino3 = new ImageIcon(getClass().getResource("/imagenes/camino_c3.png"));
                    return new ImageIcon(camino3.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Camino de piedritas del PJ4
            case 40: ImageIcon camino4 = new ImageIcon(getClass().getResource("/imagenes/camino_c4.png"));
                    return new ImageIcon(camino4.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Casilla 'a ciega'
            case 99: ImageIcon bad = new ImageIcon(getClass().getResource("/imagenes/camino_bad.png"));
                    return new ImageIcon(bad.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Muerte
            case 100: ImageIcon dead = new ImageIcon(getClass().getResource("/imagenes/dead.png"));
                    return new ImageIcon(dead.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
            // Por defecto: cesped
            default:ImageIcon def = new ImageIcon(getClass().getResource("/imagenes/suelo.png"));
                    return new ImageIcon(def.getImage().getScaledInstance(tamañoCuadrito, tamañoCuadrito, Image.SCALE_SMOOTH));
        }
    }
    
    /* Reinicia la mLogica para darle nueva dimension */
    public void redimensionaVal ()
    {
        mLogica = null; mGrafica = null;
        mLogica = new int[tamañoColumna][tamañoFila];
        mGrafica = new JLabel[tamañoColumna][tamañoFila];
        
        tamañoCuadrito = Math.min( ((10*70)/(tamañoColumna)) , (10*70)/(tamañoFila) );
        middlei = tamañoColumna/2; 
        middlej = tamañoFila/2;
        
        panelContenedor.removeAll();
        crearMapaLogico();
        desplegarMatriz();
    }
    
    /* Metodo de resolucion del laberinto. Recibe una lista con el camino realizado, y un id con el que cada PJ marca su camino con piedritas diferentes */
    public Boolean resolver (LinkedList<Nodo> camino, int id_pj)
    {
        // Usamos el método en Escalada. Miramos en cada uno de sus vecinos cuál es el mejor.
        int min_i = 0; 
        int min_j = 0;
        int min_mh = 0;
        int aux; 
        // Esta  variable nos marca cuantas opciones válidas tendremos al examinar nuestros vecinos
        int block = 0;
        // Variable permitirá establecer el primer vecino que lea como el mínimo para luego compararlo con el resto
        Boolean ft = true;
        // Variables para identificar que vecino vicitamos en cada iteracion del metodo resolver
        Boolean i = false; Boolean a = false; Boolean d = false; Boolean b = false;
        
        // Si la lista esta vacia, quiere decir que el PJ no encontró un camino factible
        if(camino.size() == 0)
        {
            System.out.println("El personaje no ha encontrado una solucion factible.");
            // Marca en la posicion de ese PJ el icono de su muerte
            if(id_pj == 10)
            {
                mLogica[c1.get_i()][c1.get_j()] = 100;
                mGrafica[c1.get_i()][c1.get_j()].setIcon(getImagen(mLogica[c1.get_i()][c1.get_j()]));
                panelContenedor.add(mGrafica[c1.get_i()][c1.get_j()]);
            }
            if(id_pj == 20)
            {
                mLogica[c2.get_i()][c2.get_j()] = 100;
                mGrafica[c2.get_i()][c2.get_j()].setIcon(getImagen(mLogica[c2.get_i()][c2.get_j()]));
                panelContenedor.add(mGrafica[c2.get_i()][c2.get_j()]);
            }
            if(id_pj == 30)
            {
                mLogica[c3.get_i()][c3.get_j()] = 100;
                mGrafica[c3.get_i()][c3.get_j()].setIcon(getImagen(mLogica[c3.get_i()][c3.get_j()]));
                panelContenedor.add(mGrafica[c3.get_i()][c3.get_j()]);
            }
            if(id_pj == 40)
            {
                mLogica[c4.get_i()][c4.get_j()] = 100;
                mGrafica[c4.get_i()][c4.get_j()].setIcon(getImagen(mLogica[c4.get_i()][c4.get_j()]));
                panelContenedor.add(mGrafica[c4.get_i()][c4.get_j()]);
            }
            // Termina la iteracion de resolver con ese PJ
            return true;
        }
        
        // Extrae el último nodo de la lista (la última casilla a la que movió)
        Nodo A = camino.removeLast();
        
        // Miraremos los vecinos en el sentido: Arriba, Dcha, Abajo, Izda
        // Tendremos en cuenta la casilla siempre que no sea: Visitadas, Muro, Casilla de Inicio, Casilla 'a ciega', Personaje
        if((!A.vecAr_) && (mLogica[A.get_i()-1][A.get_j()] != id_pj) && (mLogica[A.get_i()-1][A.get_j()] != 1) && (mLogica[A.get_i()-1][A.get_j()] != 2) && (mLogica[A.get_i()-1][A.get_j()] != 99) && (mLogica[A.get_i()-1][A.get_j()] != 9))
        {
            // Si es la primera vez, tendremos en cuenta ese vecino como el mejor para luego compararlo con el resto (si los hay)
            if(ft)
            {
                min_i = A.get_i()-1;
                min_j = A.get_j();
                // Calculamos su 'distancia a Manhattan
                min_mh = Math.abs((A.get_i()-1) - meta_.get_i()) + Math.abs(A.get_j() - meta_.get_j());
                ft = false;
                // Llevamos un control por consola del movimiento del PJ
                System.out.println("Voy a izquierda");
            }
            // Aumentamos el nº de casillas válidas
            block++;
            // Actualizamos los vecinos, poniendo en true el visitado y óptimo en este momento.
            i = false; a = true; d = false; b = false;
            
        }
        
        // Misma descripcion, mirando otro vecino
        if((!A.vecDe_) && (mLogica[A.get_i()][A.get_j()+1] != id_pj) && (mLogica[A.get_i()][A.get_j()+1] != 1) && (mLogica[A.get_i()][A.get_j()+1] != 2) && (mLogica[A.get_i()][A.get_j()+1] != 99) && (mLogica[A.get_i()][A.get_j()+1] != 9))
        {
            if(ft)
            {
                min_i = A.get_i();
                min_j = A.get_j()+1;
                min_mh = Math.abs(A.get_i() - meta_.get_i()) + Math.abs((A.get_j()+1) - meta_.get_j());
                ft = false;
                System.out.println("Voy a abajo");
                i = false; a = false; d = true; b = false;
            }
            // Al no ser la primera vez que encontramos un vecino válido, procedemos a ver cual es el mejor de ambos
            else
            {
                // Obtenemos la distancia a Manhattan del posible nuevo vecino óptimo
                aux = Math.abs(A.get_i() - meta_.get_i()) + Math.abs((A.get_j()+1) - meta_.get_j());
                // Miramos si es mejor que el vecino óptimo que ya teníamos, y si lo es procedemos a actualizar.
                if(aux < min_mh)
                {
                    min_i = A.get_i();
                    min_j = A.get_j()+1;
                    min_mh = aux;
                    System.out.println("Voy a abajo");
                    i = false; a = false; d = true; b = false;
                }
            }
            block++;
        }
        
        // Misma descripcion, mirando otro vecino
        if((!A.vecAb_) && (mLogica[A.get_i()+1][A.get_j()] != id_pj) && (mLogica[A.get_i()+1][A.get_j()] != 1) && (mLogica[A.get_i()+1][A.get_j()] != 2) && (mLogica[A.get_i()+1][A.get_j()] != 99) && (mLogica[A.get_i()+1][A.get_j()] != 9))
        {
            if(ft)
            {
                min_i = A.get_i()+1;
                min_j = A.get_j();
                min_mh = Math.abs((A.get_i()+1) - meta_.get_i()) + Math.abs(A.get_j() - meta_.get_j());
                ft = false;
                i = false; a = false; d = false; b = true;
                System.out.println("Voy a derecha");
            }
            else
            {
                aux = Math.abs((A.get_i()+1) - meta_.get_i()) + Math.abs(A.get_j() - meta_.get_j());
                if(aux < min_mh)
                {
                    min_i = A.get_i()+1;
                    min_j = A.get_j();
                    min_mh = aux;
                    i = false; a = false; d = false; b = true;
                    System.out.println("Voy a derecha");
                }
            }
            block++;
            
        }
        
        // Misma descripcion, mirando otro vecino
        if((!A.vecIz_) && (mLogica[A.get_i()][A.get_j()-1] != id_pj) &&(mLogica[A.get_i()][A.get_j()-1] != 1) && (mLogica[A.get_i()][A.get_j()-1] != 2) && (mLogica[A.get_i()][A.get_j()-1] != 99) && (mLogica[A.get_i()][A.get_j()-1] != 9))
        {
            if(ft)
            {
                min_i = A.get_i();
                min_j = A.get_j()-1;
                min_mh = Math.abs(A.get_i() - meta_.get_i()) + Math.abs((A.get_j()-1) - meta_.get_j());
                ft = false;
                i = true; a = false; d = false; b = false;
                System.out.println("Voy a arriba");
            }
            else
            {
                aux = Math.abs(A.get_i() - meta_.get_i()) + Math.abs((A.get_j()-1) - meta_.get_j());
                if(aux < min_mh)
                {
                    min_i = A.get_i();
                    min_j = A.get_j()-1;
                    min_mh = aux;
                    i = true; a = false; d = false; b = false;
                    System.out.println("Voy a arriba");
                }
            }
            block++;
            
        }
        
        // Busco cual es el vecino visitado para establecerlo en el nodo
        if(i)
            A.vecIz_ = true;
        if(a)
            A.vecAr_ = true;
        if(d)
            A.vecDe_ = true;
        if(b)
            A.vecAb_ = true;
        
        // Si he llegado a la casilla Meta se termina de resolver
        if((mLogica[min_i][min_j] == 3))
           return true;
        
        // En caso contrario
        else
        {
                // Si no ha habido ningun vecino posible, es que nos encontramos en una casilla a ciega
                // Este nodo no volverá a incluirse a la lista ya que no puede continuar, y de esta manera
                // en la siguiente iteración de resolver, se extraerá el nodo anterior a este para mirar 
                // si tiene mas vecinos a los que visitar y de esta manera retrocedemos en nuestro camino.
                if(block == 0)
                {
                    mLogica[A.get_i()][A.get_j()] = 99;
                    mGrafica[A.get_i()][A.get_j()].setIcon(getImagen(mLogica[A.get_i()][A.get_j()]));
                    panelContenedor.add(mGrafica[A.get_i()][A.get_j()]);
                    // Mostramos por consola que Retrocedemos.
                    System.out.println("Retrocedo");
                }
                
                // Si tenemos vecino para visitar
                else
                {
                    // Establecemos en el vecino al que vamos el id del PJ para poner las piedritas
                    if(mLogica[min_i][min_j] != id_pj)
                        mLogica[min_i][min_j] = id_pj;
                    else
                        mLogica[min_i][min_j] = 99;
                    
                    // Devolvemos a la lista la casilla que examinamos
                    camino.add(A);
                    
                    // Actualizamos gráficamente
                    mGrafica[min_i][min_j].setIcon(getImagen(mLogica[min_i][min_j]));
                    panelContenedor.add(mGrafica[min_i][min_j]);
                    
                    // Añadimos a la lista el nuevo vecino al que nos movemos para examinarlo en la siguiente iteracion del método
                    camino.add(new Nodo (min_i, min_j));
                    // Llevamos un control del movimiento del personaje
                    System.out.println("El pj se mueve a: "+min_i+" " +min_j);
                }
             
            // Si no ha terminado el laberiento, que continue iterando.    
            return false;
        }
    }
        
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContenedor = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        bSalir = new javax.swing.JButton();
        textM = new javax.swing.JTextField();
        labelM = new javax.swing.JLabel();
        labelN = new javax.swing.JLabel();
        textN = new javax.swing.JTextField();
        bRedim = new javax.swing.JButton();
        bGo1 = new javax.swing.JButton();
        bGo2 = new javax.swing.JButton();
        bGo3 = new javax.swing.JButton();
        bGo4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelContenedorLayout = new javax.swing.GroupLayout(panelContenedor);
        panelContenedor.setLayout(panelContenedorLayout);
        panelContenedorLayout.setHorizontalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        panelContenedorLayout.setVerticalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );

        panelMenu.setBackground(new java.awt.Color(255, 255, 255));

        bSalir.setBackground(new java.awt.Color(255, 255, 255));
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Salir");
        bSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSalirActionPerformed(evt);
            }
        });

        textM.setText("20");
        textM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMActionPerformed(evt);
            }
        });

        labelM.setText("Introducir M:");

        labelN.setText("Introducir N:");

        textN.setText("20");
        textN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textNActionPerformed(evt);
            }
        });

        bRedim.setText("Redimensionar");
        bRedim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRedimActionPerformed(evt);
            }
        });

        bGo1.setBackground(new java.awt.Color(255, 255, 255));
        bGo1.setForeground(new java.awt.Color(255, 255, 255));
        bGo1.setText("Go");
        bGo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGo1ActionPerformed(evt);
            }
        });

        bGo2.setBackground(new java.awt.Color(255, 255, 255));
        bGo2.setForeground(new java.awt.Color(255, 255, 255));
        bGo2.setText("Go");
        bGo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGo2ActionPerformed(evt);
            }
        });

        bGo3.setBackground(new java.awt.Color(255, 255, 255));
        bGo3.setForeground(new java.awt.Color(255, 255, 255));
        bGo3.setText("Go");
        bGo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGo3ActionPerformed(evt);
            }
        });

        bGo4.setBackground(new java.awt.Color(255, 255, 255));
        bGo4.setForeground(new java.awt.Color(255, 255, 255));
        bGo4.setText("Go");
        bGo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGo4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(labelM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(labelN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(bRedim)
                .addGap(215, 215, 215)
                .addComponent(bGo1)
                .addGap(18, 18, 18)
                .addComponent(bGo2)
                .addGap(18, 18, 18)
                .addComponent(bGo3)
                .addGap(18, 18, 18)
                .addComponent(bGo4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bSalir)
                .addGap(26, 26, 26))
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelM)
                    .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelN)
                    .addComponent(textN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSalir)
                    .addComponent(bRedim)
                    .addComponent(bGo1)
                    .addComponent(bGo2)
                    .addComponent(bGo3)
                    .addComponent(bGo4))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelContenedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        /* Casilla 'M: ' para guardar una nueva dimension. Se activa al pulsar ENTER en dicha casilla. OJO: No redimensiona hasta que no pulses 'Redimensionar' */
        private void textMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMActionPerformed
            String texto = textM.getText();
            try
            {
                tamañoColumna = Integer.parseInt(texto);
            }
            catch (NumberFormatException e) 
            {
                System.err.println("Error al introducir M");
                e.printStackTrace();
            }
            // Tenemos un control por consola de que ha sido modificada la variable
            System.out.println("M: "+tamañoColumna);
        }//GEN-LAST:event_textMActionPerformed
    
    /* Casilla 'N: ' para obtener nueva dimension. Se activa al pulsar ENTER en dicha casilla. OJO: No redimensiona hasta que no pulses 'Redimensionar' */
    private void textNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textNActionPerformed
        String texto = textN.getText();
        try
        {
            tamañoFila = Integer.parseInt(texto);
        }
        catch (NumberFormatException e) 
        {
            System.err.println("Error al introducir N");
            e.printStackTrace();
        }
        // Tenemos un control por consola de que ha sido modificada la variable
        System.out.println("N: "+tamañoFila);
    }//GEN-LAST:event_textNActionPerformed
    
    /* Boton 'Salir' que cierra la ventana y el juego */
    private void bSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_bSalirActionPerformed
    
    /* Redimensiona la matriz con los datos que tengan las variables tamañoColumna y tamañoFila en ese momento */
    private void bRedimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRedimActionPerformed
        setVisible(false);
        redimensionaVal();
        setVisible(true);
    }//GEN-LAST:event_bRedimActionPerformed

    /* Boton 'Go' del PJ1 */
    private void bGo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGo1ActionPerformed
        // Usamos una lista para realizar su camino
        LinkedList camino = new LinkedList<Nodo>();
        
        System.out.println("PJ 1");
        // El nodo de inicio de dicha lista será la posicion inicial de ese PJ
        Nodo pj1 = new Nodo (c1.get_i(),c1.get_j());
        // Pone piedritas en su casilla inicial para partir
        mLogica[c1.get_i()][c1.get_j()] = 10;
        // Actualiza dicha casilla visualmente
        mGrafica[c1.get_i()][c1.get_j()].setIcon(getImagen(mLogica[c1.get_i()][c1.get_j()]));
        panelContenedor.add(mGrafica[c1.get_i()][c1.get_j()]);
        // Se inserta el inicio en la lista
        camino.push(pj1);
        // Hasta que el personaje no termine de resolver el laberinto, lo estará intentando.
        while(!resolver(camino,10));
    }//GEN-LAST:event_bGo1ActionPerformed

    /* Boton 'Go' del PJ2. La descripcion del método es la misma que para el PJ1. */
    private void bGo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGo2ActionPerformed
        LinkedList camino = new LinkedList<Nodo>();
        
        System.out.println("PJ 2");
        Nodo pj2 = new Nodo (c2.get_i(),c2.get_j());
        mLogica[c2.get_i()][c2.get_j()] = 20;
        mGrafica[c2.get_i()][c2.get_j()].setIcon(getImagen(mLogica[c2.get_i()][c2.get_j()]));
        panelContenedor.add(mGrafica[c2.get_i()][c2.get_j()]);
        camino.push(pj2);
        while(!resolver(camino,20));
    }//GEN-LAST:event_bGo2ActionPerformed
    
    /* Boton 'Go' del PJ3. La descripcion del método es la misma que para el PJ1. */
    private void bGo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGo3ActionPerformed
        LinkedList camino = new LinkedList<Nodo>();
        
        System.out.println("PJ 3");
        Nodo pj3 = new Nodo (c3.get_i(),c3.get_j());
        mLogica[c3.get_i()][c3.get_j()] = 30;
        mGrafica[c3.get_i()][c3.get_j()].setIcon(getImagen(mLogica[c3.get_i()][c3.get_j()]));
        panelContenedor.add(mGrafica[c3.get_i()][c3.get_j()]);
        camino.push(pj3);
        while(!resolver(camino,30));
    }//GEN-LAST:event_bGo3ActionPerformed
    
    /* Boton 'Go' del PJ3. La descripcion del método es la misma que para el PJ1. */
    private void bGo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGo4ActionPerformed
        LinkedList camino = new LinkedList<Nodo>();
        
        System.out.println("PJ 4");
        Nodo pj4 = new Nodo (c4.get_i(),c4.get_j());
        mLogica[c4.get_i()][c4.get_j()] = 40;
        mGrafica[c4.get_i()][c4.get_j()].setIcon(getImagen(mLogica[c4.get_i()][c4.get_j()]));
        panelContenedor.add(mGrafica[c4.get_i()][c4.get_j()]);
        camino.push(pj4);
        while(!resolver(camino,40));
    }//GEN-LAST:event_bGo4ActionPerformed
    
    /* Main*/
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Hace visible en JFrame
                new Principal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bGo1;
    private javax.swing.JButton bGo2;
    private javax.swing.JButton bGo3;
    private javax.swing.JButton bGo4;
    private javax.swing.JButton bRedim;
    private javax.swing.JButton bSalir;
    private javax.swing.JLabel labelM;
    private javax.swing.JLabel labelN;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JTextField textM;
    private javax.swing.JTextField textN;
    // End of variables declaration//GEN-END:variables
}
