import estruturas.BCP;
import estruturas.ListaProcessos;

import java.util.Iterator;

public class Escalonador{
    public void incrementaProcessosExecutadosDosProcessosBloqueados(SistemaOperacional sistemaOperacional) {
        ListaProcessos processosBloqueados = sistemaOperacional.getProcessosBloqueados();

        for (BCP processo : processosBloqueados.getFila()) {
            processo.incrementaNProcessosExecutadosEnquantoBloqueado();
        }
    }

    public void escalonaProcessos(SistemaOperacional sistemaOperacional) {
        ListaProcessos processosProntos = sistemaOperacional.getProcessosProntos();
        ListaProcessos processosBloqueados = sistemaOperacional.getProcessosBloqueados();
        BCP processoExecutando = processosProntos.getFila().get(0);

        processoExecutando.setEstado("Executando");
        processosProntos.getFila().remove(0);

        lidaProcessosBloqueados(processosBloqueados, processosProntos);

        while (processoExecutando.getQuantumRestante() > 0) {
            String comando = processoExecutando.getSegmentoTexto().get(processoExecutando.getPc());
            char primeiraLetraComando = comando.charAt(0);

            if (primeiraLetraComando == 'S') {
                processoExecutando.setEstado("Finalizado");
                sistemaOperacional.getTabelaProcessos().getTabela().remove(processoExecutando);
                sistemaOperacional.incrementaProcessosFinalizados();
                break;
            }

            if (primeiraLetraComando == 'E') {
                lidaEntradaSaida(processoExecutando, processosBloqueados);
                processosBloqueados.getFila().add(processoExecutando);
                break;
            }

            if (primeiraLetraComando == 'C') {
                lidaComando(processoExecutando);
                if (!processoExecutando.getFezES())  processoExecutando.incrementaInstrucoesExecutadas();
            } else if (primeiraLetraComando == 'X' || primeiraLetraComando == 'Y') {
                lidaRegistradores(processoExecutando, comando);
            }

            processoExecutando.decrementaQuantumRestante();

            if (processoExecutando.getQuantumRestante() == 0) colocaProcessoListaPronto(
                processoExecutando,
                sistemaOperacional
            );
        }
    }

    public void colocaProcessoListaPronto(BCP processo, SistemaOperacional sistemaOperacional) {
        ListaProcessos processosProntos = sistemaOperacional.getProcessosProntos();

        processosProntos.getFila().add(processo);
        processo.setQuantumRestante(sistemaOperacional.getQuantum());
    }

    private void lidaEntradaSaida(BCP processoExecutando, ListaProcessos processosBloqueados) {
        for (BCP processo : processosBloqueados.getFila()) {
            processo.decrementaTempoEspera();
        }

        processoExecutando.setFezES(true);
        processoExecutando.setEstado("Bloqueado");
        processoExecutando.setTempoEspera(21);
        processoExecutando.incrementaPc();
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

                processosProntos.getFila().add(processo);
                iterator.remove();
            }
        }
    }
//    public void teste() {
//        int quantum = 0;
//
//        // Lê o arquivo do quantum e atribui a variavel
//        File arqQt = new File("./programas/quantum.txt");
//
//        try {
//            Scanner ltrQt = new Scanner(arqQt);
//            quantum = Integer.parseInt(ltrQt.nextLine());
//
//            ltrQt.close();
//        } catch(FileNotFoundException e) {
//            System.out.println("Não foi possível ler o arquivo quantum.");
//            e.printStackTrace();
//        }
//
//        // Cria o arquivo LogXX.txt
//        String pathLog = "./logs/";
//
//        if (quantum < 10) {
//            pathLog += "Log0" + quantum + ".txt";
//        } else {
//            pathLog += "Log" + quantum + ".txt";
//        }
//
//        File arqLog = new File(pathLog);
//
//        try {
//            arqLog.createNewFile();
//        } catch (IOException e) {
//            System.out.println("Não foi possível criar o arquivo LogXX");
//            e.printStackTrace();
//        }
//    }
}