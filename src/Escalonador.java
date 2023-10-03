import estruturas.TabelaProcessos;

import java.io.*;
import java.util.*;

public class Escalonador{
    private static List<List<String>> programas = new ArrayList<>();
    private static TabelaProcessos processos = new TabelaProcessos();
    
    public static void lerProgramas() {
        String path = "./programas";
        File pasta = new File(path);

        if (pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles();

            if (arquivos != null) {
                Arrays.sort(arquivos, Comparator.comparing(File::getName));

                for (int i = 0; i < arquivos.length; i++) {
                    File arquivo = new File(arquivos[i].toURI());
                    if (!arquivos[i].getName().equals("quantum.txt")) {
                        programas.add(new ArrayList<>());

                        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
                            String linha;
                            while ((linha = leitor.readLine()) != null) {
                                programas.get(i).add(linha);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("O caminho especificado não é uma pasta.");
        }
    }

    public static void main(String[] args) {
        lerProgramas();

        for (List<String> programa : programas) {
            System.out.println(programa);
        }

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

        Escalonador.processos = new TabelaProcessos();

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