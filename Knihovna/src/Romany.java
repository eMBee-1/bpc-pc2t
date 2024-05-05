import java.util.List;

public class Romany extends Kniha {

    private String zaner;
    public Romany(String nazov, List<String> autori, String zaner, int rok, String dostupnost, String typKnihy) {
    	super(nazov, autori, rok, dostupnost, typKnihy); 
        this.zaner = zaner;

    }
    public String getZaner() {
        return zaner;
    }

    public void setZaner(String zaner) {
        this.zaner = zaner;
    }
}


