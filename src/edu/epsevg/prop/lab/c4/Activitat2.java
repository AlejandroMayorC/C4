/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.epsevg.prop.lab.c4;

/**
 *
 * @author VLLAN
 */
public class Activitat2
  implements Jugador, IAuto
{
  private String nom;
  final private int infinito = 100000000; //Valor màxim de la heurística, representaria el infinit
  private int color; /*La inicialitzem quan fem el primer moviment, aquesta es determina depenent de si som
                    jugador1 = 0 o jugador 2 = 1;*/
  private int depth; //La inicialitzem quan cridin a la classe
  private int tiradas;
  public Activitat2()
  {
    nom = "Nombre2";
  }
  
  public int moviment(Tauler t, int color)
  {
    //int col = (int)(t.getMida() * Math.random());
    this.color = color; //Guardem el color que ens passen com a paràmetre
    System.out.println(color);
    tiradas = 0;
    tiradas ++;
    int mejorMovimiento = MinMax(t, depth);
    System.out.println("S'ha decit tirar a la columna "+mejorMovimiento+" havent explorat jugades"+tiradas);
    return mejorMovimiento;
  }
  
  public Activitat2(int depth){
      this.depth = depth;
  }
  
  public String nom()
  {
    return nom;
  }
  
/*
  Algorisme de MinMax aplicat al joc del c4
  
  t = Tauler actual sobre el que fem el moviment
  depth = profunditat màxima que explorem per fer el moviment
  */

public int MinMax(Tauler t, int depth){
        int col = 0;
        Integer valor = -infinito-1;
        int alfa = -infinito;
        int beta = infinito;
        for (int i = 0; i < t.getMida(); ++i){
            if(t.movpossible(i)){
                Tauler auxiliar = new Tauler(t); //Creem una copia del tauler actual
                auxiliar.afegeix(i,color); //Fem el moviment que hem comprovat com a possible
                int min = valorMinim(auxiliar, i, alfa, beta, depth-1);
                if (valor < min){
                    col = i;
                    valor = min;
                }
                if (beta < valor){
                    return col;
                }
                alfa = Math.max(valor,alfa);
            }
        }
        return col;
    }
   
    /**
    * Mètode que ens ajuda a calcular la funció de minmax donant-nos el màxim
    *
    t = Tauler actual sobre el que fem el moviment
    col = columna sobre la qual s'ha fet l'ultima jugada
    alfa = valor de alfa de la poda
    beta = valor de beta de la poda.
    depth = profunditat del arbre que mira les jugades possibels
    */
    public int valorMàxim(Tauler t, int col, int alfa, int beta, int depth){
        if(t.solucio(col, -color)) //Si el moviment a la columna i és solució del contrari
            return -infinito;
        if(depth > 0){ //Si encara no hem arribat a les fulles
            Integer valor = -infinito-1;
            for (int i = 0; i < t.getMida(); ++i){
                if(t.movpossible(i)){
                    Tauler auxiliar = new Tauler(t);
                    auxiliar.afegeix(i,color); ////Creem un tauler auxiliar per ajudar-nos amb l'algorisme
                    valor = Math.max(valor, valorMinim(auxiliar,i, alfa, beta, depth-1));
                    if (beta < valor){
                        return valor;
                    }
                    alfa = Math.max(valor,alfa);
                }
            }
            return valor;
        }else{ //Si estem a una fulla que no és solució del rival mirem la heuristica
            return calculHeuristica(t, col);
        }
        
    }
    /**
    * Mètode que ens ajuda a calcular la funció de minmax donant-nos el mínim
    *
    t = Tauler actual sobre el que fem el moviment
    col = columna sobre la qual s'ha fet l'ultima jugada
    alfa = valor de alfa de la poda
    beta = valor de beta de la poda.
    depth = profunditat del arbre que mira les jugades possibels
    */
    public int valorMinim(Tauler t, int col, int alfa, int beta, int depth){
        if(t.solucio(col, color))   //Si tenim que el moviment a la col i és solució per nosaltres tornem +infinit 
            return infinito;
        if(depth > 0){//Si encara no hem arribat a les fulles
            Integer valor = infinito-1;
            for (int i = 0; i < t.getMida(); ++i){
                if(t.movpossible(i)){
                    Tauler auxiliar = new Tauler(t); //Creem un tauler auxiliar per ajudar-nos amb l'algorisme
                    auxiliar.afegeix(i,-color);
                    valor = Math.min(valor, valorMàxim(auxiliar,i, alfa, beta, depth-1));
                    if (valor < alfa){
                        return valor; 
                    }
                    beta = Math.min(valor,beta);
                }
            }
            return valor;
        }
        else{ //Si estem a una fulla que no és solució del rival mirem la heuristica
            return calculHeuristica(t, col); /*Nomès entra si depth = 0, i per tant, que hem arribat a la profunditat indicada
            Hem de tenir en compte que partim d'una profunditat != 0, per exemple, 2 i per cada iteració de minmax reduim 
            en 1 el valor de la variable depth. Per tant, com ha de pasar nomès calculem la funció heurística a les fulles
            del nostre arbre de minmax*/
        }
    }
    
    /* Mètode encarregat de fer el càlcul de la heuristica que usem per fer l'algorisme de MinMax
    t = Tauler actual sobre el que calculem els valors heurístics
    */
    public int calculHeuristica(Tauler t, int col){
        tiradas ++;
        int resultat = 0; //definim el resultat com a 0 -> No es millor per cap jugador
        /*if(t.solucio(col, color)){ //Si guanyem nosaltres la solució posa + infinito
            return infinito;
        }
        else if(t.solucio(col,-color)){ //Si guanya el contrari posa - infinito
            return -infinito;
        }*/
        //System.out.println(t.getMida());
        for(int aux = 0; aux < t.getMida(); aux++){
            resultat += quantitatEnColumna(t, aux);
            resultat += quantitatEnFila(t, aux);
            
            //System.out.println("Calcul de la heuristica en las columnes = "+resultat);
            
            
                        
        }
        resultat += quantitatEnDiagonal(t);
            
        //System.out.println(resultat);
        return resultat;
    }
    /* Métode que s'encarrega de fer la implementació d'una de les parts de la heurística
    S'encarrega de comprovar quantes fitxes tenim, ja bé siguin nostres o del rival,alineades a partir de la columna donada
    
    t = tauler actual que tenim
    col = columna on estem comprovant quantes fitxes alineades tenim
    */
    public int quantitatEnColumna(Tauler t, int col){
        int seguides = 0;
        int fitxa1 = 0;
        int fitxa = 0;
        /*Inicialitzem columna Analisi desde el valor màxim de getMida(), ja que mirem desde adalt cap abaix
        si tenim alguna fitxa en cada posició. Es a dir, comencem a la columna d'alçada 6, i mirem si tenim fitxa
        si no tenim baixem a la 5 per comprovar-ne si hi ha alguna fitxa. Seguiriem executant fins arribar a la columna 0
        
        */
        for(int filaAnalisi = 0 ; filaAnalisi < t.getMida(); filaAnalisi ++){
            if(filaAnalisi == 0){
                fitxa1 = t.getColor(filaAnalisi, col); //Mirem de quin color es la fitxa que es troba a la posició filaAnalisi, col
                if(fitxa1 == 0){ //Si no tenim cap fitxa a la columna retornem
                    return 0;
                }
                else{ //Si la columna 1 te fitxa sumem el valor de fitxa1 al #fitxes seguides
                    seguides += fitxa1;
                }
            }
            else{ //Si no estem a la fila 0 aleshores vol dir que tenim fitxes
                 fitxa = t.getColor(filaAnalisi, col);//comprovem si tenim fitxa a la fila i col pasades
                 if(fitxa != 0){ //Si tenim fitxa
                     if(fitxa == fitxa1){
                         seguides += fitxa;
                     }
                     else{
                         //fitxa1 = fitxa;
                         //seguides = 0;
                         break; //Si la fitxa és d'un color diferent sortim
                     }
                     if(seguides > 3 || seguides < -3){ //Si tenim que seguides > 3 vol dir que guanya el jugador 1
                                                        //Si seguides és < vol dir que guanya el jugador 2
                        seguides = infinito * fitxa1; 
                        return seguides;
                     }
                 }
            }
        } 

        //System.out.println(seguides);
        return seguides; //seguides sera valor + si té més fitxes de jugador 1 consecutives
                         //seguides sera valor - si té més fitxes de jugador 2 consecutives
    }
    
    /* Métode que s'encarrega de fer la implementació d'una de les parts de la heurística
    S'encarrega de comprovar quantes fitxes tenim, ja bé siguin nostres o del rival,alineades a partir de la fila donada
    
    t = tauler actual que tenim
    fila = fila on estem comprovant quantes fitxes alineades tenim
    */
    public int quantitatEnFila(Tauler t, int fila){
        int seguides = 0; //Contador de la quantitat de fitxes que tenim seguides
        int resultat = 0;
        int fitxaAnalitzant = 0; //Indica quin és el color de la fitxa que estem analitzant
        int fitxaAux = 0;
        int forat = 0; //Contador de espais Buits
        int fitxaActual = 0;
        for(int columnaAnalisi = 0; columnaAnalisi < t.getMida(); columnaAnalisi++){
            fitxaActual = t.getColor(fila, columnaAnalisi);
            if(fitxaActual == 0){ 
            //No tenim fitxa a la posició determinada per (fila, columnaAnalisi)
                if(fitxaAnalitzant != 0){ 
                //Fitxa que analitzem té color
                    if(seguides + forat > 3){
                        //Si seguides sumen + 3 guanyem nosaltres o el rival, per tant no val la pena seguir mirant
                        //Si forat > 3 encara tenim 4 espais i per tant, es pot seguir jugant i per això retornem el valor següent:
                        resultat += seguides * color * fitxaAnalitzant;
                    }
                    //Si no
                    fitxaAux = fitxaAnalitzant; //recordem la fitxa anterior que estabem analitzant
                    fitxaAnalitzant = 0; //S'ha tallat la ratxa de fitxes seguides, per tant, no tenim cap fitxa incial
                    forat = 1; //Tenim un forat
                }
                else{//fitxa1 no te color, és a dir, no tenim cap fitxa inicial
                    forat ++;
                    if(seguides + forat > 3){
                    //Si seguides sumen + 3 guanyem nosaltres o el rival, per tant no val la pena seguir mirant
                    //Si forat > 3 encara tenim 4 espais i per tant, es pot seguir jugant i per això retornem el valor següent:
                        resultat += seguides * color * fitxaAux;
                    }   
                }    
            }
            else{
                //Tenim una fitxa per la posició determinada per (fila,columnaAnalisi)
                if(fitxaActual == fitxaAnalitzant){
                    //Si la fitxa actual és del mateix color que la fitxa que estem analitzant:
                    seguides ++;
                    if(seguides > 3){ //Si tenim seguides > 3 vol dir que ja tenim un 4 en ratlla 
                        return fitxaAnalitzant*infinito;
                    }
                }
                else if(fitxaAnalitzant == 0){ 
                //Si la fitxa que estem analitzant encara no té color, li donem el de la fitxa actual
                    fitxaAnalitzant = fitxaActual;
                    seguides = 1;
                }
                else{
                    //Cas on fitxaAcutal es de diferent color que la fitxaAnalitzant
                    if(seguides + forat > 3){
                    //Si seguides sumen + 3 guanyem nosaltres o el rival, per tant no val la pena seguir mirant
                    //Si forat > 3 encara tenim 4 espais i per tant, es pot seguir jugant i per això retornem el valor següent:
                       resultat += seguides * color * fitxaAnalitzant;
                    }
                    fitxaAnalitzant = fitxaActual;
                    seguides = 1;
                    forat = 0;
                }
                
            }
        }
        //System.out.println(resultat);
        return resultat;
    }
    
    public int quantitatEnDiagonal(Tauler t){
        int resultat = 0;
        int mida = t.getMida();
        // Evaluar diagonales de arriba izquierda a abajo derecha y viceversa
        for (int k = 0; k < 2 * mida - 1; k++) {
            resultat += evaluarDiagonal(t, k, true); // Diagonal de arriba izquierda a abajo derecha
            resultat += evaluarDiagonal(t, k, false); // Diagonal de arriba derecha a abajo izquierda
        }
        return resultat;
    }

    private int evaluarDiagonal(Tauler t, int k, boolean leftToRight){
        int mida = t.getMida();
        int seguides = 0;
        int puntuacio = 0;
        int lastColor = 0;

        int startY = Math.max(0, k - mida + 1);
        int endY = Math.min(k, mida - 1);

        for (int y = startY; y <= endY; y++) {
            int x = leftToRight ? (k - y) : (mida - 1 - (k - y));
            int fitxaActual = t.getColor(x, y);

            if(fitxaActual == lastColor && fitxaActual != 0) {
                seguides++;
            } else {
                puntuacio += calcularPuntuacio(seguides, lastColor);
                seguides = (fitxaActual != 0) ? 1 : 0;
                lastColor = fitxaActual;
            }
        }
        puntuacio += calcularPuntuacio(seguides, lastColor); // Añadir puntuación para la última secuencia
        return puntuacio;
    }

    private int calcularPuntuacio(int seguides, int color){
        if(seguides >= 4) {
            return color * infinito; // Ganar o perder el juego
        }
        return color * seguides * seguides; // Puntuar según el número de fichas seguidas y el color
    }

}