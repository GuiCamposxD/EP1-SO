package src;

import src.estruturas.BCP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private int trocasRealizadas;
    private int instruncoesRealizadas;
    private String logFileName;
    File log;
    FileWriter logWriter;
    public Logger(int quantum){
        this.trocasRealizadas = 0;
        this.instruncoesRealizadas = 0;
        String fileName;
        if(quantum < 10){
            logFileName = "logs/log0" + quantum + ".txt";
        } else{
            logFileName = "logs/log" + quantum + ".txt";
        }

        log = new File(logFileName);

        if (log.exists() && log.isFile()) {
            log.delete();
        }

        try {
            log.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void logCarregaProcessos(BCP processo){
        try {
            logWriter = new FileWriter(log, true);
            logWriter.write("Carregando " + processo.getNomePrograma() + "\n");
            logWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void logExecutandoProcessos(BCP processo){
        try {
            logWriter = new FileWriter(log, true);
            logWriter.write("Executando " + processo.getNomePrograma() + "\n");
            logWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void logInterrompendoProcessos(BCP processo, SistemaOperacional sistemaOperacional){
        instruncoesRealizadas += sistemaOperacional.getQuantum() - processo.getQuantumRestante();
        try {
            this.trocasRealizadas++;
            logWriter = new FileWriter(log, true);
            logWriter.write("Interrompendo " + processo.getNomePrograma() + " após " + processo.getQuantumRestante() + " instruções\n");
            logWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void logFinalizaProcessos(BCP processo){
        try {
            logWriter = new FileWriter(log, true);
            logWriter.write(processo.getNomePrograma() + " terminado. X=" + processo.getRegistradorX() + ". Y=" + processo.getRegistradorY() + "\n");
            logWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void logESProcessos(BCP processo){
        try {
            logWriter = new FileWriter(log, true);
            logWriter.write("E/S iniciada em " + processo.getNomePrograma() + "\n");
            logWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    void logSaida(SistemaOperacional sistemaOperacional){
        try {
            logWriter = new FileWriter(log, true);
            logWriter.write("MEDIA DE TROCAS: " + ((float) trocasRealizadas)/( (float) sistemaOperacional.getQuantidadeProcessos()) + "\n");
            logWriter.write("MEDIA DE INSTRUCOES: " + ((float) instruncoesRealizadas)/( (float) sistemaOperacional.getQuantidadeProcessos()) + "\n");
            logWriter.write("QUANTUM: " + sistemaOperacional.getQuantum() + "\n");
            logWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
