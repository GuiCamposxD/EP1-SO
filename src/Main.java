package src;

public class Main {
    public static void main(String[] args) {
        SistemaOperacional sistemaOperacional = new SistemaOperacional();

        sistemaOperacional.executaProcessos(sistemaOperacional, sistemaOperacional.getLogger());

        System.out.println(sistemaOperacional);
    }
}
