package estruturas;
public class BCP {
    private final String nomeProcesso;
    private final int id;
    private int pc;
    private String estado;
    private int regX;
    private int regY;

    public String getNomeProcesso() {
        return this.nomeProcesso;
    }

    public BCP(String nome, int id) {
        this.nomeProcesso = nome;
        this.id = id;
        this.pc = 0;
        this.estado = "Pronto";
        this.regX = 0;
        this.regY = 0;
    }
}
