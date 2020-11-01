import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Pablo García Fernández
 * @file Bsqueda.java
 * @objective - Se encarga de realizar la búsqueda tabú para el problema del viajante.
 *
 */
public class Busqueda {
    private final int MAX = 100;  //maximo iteraciones sin mejora
    private HashMap<Integer,Coordenadas> costes;
    private ArrayList<Integer> mejor;     //mejor solucion
    private int iteracionMejor;
    private ArrayList<Integer> estado;    //solucion actual (de la cual vamos a estudiar su contorna)
    private ListaTabu listaTabu;
    private int i_intercambiada;
    private int j_intercambiada;
    private int iteracciones;
    private int reinicios;

    //para reinicialización con matriz frecs
    private double DMAX;
    private double DMIN;
    private int[][] frecs = null;
    private final double mu = 1;  //hiperparámetro de penalización ciudades consecutivas.

    //para alternancia estratégica.


    /**
     * Para iniciar la búsqueda con estado inicial aleatorio.
     * @param costes, Para cada ciudad sus posicion en coordenadas (radianes)
     */
    public Busqueda(HashMap<Integer, Coordenadas> costes) {
        this.costes = costes;
        estado = this.solucionGreedy();   //la solución greedy no se refleja sobre la matriz frecs
        mejor = (ArrayList<Integer>) estado.clone();

        //borrar
        System.out.println("Partiendo estrategia avara: ");
        System.out.println(estado);
        System.out.println("Coste: " + costeRecorrido(estado) + "\n\n");
        //hasta aquí

        listaTabu = new ListaTabu();
        i_intercambiada=-1;
        j_intercambiada=-1;
        iteracciones=1;
        reinicios=1;
        iteracionMejor=0;

        //Inicializamos Dmax y Dmin.
        DMAX = -1;
        DMIN = 100000000;
        double coste_aux;
        for(int i=0; i<costes.size();i++){
            for(int j=0;j<costes.size();j++){
                if(i!=j && i>j) {
                    coste_aux = costes.get(i).distancia(costes.get(j));
                    if (coste_aux < DMIN) {
                        DMIN = coste_aux;
                    }
                    if (coste_aux > DMAX) {
                        DMAX = coste_aux;
                    }
                }
            }
        }
        System.out.println("Coste maximo: " + DMAX);
        System.out.println("Coste mínimo: " +DMIN);

        //creamos la matriz freecs
        frecs = new int[costes.size()][costes.size()];
    }

    public ArrayList<Integer> buscar(){

        ArrayList<Integer> devolver = new ArrayList<>();

        int IteraccionesSinMejora = 0;

        //Iniciamos la búsqueda.  10001
        while(iteracciones!=10001){
            /**
             * esto solo está para informar cuanto falta
             */
            if(iteracciones%500==0){
                System.out.println("Vamos en: " + iteracciones);
            }

            /**
             * REINICIO!!!!!!
             *   - oscilación estratégica.
             */
            if(IteraccionesSinMejora%MAX==0){
                //reinicio por intensificación
                if(IteraccionesSinMejora==MAX || IteraccionesSinMejora==2*MAX || IteraccionesSinMejora==3*MAX){  //probamos con margen de error
                    estado = (ArrayList<Integer>) mejor.clone();
                }
                //reinicio por diversificación. Ponemos Iteracciones Sin Mejora a 0.
                else{
                    estado = this.solucionGreedy();

                }
                System.out.println("REINICIO tipo: \n");
                listaTabu.reiniciarTabla();
                reinicios++;
                System.out.println("Nuevo estado inicial: ");
                imprimeRecorrido(estado);
                System.out.println("\tCOSTE: " + costeRecorrido(estado));
            }

            //Nuevo estado pasa a ser el mejor de los vecinos.
            estado = exploraContorna(estado);
            //añadimos el intercambio realizado.
            listaTabu.addProhibicion(i_intercambiada,j_intercambiada);

            if(costeRecorrido(estado) < costeRecorrido(mejor)){
                mejor = (ArrayList<Integer>) estado.clone();
                IteraccionesSinMejora=0;
                iteracionMejor = iteracciones;
            }
            else{
                IteraccionesSinMejora++;
            }
            iteracciones++;

            /**
             * Al finalizar la iteración, ajustamos los valores de la matriz freecs
             * Recorremos la solución actual e incrementamos en 1 la casilla i j de la matriz
             */
            frecs[0][estado.get(0)] += 1;
            frecs[estado.get(0)][0] += 1;
            for(int i=0, j=1; i<estado.size()-1; i++,j++){
                frecs[estado.get(i)][estado.get(j)] += 1;
                frecs[estado.get(j)][estado.get(i)] += 1;
            }
        }


        System.out.println("\nMEJOR SOLUCION:");
        imprimeRecorrido(mejor);
        System.out.println("\tCOSTE (km): " + costeRecorrido(mejor));
        System.out.println("\tITERACION: " + iteracionMejor + "\n\n\n");


        devolver.add(costeRecorrido(mejor));
        devolver.add(iteracionMejor);
        return(devolver);
    }

    /**
     * exploramos la contorna de solución y devolvemos su mejor vecino
     * @param solucion estado a generar vecindario
     * @return, mejor vecino (el de menor coste)
     */
    private ArrayList<Integer> exploraContorna(ArrayList<Integer> solucion){
        ArrayList<Integer> aux;   //va tomando el valor de los distintos vecinos
        ArrayList<Integer> vecino = null;  //almacena el mejor vecino
        for(int i=0;i<solucion.size();i++) {
            for (int j = 0; j < solucion.size(); j++) {
                if(i!=j && i>j){
                    /** generamos vecino a partir de estado,
                     * y evaluamos su coste. Si es mejor lo almacenamos como
                     * mejor vecino. Al finalizar el bucle el mejor vecino pasa a ser estado */
                    if(!listaTabu.contieneProhibicion(i,j)) {
                        aux = (ArrayList<Integer>) solucion.clone();
                        aux.set(i, solucion.get(j));
                        aux.set(j, solucion.get(i));

                        if(vecino == null || costeRecorrido((aux))<costeRecorrido(vecino)){
                            vecino = (ArrayList<Integer>) aux.clone();
                            i_intercambiada = i;
                            j_intercambiada = j;
                        }
                    }
                }
            }
        }
        //si al finalizar el proceso vecino, sigue siendo null, lo ponemos a solucion (no hubo ningun vecino).
        if(vecino==null){
            vecino = (ArrayList<Integer>) solucion.clone();
        }
        return(vecino);
    }

    /**
     * generar estado inicial aleatorio.
     * En esta versión 3 además tiene en cuenta la matriz de pesos frecs y se usa en la reinicialización
     * @return array de enteros con la configuración inicial
     */
    private ArrayList<Integer> solucionGreedy(){

        ArrayList<Integer> aux = new ArrayList<>();  //array a devolverr
        ArrayList<Integer> ciudadesPorAsignar = new ArrayList<>();   //vamos leyendo de aqui las ciudades
        //inicializamos ciudadesPorAsignar
        for(int i=1;i<costes.size();i++){
            ciudadesPorAsignar.add(i);
        }


        //partimos siempre de una ciudad, en la 1 iteración la 0
        int ciudad=0;
        double costeActual = 100000000;  //para que se mejora la 1 vez
        double costeI;  //coste de iteracion

        //tantas veces como ciudades (hasta rellenar aux)
        for(int i=0;i<costes.size()-1;i++){
            int ciudadVecina=-1;
            //miramos el coste de la ciudad actual con el resto de ciudades. AÑADIMOS FACTOR DE PENALIZACIÓN
            for(int j: ciudadesPorAsignar){
                 costeI= costes.get(ciudad).distancia(costes.get(j)) +
                         this.factorPenalizacion(ciudad,j);
                if(ciudadVecina==-1 || costeI < costeActual){
                    ciudadVecina = j;
                    costeActual = costeI;
                }
            }

            //al finalizar la exploración de todas las ciudades
            //añadimos ciudad vecina a aux y la eliminamos de ciudadesPorAsignar
            aux.add(ciudadVecina);
            final int borrar = ciudadVecina;
            ciudadesPorAsignar.removeIf(x -> x == borrar);
            ciudad = ciudadVecina;
        }
        return(aux);
    }

    /**
     * Para calcular el coste de un recorrido en base a la formula del enunciado
     * @return int, coste (redondeado hacia arriba)
     */
    private int costeRecorrido(ArrayList<Integer> estado){
        double coste=0.0;
        //primera ciudad del vector de recorrido (que no contiene a la ciudad 0)
        Integer ciudad1 = estado.get(0);
        //Sumamos en coste la distancia de la ciudad con id 0 a la primera ciudad del estado.
        coste += costes.get(0).distancia(costes.get(ciudad1));

        //la última a mano
        for(int i=0;i<estado.size()-1;i++){
            coste += costes.get(estado.get(i)).distancia(costes.get(estado.get(i+1)));
        }

        //sumamos de la úlima a cero
        coste += costes.get(estado.get(estado.size()-1)).distancia(costes.get(0));
        return((int)Math.ceil(coste));
    }
    
    public void imprimeRecorrido(ArrayList<Integer> estado){
        System.out.print("\tRECORRIDO: ");
        for(Integer x: estado) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    private double factorPenalizacion(int i, int j){
        if(frecs==null){
            return(0);
        }
        double maximaFrecuencia=-1.0;
        //calculamos máxia frecuencia de frecs
        for(int a=0;a<costes.size();a++) {
            for (int b = 0; b < costes.size(); b++) {
                if(a!=b && a>b) {  //por ser matriz simétrica
                    if (frecs[a][b] > maximaFrecuencia) {
                        maximaFrecuencia = frecs[a][b];
                    }
                }
            }
        }
        return(mu * (DMAX-DMIN) * ((double)frecs[i][j]/maximaFrecuencia));
    }

}



//impresión matriz
/*for(int i=0;i<costes.size();i++){
    for(int j=0;j<costes.size();j++){
        System.out.print(frecs[i][j] + " ");
     }
     System.out.println();
 }
 System.out.println("\n\n\n\n");*/