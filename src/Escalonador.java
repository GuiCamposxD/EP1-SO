import estruturas.BCP;
import estruturas.ListaProcessos;

import java.io.*;
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

        for (int i = processoExecutando.getQuantumRestante(); i > 0; i--) {
            String comando = processoExecutando.getSegmentoTexto().get(processoExecutando.getPc());
            char primeiraLetraComando = comando.charAt(0);

            if (primeiraLetraComando == 'S') {
                processoExecutando.setEstado("Finalizado");
                sistemaOperacional.incrementaProcessosFinalizados();
            }

            if (primeiraLetraComando == 'E') {
                lidaEntradaSaida(processoExecutando, processosBloqueados);
                processosBloqueados.getFila().add(processoExecutando);
            }

            if (primeiraLetraComando == 'C' && !processoExecutando.getFezES()) {
                lidaComando(processoExecutando);
            } else if (primeiraLetraComando == 'X' || primeiraLetraComando == 'Y') {
                lidaRegistradores(processoExecutando, comando);
            }
        }
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
        processoExecutando.incrementaInstrucoesExecutadas();
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