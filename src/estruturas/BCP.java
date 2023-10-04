package estruturas;

import java.util.ArrayList;

public class BCP {
    private final String nomeProcesso;
    private final int id;
    private int pc;
    private String estado;
    private ArrayList<String> segmentoTexto;
    private int registradorX;
    private int registradorY;
    private boolean fezES;
    private int instrucoesExecutadas;
    private int tempoEspera;
    private int nProcessosExecutadosEnquantoBloqueado;
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

    public boolean getFezES() {
        return fezES;
    }

    public int getInstrucoesExecutadas() {
        return instrucoesExecutadas;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public int getNProcessosExecutadosEnquantoBloqueado() {
        return nProcessosExecutadosEnquantoBloqueado;
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

    public void incrementaInstrucoesExecutadas() {
        this.instrucoesExecutadas++;
    }

    public void incrementaNProcessosExecutadosEnquantoBloqueado() {
        this.nProcessosExecutadosEnquantoBloqueado++;
    }

    public void setFezES(boolean fezES) {
        this.fezES = fezES;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public void setQuantumRestante(int quantumRestante) {
        this.quantumRestante = quantumRestante;
    }

    public BCP(String nome, int id, int quantum) {
        this.nomeProcesso = nome;
        this.id = id;
        this.pc = 0;
        this.estado = "Pronto";
        this.registradorX = 0;
        this.registradorY = 0;
        this.segmentoTexto = new ArrayList<>();
        this.fezES = false;
        this.instrucoesExecutadas = 0;
        this.quantumRestante = quantum;
    }

    public void decrementaTempoEspera() {
        this.tempoEspera--;
    }
}
