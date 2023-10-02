import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

public class Escalonador{
    public static void main(String[] args) {
        int quantum = 0;

        // Lê o arquivo do quantum e atribui a variavel
        File arqQt = new File("../programas/quantum.txt");

        try {
            Scanner ltrQt = new Scanner(arqQt);
            quantum = Integer.parseInt(ltrQt.nextLine());

            ltrQt.close();
        } catch(FileNotFoundException e) {
            System.out.println("Não foi possível ler o arquivo quantum.");
            e.printStackTrace();
        }

        // Cria o arquivo LogXX.txt
        String pathLog = "../programas/";

        if(quantum < 10) {
            pathLog += "Log0" + quantum + ".txt";
        } else {
            pathLog += "Log" + quantum + ".txt";
        }

        File arqLog = new File(pathLog);

        try {
            arqLog.createNewFile();
        } catch(IOException e) {
            System.out.println("Não foi possível criar o arquivo LogXX");
            e.printStackTrace();
        }
    }
}