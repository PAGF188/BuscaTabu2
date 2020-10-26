
/**
 * @autor Pablo García Fernández.
 * @file Coordenadas.java
 * @objective - Encapsulas las coordenadas de una ciudad.
 *            - Pasar los grados a radianes.
 *            - Calcular la distancia entre dos ciudades.
 */
public class Coordenadas {
    public double x;
    public double y;
    private final static int radio = 6371;


    public Coordenadas(double x, double y) {
        this.x = Math.toRadians(x);
        this.y = Math.toRadians(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distancia(Coordenadas aux){
        return(Math.ceil(2.0*radio*Math.asin(Math.sqrt(Math.sin((aux.x-this.x)/2.0)*Math.sin((aux.x-this.x)/2.0) +
                Math.cos(this.x)*Math.cos(aux.x)*Math.sin((aux.y-this.y)/2)*Math.sin((aux.y-this.y)/2.0)))));
    }

    @Override
    public String toString() {
        return "Coordenadas{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
