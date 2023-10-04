package estruturas;

import java.util.ArrayList;

public class TabelaProcessos {
    private ArrayList<BCP> tabela;

    //Getters
    public ArrayList<BCP> getTabela() {
        return tabela;
    }

    public TabelaProcessos() {
        this.tabela = new ArrayList<>();
    }


    public void insereProcesso(BCP processo) {
        if (processo == null) throw new RuntimeException("Processo não existe!");

        tabela.add(processo);
    }
}
