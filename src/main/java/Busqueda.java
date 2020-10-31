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


    /**
     * Para iniciar la búsqueda con estado inicial aleatorio.
     * @param costes, Para cada ciudad sus posicion en coordenadas (radianes)
     */
    public Busqueda(HashMap<Integer, Coordenadas> costes) {
        this.costes = costes;
        estado = this.solucionGreedy();
        mejor = (ArrayList<Integer>) estado.clone();

        /**borrar
         *
         */
        System.out.println("Partiendo estrategia avara: ");
        System.out.println(estado);
        System.out.println("Coste: " + costeRecorrido(estado) + "\n\n");
        //aqui

        listaTabu = new ListaTabu();
        i_intercambiada=-1;
        j_intercambiada=-1;
        iteracciones=1;
        reinicios=1;
        iteracionMejor=0;
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

            //Reinicio
            if(IteraccionesSinMejora==MAX){
                listaTabu.reiniciarTabla();
                estado = (ArrayList<Integer>) mejor.clone();
                reinicios++;
                IteraccionesSinMejora = 0;
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
        double costeI;  //coste de 1 iteracion

        //tantas veces como ciudades (hasta rellenar aux)
        for(int i=0;i<costes.size()-1;i++){
            int ciudadVecina=-1;
            //miramos el coste de la ciudad actual con el resto de ciudades
            for(int j: ciudadesPorAsignar){
                 costeI= costes.get(ciudad).distancia(costes.get(j));
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

}
