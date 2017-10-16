package application.accounting;
import java.util.*;
import java.io.*;
public class AccountingEntry{
  private int tag;
  private long betrag;
  public AccountingEntry(int tag, long betrag){
    this.tag = tag;
    this.betrag = betrag;
  }
  public int gettag(){
    return this.tag;
  }
  public long getbetrag(){
    return this.betrag;
  }
}
