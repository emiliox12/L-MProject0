package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	
	private static ArrayList<String> cadena = new ArrayList<String>();
	
	public static void leerArchivo(String direccion) throws FileNotFoundException, IOException {
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
        for(int i=0 ; i<cadena.size(); i++) {
        	System.out.println(cadena.get(i));
        }
        b.close();
    }
	
	public static void main(String[] args) throws IOException
	{
		InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);

        try {
        	System.out.print("Ingrese la ruta del archivo (incluya el .txt) \n"+"o si no escriba ´test´ para utilizar el test \n");
            String dir = br.readLine();
            if(dir.equals("test")) dir = "data/EntradaComandosRobot.txt";
            
            leerArchivo(dir);
        }catch (FileNotFoundException e) { System.out.println("ERROR: " + e.getMessage()); }
		
	}
}
