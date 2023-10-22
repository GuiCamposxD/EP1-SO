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
        processoExecutando.setEstado("Bloqueado");
        processoExecutando.setTempoEspera(sistemaOperacional.getQuantum());
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
        ListaProcessos processosBloqueados,
        ListaProcessos processosProntos,
        SistemaOperacional sistemaOperacional
    ) {
        Iterator<BCP> iterator = processosBloqueados.getFila().iterator();
        while (iterator.hasNext()) {
            BCP processo = iterator.next();
            if (processo.getTempoEspera() == 0) {
                processo.setQuantumRestante(sistemaOperacional.getQuantum());

                processosProntos.getFila().addLast(processo);
                iterator.remove();
            }
        }
    }

    private void lidaProcessosBloqueados(
            ListaProcessos processosBloqueados,
            ListaProcessos processosProntos,
            SistemaOperacional sistemaOperacional
    ) {

        if (!processosBloqueados.getFila().isEmpty()) {
            while (processosBloqueados.getFila().getFirst().getTempoEspera() > 0) {
                for (BCP processo : processosBloqueados.getFila()) {
                    processo.decrementaTempoEspera();
                }
            }
        }

        this.colocaBloqueadoEmPronto(processosBloqueados, processosProntos, sistemaOperacional);
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

        lidaProcessosBloqueados(processosBloqueados, processosProntos, sistemaOperacional);

        while (processoExecutando.getQuantumRestante() > 0) {
            String comando = processoExecutando.getSegmentoTexto().get(processoExecutando.getPc());
            char primeiraLetraComando = comando.charAt(0);

            if (primeiraLetraComando == 'S') {
                processoExecutando.setEstado("Finalizado");

                sistemaOperacional.getTabelaProcessos().getTabela().remove(processoExecutando);

                sistemaOperacional.incrementaTotalDeInstrucoesExecutadas();
                sistemaOperacional.incrementaTrocasRealizadas();
                sistemaOperacional.incrementaQuantidadeQuantum();
                sistemaOperacional.incrementaProcessosFinalizados();

                logger.logFinalizaProcessos(processoExecutando);
                break;
            }

            if (primeiraLetraComando == 'E') {
                lidaEntradaSaida(processoExecutando, processosBloqueados, sistemaOperacional, logger);

                processosBloqueados.getFila().addLast(processoExecutando);

                sistemaOperacional.incrementaTotalDeInstrucoesExecutadas();
                sistemaOperacional.incrementaTrocasRealizadas();
                sistemaOperacional.incrementaQuantidadeQuantum();

                break;
            }

            if (primeiraLetraComando == 'C') {
                lidaComando(processoExecutando);
                sistemaOperacional.incrementaTotalDeInstrucoesExecutadas();
            } else if (primeiraLetraComando == 'X' || primeiraLetraComando == 'Y') {
                lidaRegistradores(processoExecutando, comando);
                sistemaOperacional.incrementaTotalDeInstrucoesExecutadas();
            }

            processoExecutando.decrementaQuantumRestante();

            if (processoExecutando.getQuantumRestante() == 0) {
                colocaProcessoListaPronto(processoExecutando, sistemaOperacional);

                logger.logInterrompendoProcessos(processoExecutando);

                sistemaOperacional.incrementaTrocasRealizadas();
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