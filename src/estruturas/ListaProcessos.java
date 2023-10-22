package src.estruturas;

import java.util.LinkedList;

public class ListaProcessos {
    private final LinkedList<BCP> fila;

    // Getters
    public LinkedList<BCP> getFila() {
        return fila;
    }

    public ListaProcessos() {
        this.fila = new LinkedList<>();
    }

    public void adicionaProcesso(BCP processo) {
        this.fila.add(processo);
    }
}
