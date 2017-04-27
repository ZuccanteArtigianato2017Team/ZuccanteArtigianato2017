/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestioneLim;

import com.marcotramontini.grafo.Grafo;
import javafx.util.Pair;

/**
 *
 * @author Marco
 */
public class Percorso {

    public static Percorso staticInstance;
    private final int righe;
    private final int colonne;

    private final Grafo<Pair<Integer, Integer>> grafo;

    public Percorso(int righe, int colonne) {
        this.righe = righe;
        this.colonne = colonne;
        grafo = new Grafo<>();
        for (int r = 0; r < righe; r++) {
            for (int c = 0; c < colonne; c++) {
                grafo.addNodo(new Pair<>(r, c));
            }
        }
    }

    private void addArco(Pair<Integer, Integer> primo, Pair<Integer, Integer> secondo) {
        if (primo.getKey() >= 0 && primo.getValue() >= 0 && secondo.getKey() >= 0 && secondo.getKey() >= 0) {
            if (primo.getKey() < righe && primo.getValue() < colonne && secondo.getKey() < righe && secondo.getValue() < colonne) {
                grafo.addArco(primo, secondo, 0);
            }
        }
    }

    public void setCella(int r, int c, Tessera pezzo) {
        if (pezzo.getSX()) {
            addArco(new Pair<>(r,c-1),new Pair<>(r,c));
            addArco(new Pair<>(r,c),new Pair<>(r,c-1));
        }
        if(pezzo.getSU()){
            addArco(new Pair<>(r-1,c),new Pair<>(r,c));
            addArco(new Pair<>(r,c),new Pair<>(r-1,c));
        }
        if(pezzo.getDX()){
            addArco(new Pair<>(r,c+1),new Pair<>(r,c));
            addArco(new Pair<>(r,c),new Pair<>(r,c+1));
        }
        if(pezzo.getGIU()){
            addArco(new Pair<>(r+1,c),new Pair<>(r,c));
            addArco(new Pair<>(r,c),new Pair<>(r+1,c));
        }
            
    }
    
    @Override
    public String toString(){
        return grafo.toString();
    }

}
