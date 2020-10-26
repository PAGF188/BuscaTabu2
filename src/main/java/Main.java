import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @autor Pablo García Fernández.
 * @file Main.java
 * @objective Leer argumentos de entrada. Disparar le ejecución de la búsqueda
 */
public class Main {
    public static void main(String[] args) {
        //Cuando queremos que el estado inicial sea aleatorio
        if(args.length==1){
            HashMap<Integer,Coordenadas> ciudades = Parser.parsearCiudades(args[0]);
            Busqueda b = new Busqueda(ciudades);
            b.buscar();
        }
        //cuando le pasamos nosotros el estado inicial.
        else if(args.length==2){
            HashMap<Integer,Coordenadas> ciudades = Parser.parsearCiudades(args[0]);
            ArrayList<Integer> estadoInicial = Parser.parsearAleatorios(args[1]);
            //HashMap<Integer,Coordenadas> ciudades = Parser.parsearCiudades("/home/pablo/coordenadas_100.txt");
            //ArrayList<Integer> estadoInicial = Parser.parsearAleatorios("/home/pablo/aleatorios100.txt");
            //ArrayList<Integer> estadoInicial = new ArrayList<>(Arrays.asList(35, 40, 5, 68, 94, 14, 18, 46, 64, 41, 51, 96, 7,29, 61, 93, 44, 77, 42, 30, 43, 19, 60, 45, 62, 37, 55, 23, 76, 16, 66, 59, 10, 87, 73, 74, 26, 97, 50, 48, 83, 47, 98, 70, 89, 15, 9, 11, 54, 86, 52, 91, 99, 36, 63, 57, 21, 28, 32, 17, 92, 22, 33, 38, 31, 34, 81, 1, 95, 84, 39, 20, 6, 56, 13, 49, 12, 25, 90, 4, 79, 65, 67, 75, 69, 3, 24, 27, 71, 72, 85, 58, 78, 2, 82, 80, 8, 53, 88 ));
            Busqueda b = new Busqueda(ciudades,estadoInicial);
            b.buscar();
        }
        else{
            System.out.println("Argument Error! : java -jar <fichero_distancias> [fichero_aleatorios]\n\n");
        }
    }
}


/**
 * for (Map.Entry<Integer, Coordenadas> entry : ciudades.entrySet()) {
 *                 System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
 *             }
 */
