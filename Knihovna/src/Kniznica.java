import java.util.HashMap;
import java.util.List;

public class Kniznica {

    private HashMap<String, Kniha> knihy;

    public Kniznica() {
        this.knihy = new HashMap<>();
    }

    public void pridajKnihu(Kniha kniha) {
        this.knihy.put(kniha.getNazov(), kniha);
    }

    public void upravKnihu(String nazov, List<String> autor, int rok, boolean dostupnost) {
        Kniha kniha = this.knihy.get(nazov);
        if (kniha != null) {
            kniha.setAutor(autor);
            kniha.setRok(rok);
            kniha.setDostupnost(dostupnost);
        }
        else {
            System.out.println("Kniha s nazvom " + nazov + " neexistuje.");
        }
    	
    }

    // TODO - odstran knihu

    // TODO - zmen dostupnost knihy

    public void vypisKnihy() {
        for (Kniha kniha : this.knihy.values()) {
            System.out.println("Nazov: " + kniha.getNazov() + ", Autor: " + kniha.getAutor() + ", Rok: " + kniha.getRok() + ", Dostupnost: " + kniha.getDostupnost());
        }
    }

    

}
