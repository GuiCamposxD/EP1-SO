package estruturas;

import java.util.ArrayList;

public class TabelaProcessos {
    private ArrayList<BCP> tabelaProcessoProntos;
    private ArrayList<BCP> tabelaProcessoBloqueados;

    public TabelaProcessos() {
        this.tabelaProcessoProntos = new ArrayList<>();
        this.tabelaProcessoBloqueados = new ArrayList<>();
    }
}
