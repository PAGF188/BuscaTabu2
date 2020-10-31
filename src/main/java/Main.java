import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @autor Pablo García Fernández.
 * @file Main.java
 * @objective Leer argumentos de entrada. Disparar le ejecución de la búsqueda
 */
public class Main {
    public static void main(String[] args) {

        if(args.length!=1){
            System.out.println("Argument Error! : java -jar <fichero_distancias>\n\n");
            System.exit(-1);
        }

        //Para la prueba inicial sin inicialización greedy
        /**
        HashMap<Integer,Coordenadas> ciudades = Parser.parsearCiudades(args[0]);
        ArrayList<Integer> coste = new ArrayList<>();
        ArrayList<Integer> iteracion = new ArrayList<>();
        for(int i=0;i<10;i++){
            System.out.println("EMPEZAMOS BÚSQUEDA: " + i);
            Busqueda b = new Busqueda(ciudades);
            ArrayList<Integer> aux = b.buscar();
            coste.add(aux.get(0));
            iteracion.add(aux.get(1));
        }
        System.out.println("RESULTADOS SIN MODIFICACIONES: ");
        System.out.println("\t\tMedia\t\t\t\tDesviación");
        System.out.println("Coste: \t" + media(coste) + "\t\t\t" + desviacionTipica(coste));
        System.out.println("Iter.: \t" + media(iteracion) + "\t\t\t" + desviacionTipica(iteracion));
         */

        //Para la prueba con inicialización greedy.
        HashMap<Integer,Coordenadas> ciudades = Parser.parsearCiudades(args[0]);
        Busqueda b = new Busqueda(ciudades);
        ArrayList<Integer> aux = b.buscar();
        System.out.println("RESULTADOS CON MODIFICACIONES greedy: ");
        System.out.println("Coste: \t" + aux.get(0));
        System.out.println("Iter.: \t" + aux.get(1));
    }

    private static double media(ArrayList<Integer> valores){
        double sum=0.0;
        for(int i=0;i<valores.size();i++){
            sum += valores.get(i);
        }
        return(sum/valores.size());
    }

    private static double desviacionTipica(ArrayList<Integer> nums){

        double media = media(nums);
        double s=0;

        for(int e: nums){
            s += (e-media)*(e-media);
        }
        return(Math.sqrt(s/(nums.size()*1.0)));
    }
}


/**
 * for (Map.Entry<Integer, Coordenadas> entry : ciudades.entrySet()) {
 *                 System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
 *             }
 */

