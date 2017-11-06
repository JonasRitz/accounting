package application.accounting;
import java.util.*;
import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;




public class Accounting{
  public static double genauigkeit = 1000000.0;
  private static final Logger logger = Logger.getLogger(Accounting.class.getName());
  public static void main(String argv[]) throws FileNotFoundException {
    String log = "./src/data/log.txt";
    try{
        boolean append = true;
        FileHandler fh = new FileHandler(log, append);
        fh.setFormatter(new Formatter() {
            public String format(LogRecord rec){
                StringBuffer buf = new StringBuffer(1000);
                buf.append(new java.util.Date()).append(' ');
                buf.append(rec.getLevel()).append(' ');
                buf.append(formatMessage(rec)).append('\n');
                return buf.toString();
            }
        });
        logger.addHandler(fh);
    }catch(IOException e){
        logger.severe("Datei kann nicht geschrieben werden");
        e.printStackTrace();
    }
    logger.setLevel(Level.SEVERE);
    
    String datei;
    String zins;
    try{
        BufferedWriter bw;
        if(argv.length != 0){
            ArgParser p1 = new ArgParser(argv);
            datei = p1.getInputFilename();
            zins = p1.getNonOptions().replace(",",".");
            Depositor.setzeZinsen(Double.parseDouble(zins));
            FileWriter fw = new FileWriter(p1.getOutputFilename());
            bw = new BufferedWriter(fw);
        }else{
            bw = new BufferedWriter(new OutputStreamWriter(System.out));
            Scanner sc = new Scanner(System.in);
            datei = sc.nextLine();
            zins  = sc.nextLine();
            Depositor.setzeZinsen(Double.parseDouble(zins));
        }
        
        logger.info("lese von Datei: " + datei);
        ArrayList<Depositor> mitglieder = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(datei))){
        String line;
        while( (line=br.readLine()) != null){
            logger.info("gelesene Zeile: " + line);
            if( line.length() != 0 && line.charAt(0) >= '0' && line.charAt(0) <='9'){
            Depositor aktuell;
            String values[] = line.split(";");
            ArrayList<AccountingEntry> liste = new ArrayList<>();
            double start = Double.parseDouble(values[3].replace(',','.'));
            aktuell = new Depositor(values[0], values[1], values[2], (long) (start*genauigkeit), liste);
            for(int i=4; i<values.length-1; i+=2){
                double d = Double.parseDouble(values[i+1].replace(',','.'));
                aktuell.einzahlen(Integer.parseInt(values[i]), (long)(d*genauigkeit));
            }
            aktuell.berechneGuthaben(aktuell.getZins());
            mitglieder.add(aktuell);
            }else{
                bw.write(line+"\n");
            }
        }

        }catch(IOException e){
            e.printStackTrace();
        System.out.println("Datei nicht gefunden");
            }
        
        for(Depositor dep : mitglieder){
            bw.write(dep.toString() + "\n");
        }
        bw.flush();
    }catch(IOException e){
        e.printStackTrace();
    }

  }
}
