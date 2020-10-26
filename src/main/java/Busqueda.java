import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Pablo García Fernández
 * @file Bsqueda.java
 * @objective - Se encarga de realizar la búsqueda tabú para el problema del viajante.
 *
 */
public class Busqueda {
    private final int MAX = 100;
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
     * Para iniciar la búsqueda en una solucion dada.
     * @param costes, Para cada ciudad sus posicion en coordenadas (radianes)
     * @param estadoInicial, estado de partida, solucion inicial.
     */
    public Busqueda(HashMap<Integer, Coordenadas> costes, ArrayList<Integer> estadoInicial) {
        this.costes = costes;
        this.estado = estadoInicial;
        mejor = (ArrayList<Integer>) estado.clone();
        listaTabu = new ListaTabu();
        i_intercambiada=-1;
        j_intercambiada=-1;
        iteracciones=1;
        reinicios=0;
        iteracionMejor=0;
    }

    /**
     * Para iniciar la búsqueda con estado inicial aleatorio.
     * @param costes, Para cada ciudad sus posicion en coordenadas (radianes)
     */
    public Busqueda(HashMap<Integer, Coordenadas> costes) {
        this.costes = costes;
        estado = this.generaEstadoAleatorio();
        mejor = (ArrayList<Integer>) estado.clone();
        listaTabu = new ListaTabu();
        i_intercambiada=-1;
        j_intercambiada=-1;
        iteracciones=1;
        reinicios=1;
        iteracionMejor=0;
    }

    public void buscar(){

        int IteraccionesSinMejora = 0;

        System.out.println("RECORRIDO INICIAL");
        imprimeRecorrido(estado);
        System.out.println("\tCOSTE (km): " + costeRecorrido(estado));
        System.out.println();

        //Iniciamos la búsqueda.  10001
        while(iteracciones!=10001){

            //Reinicio
            if(IteraccionesSinMejora==MAX){
                listaTabu.reiniciarTabla();
                estado = (ArrayList<Integer>) mejor.clone();
                reinicios++;
                IteraccionesSinMejora = 0;
                System.out.println("***************\n" +
                        "REINICIO: "+ reinicios +"\n" +
                        "***************\n");
            }

            System.out.println("ITERACION: " + iteracciones);

            //Nuevo estado pasa a ser el mejor de los vecinos.
            estado = (ArrayList<Integer>) exploraContorna(estado).clone();
            //añadimos el intercambio realizado.
            listaTabu.addProhibicion(i_intercambiada,j_intercambiada);

            System.out.println("\tINTERCAMBIO: (" + i_intercambiada + ", " + j_intercambiada + ")");
            imprimeRecorrido(estado);
            System.out.println("\tCOSTE (km): " + costeRecorrido(estado));

            if(costeRecorrido(estado) < costeRecorrido(mejor)){
                mejor = (ArrayList<Integer>) estado.clone();
                IteraccionesSinMejora=0;
                iteracionMejor = iteracciones;
            }
            else{
                IteraccionesSinMejora++;
            }

            System.out.println("\tITERACIONES SIN MEJORA: " + IteraccionesSinMejora);
            System.out.println("\tLISTA TABU:");
            listaTabu.imprime();
            System.out.println();
            iteracciones++;
        }

        System.out.println("\nMEJOR SOLUCION");
        imprimeRecorrido(mejor);
        System.out.println("\tCOSTE (km): " + costeRecorrido(mejor));
        System.out.println("\tITERACION: " + iteracionMejor);

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
    private ArrayList<Integer> generaEstadoAleatorio(){
        ArrayList<Integer> si = new ArrayList<>();
        for(int i=0; i<costes.size()-1; i++){
            int valor = 1 + (int)(Math.random() * 99);
            if(si.contains(valor)) {
                do {
                    valor = (valor + 1) % 99;
                    if (valor == 0)
                        valor = 99;
                } while (si.contains(valor));
            }
            si.add(valor);
        }
        return(si);
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
