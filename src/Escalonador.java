package src;

import src.estruturas.BCP;
import src.estruturas.ListaProcessos;

import java.util.Iterator;

public class Escalonador{
    private void lidaEntradaSaida(BCP processoExecutando, ListaProcessos processosBloqueados, SistemaOperacional sistemaOperacional, Logger logger) {
        for (BCP processo : processosBloqueados.getFila()) {
            processo.decrementaTempoEspera();
        }

        logger.logESProcessos(processoExecutando);
        processoExecutando.setFezES(true);
        processoExecutando.setEstado("Bloqueado");
        processoExecutando.setTempoEspera(sistemaOperacional.getQuantum());
        processoExecutando.incrementaPc();
        logger.logInterrompendoProcessos(processoExecutando, sistemaOperacional);
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

    private void lidaProcessosBloqueados(ListaProcessos processosBloqueados, ListaProcessos processosProntos) {
        if (processosProntos.getFila().isEmpty() && !processosBloqueados.getFila().isEmpty()) {
            for (BCP processo : processosBloqueados.getFila()) {
                processo.setTempoEspera(0);
                processo.setQuantumRestante(3);
            };

            processosProntos.getFila().addAll(processosBloqueados.getFila());
            return;
        }

        for (BCP processo : processosBloqueados.getFila()) {
            processo.incrementaNProcessosExecutadosEnquantoBloqueado();
            processo.decrementaTempoEspera();
        }

        Iterator<BCP> iterator = processosBloqueados.getFila().iterator();
        while (iterator.hasNext()) {
            BCP processo = iterator.next();
            if (processo.getTempoEspera() == 0 || processo.getNProcessosExecutadosEnquantoBloqueado() >= 2) {
                processo.setTempoEspera(0);
                processo.setQuantumRestante(3);

                processosProntos.getFila().addLast(processo);
                iterator.remove();
            }
        }
    }

    public void colocaProcessoListaPronto(BCP processo, SistemaOperacional sistemaOperacional) {
        ListaProcessos processosProntos = sistemaOperacional.getProcessosProntos();

        processosProntos.getFila().addLast(processo);
        processo.setQuantumRestante(sistemaOperacional.getQuantum());
    }

    public void escalonaProcessos(SistemaOperacional sistemaOperacional, Logger logger) {
        ListaProcessos processosProntos = sistemaOperacional.getProcessosProntos();
        ListaProcessos processosBloqueados = sistemaOperacional.getProcessosBloqueados();
        BCP processoExecutando = processosProntos.getFila().get(0);

        processoExecutando.setEstado("Executando");
        logger.logExecutandoProcessos(processosProntos.getFila().get(0));
        processosProntos.getFila().remove(0);

        lidaProcessosBloqueados(processosBloqueados, processosProntos);

        while (processoExecutando.getQuantumRestante() > 0) {
            String comando = processoExecutando.getSegmentoTexto().get(processoExecutando.getPc());
            char primeiraLetraComando = comando.charAt(0);

            if (primeiraLetraComando == 'S') {
                processoExecutando.setEstado("Finalizado");
                logger.logFinalizaProcessos(processoExecutando);
                sistemaOperacional.getTabelaProcessos().getTabela().remove(processoExecutando);
                sistemaOperacional.incrementaProcessosFinalizados();
                sistemaOperacional.incrementaQuantidadeQuantum();
                break;
            }

            if (primeiraLetraComando == 'E') {
                lidaEntradaSaida(processoExecutando, processosBloqueados, sistemaOperacional, logger);
                processosBloqueados.getFila().addLast(processoExecutando);
                sistemaOperacional.incrementaQuantidadeQuantum();
                break;
            }

            if (primeiraLetraComando == 'C') {
                lidaComando(processoExecutando);
                if (!processoExecutando.getFezES())  processoExecutando.incrementaInstrucoesExecutadas();
            } else if (primeiraLetraComando == 'X' || primeiraLetraComando == 'Y') {
                lidaRegistradores(processoExecutando, comando);
            }

            processoExecutando.decrementaQuantumRestante();

            if (processoExecutando.getQuantumRestante() == 0){
                colocaProcessoListaPronto(processoExecutando, sistemaOperacional);
                logger.logInterrompendoProcessos(processoExecutando, sistemaOperacional);
                sistemaOperacional.incrementaQuantidadeQuantum();
                break;
            }
        }
    }

    public static void main(String[] args) {
        SistemaOperacional sistemaOperacional = new SistemaOperacional();

        sistemaOperacional.executaProcessos(sistemaOperacional, sistemaOperacional.getLogger());
    }
}