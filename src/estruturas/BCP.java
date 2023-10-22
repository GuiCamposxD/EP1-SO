package src.estruturas;

import java.util.ArrayList;

public class BCP {
    private final String nomeProcesso;
    private final String nomePrograma;
    private final int id;
    private int pc;
    private String estado;
    private final ArrayList<String> segmentoTexto;
    private int registradorX;
    private int registradorY;
    private int tempoEspera;
    private int quantumRestante;

    //Getters
    public ArrayList<String> getSegmentoTexto() {
        return segmentoTexto;
    }
    public int getPc() {
        return pc;
    }

    public String getEstado() {
        return estado;
    }

    public int getRegistradorX() {
        return registradorX;
    }

    public int getRegistradorY() {
        return registradorY;
    }

    public String getNomeProcesso() {
        return this.nomeProcesso;
    }

    public int getId() {
        return id;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public int getQuantumRestante() {
        return quantumRestante;
    }

    //Setters
    public void setSegmentoTexto(String segmentoTexto) {
        this.segmentoTexto.add(segmentoTexto);
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setRegistradorX(int registradorX) {
        this.registradorX = registradorX;
    }

    public void setRegistradorY(int registradorY) {
        this.registradorY = registradorY;
    }

    public void incrementaPc() {
        this.pc++;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public void decrementaQuantumRestante() {
        this.quantumRestante--;
    }

    public void setQuantumRestante(int quantumRestante) {
        this.quantumRestante = quantumRestante;
    }

    public BCP(String nomeProcesso, String nomePrograma, int id, int quantum) {
        this.nomeProcesso = nomeProcesso;
        this.nomePrograma = nomePrograma;
        this.id = id;
        this.pc = 0;
        this.estado = "Pronto";
        this.registradorX = 0;
        this.registradorY = 0;
        this.segmentoTexto = new ArrayList<>();
        this.quantumRestante = quantum;
    }

    public void decrementaTempoEspera() {
        this.tempoEspera--;
    }

    public String getNomePrograma() {
        return nomePrograma;
    }
}
