import java.io.*;
import java.util.Scanner;

public class Escalonador{
    private int quantum;

    public Escalonador() {
        this.setQuantum();
    }

    private void setQuantum() {
        try (BufferedReader leitor = new BufferedReader(new FileReader("./programas/quantum.txt"))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                this.quantum = Integer.parseInt(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teste() {
        int quantum = 0;

        // Lê o arquivo do quantum e atribui a variavel
        File arqQt = new File("./programas/quantum.txt");

        try {
            Scanner ltrQt = new Scanner(arqQt);
            quantum = Integer.parseInt(ltrQt.nextLine());

            ltrQt.close();
        } catch(FileNotFoundException e) {
            System.out.println("Não foi possível ler o arquivo quantum.");
            e.printStackTrace();
        }

        // Cria o arquivo LogXX.txt
        String pathLog = "./logs/";

        if (quantum < 10) {
            pathLog += "Log0" + quantum + ".txt";
        } else {
            pathLog += "Log" + quantum + ".txt";
        }

        File arqLog = new File(pathLog);

        try {
            arqLog.createNewFile();
        } catch (IOException e) {
            System.out.println("Não foi possível criar o arquivo LogXX");
            e.printStackTrace();
        }
    }
}