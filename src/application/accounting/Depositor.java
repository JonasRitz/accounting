package application.accounting;
import java.util.*;
import java.io.*;
public class Depositor{
  private static double zinssatz;
  private String nummer;
  private String nachname;
  private String vorname;
  private long startguthaben;
  private List<AccountingEntry> einzahlungen;

  public Depositor(String nummer, String nachname, String vorname, long startguthaben, List<AccountingEntry> einzahlungen){
    this.nummer = nummer;
    this.nachname = nachname;
    this.vorname = vorname;
    this.startguthaben = startguthaben;
    this.einzahlungen = einzahlungen;
  }

  public static void setzeZinsen(double zs){
    zinssatz = zs;
  }

  public static double getZins(){
    return zinssatz;
  }

  public double berechneGuthaben(double zs){
    this.startguthaben =(long) (this.startguthaben * ( (zs/100.0) + 1));
    for(AccountingEntry entry : this.einzahlungen){
      this.startguthaben += entry.getbetrag() * ( (zs/100.0) * ((360-entry.gettag())/360) + 1);
    }
    return this.startguthaben;
  }

  public void einzahlen(int tag, long betrag){
    this.einzahlungen.add(new AccountingEntry(tag, betrag));
  }

  public String toString(){
    double guthaben = this.startguthaben / Accounting.genauigkeit;
    guthaben *= 100;
    guthaben = Math.round(guthaben);
    guthaben/=100.0;
    String gut = guthaben + "";
    gut = gut.replace('.',',');
    return this.nummer + ";" + this.nachname + ";" + this.vorname + ";" + gut;
  }
}
