import java.util.List;

public class Ucebnice extends Kniha {

    private int rocnik;

    public Ucebnice(String nazov, List<String> autori, int rocnik, int rok, String dostupnost, String typKnihy) {
    	super(nazov, autori, rok, dostupnost, typKnihy); 
        this.rocnik = rocnik;
    }

    public int getRocnik() {
		return rocnik;
	}

	public void setRocnik(int rocnik) {
		this.rocnik = rocnik;
	}

}
