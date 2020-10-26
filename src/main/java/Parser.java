import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @autor Pablo García Fernández.
 * @file Parser.java
 * @objective procesar los archivos .txt pasados como argumentos (el de aleatorios y el de ciudades)
 */

public class Parser {

    /**
     * Para parsear el archivo de ciudades con sus coordenadas
     * @param path, ruta al archivo
     * @return. HashMap, la clave es el id de la ciudad (entero), el valor sus coordenadas.
     */
    public static HashMap<Integer, Coordenadas> parsearCiudades(String path){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File (path);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            int i=0;
            String linea = br.readLine();
            HashMap<Integer,Coordenadas> ciudades = new HashMap<>(Integer.parseInt(linea));
            //resto de líneas, ciudades
            while ((linea = br.readLine()) != null) {
                String partes[] = linea.split(" ");
                double x = Double.parseDouble(partes[0]);
                double y = Double.parseDouble(partes[1]);
                ciudades.put(i,new Coordenadas(x,y));
                i++;
            }
            return(ciudades);
        }
        catch(Exception e){
            System.out.println("File open error!: \n");
            e.printStackTrace();
            return(null);
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                System.out.println("File open error!: \n");
                e2.printStackTrace();
            }
        }
    }

    /**
     * Función para parsear el archivo de aleatorios
     * @param path, ruta al archivo
     * @return, estado inicial. vector de enteros con ciudades no repetidas 1-99
     */
    public static ArrayList<Integer> parsearAleatorios(String path){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File (path);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            String linea;
            ArrayList<Integer> estadoInicial = new ArrayList<>(100);
            while ((linea = br.readLine()) != null) {
                int valor = 1 + (int)(Double.parseDouble(linea) * 99);
                if(estadoInicial.contains(valor)){
                    do {
                        valor = (valor+1)%99;
                        if(valor==0)
                            valor=99;
                    }while(estadoInicial.contains(valor));
                }
                estadoInicial.add(valor);
            }
            return(estadoInicial);
        }
        catch(Exception e){
            System.out.println("File open error!: \n");
            e.printStackTrace();
            return(null);
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                System.out.println("File open error!: \n");
                e2.printStackTrace();
            }
        }
    }
}
