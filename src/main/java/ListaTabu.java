import java.util.LinkedList;

/**
 * @autor Pablo García Fernández.
 * @file ListaTabu.java
 * @objective Almacenar las tuplas de movimiento i,j prohibidas durante N iteracciones
 */
public class ListaTabu {

    private final int N = 100;
    private LinkedList<Tupla> tuplas;

    private class Tupla {
        int i;
        int j;

        public Tupla(int i, int j){
            this.i=i;
            this.j=j;
        }
    }

    public ListaTabu() {
        tuplas = new LinkedList<>();
    }

    public void addProhibicion(int i, int j){
        Tupla aux = new Tupla(i,j);
        if(tuplas.size()==N){
            tuplas.removeFirst();
        }
        tuplas.addLast(aux);
    }

    public boolean contieneProhibicion(int i, int j){
        Tupla aux = new Tupla(i,j);
        for(Tupla t: tuplas){
            if(t.i==i && t.j==j)
                return(true);
        }
        return(false);
    }

    public void imprime(){
        for(Tupla t: tuplas){
            System.out.println("\t"+t.i + " " + t.j);
        }
    }

    public void reiniciarTabla(){
        this.tuplas=null;
        this.tuplas = new LinkedList<>();
    }
}
