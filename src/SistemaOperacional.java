package src;

import src.estruturas.BCP;
import src.estruturas.ListaProcessos;
import src.estruturas.TabelaProcessos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class SistemaOperacional {
    private Escalonador escalonador;
    private final TabelaProcessos tabelaProcessos;
    private final ListaProcessos listaProcessosProntos;
    private final ListaProcessos listaProcessosBloqueados;
    private int processosFinalizados;
    private int quantum;

    public SistemaOperacional() {
        this.escalonador = new Escalonador();
        this.tabelaProcessos = new TabelaProcessos();
        this.listaProcessosProntos = new ListaProcessos();
        this.listaProcessosBloqueados = new ListaProcessos();
        this.processosFinalizados = 0;
        this.setQuantum();

        this.lerProgramas();
    }

    // Getters
    public Escalonador getEscalonador() {
        return this.escalonador;
    }

    public TabelaProcessos getTabelaProcessos() {
        return this.tabelaProcessos;
    }

    public ListaProcessos getProcessosProntos() {
        return this.listaProcessosProntos;
    }

    public ListaProcessos getProcessosBloqueados() {
        return this.listaProcessosBloqueados;
    }

    // Setters
    private void setQuantum() {
        try (BufferedReader leitor = new BufferedReader(new FileReader("./programas/quantum.txt"))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                this.quantum = Integer.parseInt(linha);
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Não foi possível ler o quantum");
        }
    }
    public void setProcessosTerminados(int processosFinalizados) {
        this.processosFinalizados = processosFinalizados;
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
                                    new BCP(arquivos[i].getName(), i, this.quantum)
                            );

                            String linha;
                            boolean primeiraLinha = true;

                            while ((linha = leitor.readLine()) != null) {
                                if (primeiraLinha) {
                                    primeiraLinha = false;
                                    continue;
                                }
                                this.tabelaProcessos.getTabela().get(i).setSegmentoTexto(linha);
                            }

                            this.getProcessosProntos().adicionaProcesso(this.tabelaProcessos.getTabela().get(i));
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

    public void executaProcessos(SistemaOperacional sistemaOperacional) {
        while (processosFinalizados < this.tabelaProcessos.getTabela().size()) {
            this.escalonador.escalonaProcessos(sistemaOperacional);
        }
    }

    public void incrementaProcessosFinalizados() {
        this.processosFinalizados++;
    }
}
