import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;
import java.util.*;

public class Escalonador{

    public static List<List<String>> registros = new ArrayList<>();
    
    public static void lerProgramas(){
        
        registros.add(new ArrayList<>());

         for (int i = 1; i<=10; i++){
            registros.add(new ArrayList<>());
            String nPrograma = String.valueOf(i);
            String path = "programas/";

            if (i<10)
                path += "0" + nPrograma + ".txt";
            else
                path += nPrograma + ".txt";

            File aux = new File (path);
            try{
                Scanner scanner = new Scanner(aux);
                String linha = new String();

                while (!linha.equals("SAIDA")){
                    linha = scanner.nextLine();
                    registros.get(i).add(linha);
                }

                scanner.close();
            }catch(FileNotFoundException e){
                System.out.println("Não foi possível encontrar o arquivo");
                e.printStackTrace();
            }
         }
    }
    public static void main(String[] args) {

        lerProgramas();

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