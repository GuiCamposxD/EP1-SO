package estruturas;
public class ProcessosBloqueados extends Processos{
    private int x, y;
    String nome;

    public ProcessosBloqueados(int x, int y, String nome){ 
        super(x, y, nome); 
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
