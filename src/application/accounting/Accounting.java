package application.accounting;
import java.util.*;
import java.io.*;
public class Accounting{
  public static double genauigkeit = 1000000.0;

  public static void main(String argv[]) throws FileNotFoundException {
    String datei;
    String zins;
    if(argv.length != 0){
        ArgParser p1 = new ArgParser(argv);
        datei = p1.getInputFilename();
        zins = p1.getNonOptions().replace(",",".");
        Depositor.setzeZinsen(Double.parseDouble(zins));
    }else{
        Scanner sc = new Scanner(System.in);
        datei = sc.nextLine();
        zins  = sc.nextLine();
        Depositor.setzeZinsen(Double.parseDouble(zins));
    }
    
    ArrayList<Depositor> mitglieder = new ArrayList<>();
    try(BufferedReader br = new BufferedReader(new FileReader(datei))){
      String line;
      while( (line=br.readLine()) != null){
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
          System.out.println(line);
        }
      }

    }catch(IOException e){
      e.printStackTrace();
      System.out.println("Datei nicht gefunden");
    }
    for(Depositor dep : mitglieder){
      System.out.println(dep);
    }

  }
}
