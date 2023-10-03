package estruturas;

import java.util.ArrayList;

public class TabelaProcessos {
    public ArrayList<BCP> getTabelaProcessoProntos() {
        return tabelaProcessoProntos;
    }

    public ArrayList<BCP> getTabelaProcessoBloqueados() {
        return tabelaProcessoBloqueados;
    }

    private ArrayList<BCP> tabelaProcessoProntos;
    private ArrayList<BCP> tabelaProcessoBloqueados;

    public TabelaProcessos() {
        this.tabelaProcessoProntos = new ArrayList<>();
        this.tabelaProcessoBloqueados = new ArrayList<>();
    }

    public void insereTabelaProcessoProntos(BCP processo) {
        if (processo != null) {
            this.tabelaProcessoProntos.add(processo);
            return;
        }

        throw new RuntimeException("Processo não existe!");
    }
}
