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

    //Setters
    public void setSegmentoTexto(String segmentoTexto) {
        this.segmentoTexto.add(segmentoTexto);
    }

    public BCP(String nome, int id) {
        this.nomeProcesso = nome;
        this.id = id;
        this.pc = 0;
        this.estado = "Pronto";
        this.registradorX = 0;
        this.registradorY = 0;
        this.segmentoTexto = new ArrayList<>();
    }
}
