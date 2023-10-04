public class Main {
    public static void main(String[] args) {
        SistemaOperacional sistemaOperacional = new SistemaOperacional();

        sistemaOperacional.executaProcessos(sistemaOperacional);

        System.out.println(sistemaOperacional);
    }
}
