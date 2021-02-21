package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Model.Lexer;

public class Controller {
	
	private ArrayList<String> cadena;
	private Lexer lexer;
	

	public Controller() {
		cadena = new ArrayList<String>();
        lexer = new Lexer();
	}
	
	public void run() {
		InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);

        try {
        	System.out.print("Ingrese la ruta del archivo (incluya el .txt) \n"+"o si no escriba �test� para utilizar el test \n");
            String dir = br.readLine();
            if(dir.equals("test")) dir = "data/EntradaComandosRobot.txt";
            
            leerArchivo(dir);
        }catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
	}
	
	public void leerArchivo(String direccion) throws FileNotFoundException, IOException {
        FileReader f = new FileReader(direccion);
        BufferedReader b = new BufferedReader(f);
        boolean termino = false;
        while(termino == false) {
        	String line = b.readLine();
        	if(line != null) {
        		cadena.add(line);
        	}
        	else {
        		termino = true;
        	}    
        }
        String commands = "";
        for(int i=0 ; i<cadena.size(); i++) {
        	commands += " " + cadena.get(i);
        }
        boolean result = lexer.validateString(commands);
        if (result) {
        	System.out.println("valid file");
        } else {
        	System.out.println("invalid file");
        }
        b.close();
    }

}
