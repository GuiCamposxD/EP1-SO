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
import java.util.Iterator;

public class Escalonador{
    private final Logger logger;
    private final TabelaProcessos tabelaProcessos;
    private final ListaProcessos listaProcessosProntos;
    private final ListaProcessos listaProcessosBloqueados;
    private int quantidadeProcessos;
    private int processosFinalizados;
    private int quantum;
    private int quantidadeQuantum;
    private int totalDeInstrucoesExecutadas;
    private int trocasRealizadas;
    // Getters
    public int getTotalDeInstrucoesExecutadas() {
        return totalDeInstrucoesExecutadas;
    }

    public int getTrocasRealizadas() {
        return trocasRealizadas;
    }

    public TabelaProcessos getTabelaProcessos() {
        return this.tabelaProcessos;
    }

    public ListaProcessos getProcessosProntos() {
        return this.listaProcessosProntos;
    }

    public int getQuantidadeQuantum(){
        return this.quantidadeQuantum;
    }
    public int getQuantum() {
        return quantum;
    }
    public int getQuantidadeProcessos() {
        return quantidadeProcessos;
    }

    // Setters
    public void incrementaTrocasRealizadas() {
        this.trocasRealizadas += 1;
    }

    public void incrementaTotalDeInstrucoesExecutadas() {
        this.totalDeInstrucoesExecutadas += 1;
    }
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
    public void incrementaProcessosFinalizados() {
        this.processosFinalizados++;
    }
    public void incrementaQuantidadeQuantum(){
        this.quantidadeQuantum++;
    }

    public Escalonador() {
        this.tabelaProcessos = new TabelaProcessos();
        this.listaProcessosProntos = new ListaProcessos();
        this.listaProcessosBloqueados = new ListaProcessos();
        this.processosFinalizados = 0;
        this.setQuantum();
        this.logger = new Logger(quantum);

        this.lerProgramas();
    }
    private void lerProgramas() {
        String path = "./programas";
        File pasta = new File(path);

        if (pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles();
            assert arquivos != null;
            this.quantidadeProcessos = arquivos.length - 1;

            Arrays.sort(arquivos, Comparator.comparing(File::getName));

            for (int i = 0; i < arquivos.length; i++) {
                File arquivo = new File(arquivos[i].toURI());
                if (!arquivos[i].getName().equals("quantum.txt")) {

                    try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
                        String linha;
                        linha = leitor.readLine();
                        this.tabelaProcessos.insereProcesso(
                                new BCP(arquivos[i].getName(), linha, i, this.quantum)
                        );

                        while ((linha = leitor.readLine()) != null) {
                            this.tabelaProcessos.getTabela().get(i).setSegmentoTexto(linha);
                        }

                        this.listaProcessosProntos.adicionaProcesso(this.tabelaProcessos.getTabela().get(i));
                        this.logger.logCarregaProcessos(this.tabelaProcessos.getTabela().get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("O caminho especificado não é uma pasta.");
        }
    }

    public void executaProcessos() {
        while (processosFinalizados != this.quantidadeProcessos) {
            this.escalonaProcessos();
        }
        logger.logSaida(this);
    }


    private void lidaEntradaSaida(BCP processoExecutando) {
        for (BCP processo : listaProcessosBloqueados.getFila()) {
            processo.decrementaTempoEspera();
        }

        logger.logESProcessos(processoExecutando);
        processoExecutando.setEstado("Bloqueado");
        processoExecutando.setTempoEspera(getQuantum());
        processoExecutando.incrementaPc();

        logger.logInterrompendoProcessos(processoExecutando);
    }

    private void lidaComando(BCP processoExecutando) {
        processoExecutando.incrementaPc();
    }

    private void lidaRegistradores(BCP processoExecutando, String comando) {
        int equalSignIndex = comando.indexOf('=');

        if (equalSignIndex != -1 && equalSignIndex < comando.length() - 1) {
            int valorRegistrador = Integer.parseInt(comando.substring(equalSignIndex + 1));

            if (comando.charAt(0) == 'X') {
                processoExecutando.setRegistradorX(valorRegistrador);
            } else {
                processoExecutando.setRegistradorY(valorRegistrador);
            }
        }

        processoExecutando.incrementaPc();
    }

    private void colocaBloqueadoEmPronto(
        ) {
        Iterator<BCP> iterator = listaProcessosBloqueados.getFila().iterator();
        while (iterator.hasNext()) {
            BCP processo = iterator.next();
            if (processo.getTempoEspera() == 0) {
                processo.setQuantumRestante(getQuantum());

                listaProcessosProntos.getFila().addLast(processo);
                iterator.remove();
            }
        }
    }

    private void lidaProcessosBloqueados(
            ) {

        if (!listaProcessosBloqueados.getFila().isEmpty()) {
            while (listaProcessosBloqueados.getFila().getFirst().getTempoEspera() > 0) {
                for (BCP processo : listaProcessosBloqueados.getFila()) {
                    processo.decrementaTempoEspera();
                }
            }
        }

        this.colocaBloqueadoEmPronto();
    }

    public void colocaProcessoListaPronto(BCP processo) {
        ListaProcessos processosProntos = getProcessosProntos();

        processosProntos.getFila().addLast(processo);
        processo.setQuantumRestante(getQuantum());
    }

    public void escalonaProcessos() {
        BCP processoExecutando = listaProcessosProntos.getFila().get(0);

        processoExecutando.setEstado("Executando");
        logger.logExecutandoProcessos(listaProcessosProntos.getFila().get(0));
        listaProcessosProntos.getFila().remove(0);

        lidaProcessosBloqueados();

        while (processoExecutando.getQuantumRestante() > 0) {
            String comando = processoExecutando.getSegmentoTexto().get(processoExecutando.getPc());
            char primeiraLetraComando = comando.charAt(0);

            if (primeiraLetraComando == 'S') {
                processoExecutando.setEstado("Finalizado");

                getTabelaProcessos().getTabela().remove(processoExecutando);

                incrementaTotalDeInstrucoesExecutadas();
                incrementaTrocasRealizadas();
                incrementaQuantidadeQuantum();
                incrementaProcessosFinalizados();

                logger.logFinalizaProcessos(processoExecutando);
                break;
            }

            if (primeiraLetraComando == 'E') {
                lidaEntradaSaida(processoExecutando);

                listaProcessosBloqueados.getFila().addLast(processoExecutando);

                incrementaTotalDeInstrucoesExecutadas();
                incrementaTrocasRealizadas();
                incrementaQuantidadeQuantum();

                break;
            }

            if (primeiraLetraComando == 'C') {
                lidaComando(processoExecutando);
                incrementaTotalDeInstrucoesExecutadas();
            } else if (primeiraLetraComando == 'X' || primeiraLetraComando == 'Y') {
                lidaRegistradores(processoExecutando, comando);
                incrementaTotalDeInstrucoesExecutadas();
            }

            processoExecutando.decrementaQuantumRestante();

            if (processoExecutando.getQuantumRestante() == 0) {
                colocaProcessoListaPronto(processoExecutando);

                logger.logInterrompendoProcessos(processoExecutando);

                incrementaTrocasRealizadas();
                incrementaQuantidadeQuantum();

                break;
            }
        }
    }

    public static void main(String[] args) {
        Escalonador escalonador = new Escalonador();
        escalonador.executaProcessos();
    }
}