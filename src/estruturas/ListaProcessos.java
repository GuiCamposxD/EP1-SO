package estruturas;

import java.util.ArrayList;

public class ListaProcessos {
    private final ArrayList<BCP> fila;

    public ListaProcessos() {
        this.fila = new ArrayList<>();
    }

    public void adicionaProcesso(BCP processo) {
        this.fila.add(processo);
    }
}
