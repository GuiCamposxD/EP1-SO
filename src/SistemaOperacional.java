import estruturas.BCP;
import estruturas.ListaProcessos;
import estruturas.TabelaProcessos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class SistemaOperacional {
    private Escalonador escalonador;
    private final TabelaProcessos tabelaProcessos;
    private final ListaProcessos processosProntos;
    private final ListaProcessos processosBloqueados;

    //Getters
    public Escalonador getEscalonador() {
        return this.escalonador;
    }

    public TabelaProcessos getTabelaProcessos() {
        return this.tabelaProcessos;
    }

    public ListaProcessos getProcessosProntos() {
        return this.processosProntos;
    }

    public ListaProcessos getProcessosBloqueados() {
        return this.processosBloqueados;
    }
    public SistemaOperacional() {
        this.escalonador = new Escalonador();
        this.tabelaProcessos = new TabelaProcessos();
        this.processosProntos = new ListaProcessos();
        this.processosBloqueados = new ListaProcessos();

        this.lerProgramas();
    }

    private void insereTabelaPronto(BCP processo) {
        this.getProcessosProntos().adicionaProcesso(processo);
    }

    private void lerProgramas() {
        String path = "./programas";
        File pasta = new File(path);

        if (pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles();

            if (arquivos != null) {
                Arrays.sort(arquivos, Comparator.comparing(File::getName));

                for (int i = 0; i < arquivos.length; i++) {
                    File arquivo = new File(arquivos[i].toURI());
                    if (!arquivos[i].getName().equals("quantum.txt")) {

                        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
                            this.tabelaProcessos.insereProcesso(
                                    new BCP(arquivos[i].getName(), i)
                            );

                            String linha;

                            while ((linha = leitor.readLine()) != null) {
                                this.tabelaProcessos.getTabela().get(i).setSegmentoTexto(linha);
                            }

                            this.insereTabelaPronto(this.tabelaProcessos.getTabela().get(i));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("O caminho especificado não é uma pasta.");
        }
    }

    public void executaProcessos() {

    }
}
