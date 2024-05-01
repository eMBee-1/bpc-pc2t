import java.util.List;

public class Ucebnice extends Kniha {

    private int rocnik;

    public Ucebnice(String nazov, List<String> autor, int rok, boolean dostupnost, int rocnik) {
        super(nazov, autor, rok, dostupnost);
        this.setRocnik(rocnik);
    }

	public int getRocnik() {
		return rocnik;
	}

	public void setRocnik(int rocnik) {
		this.rocnik = rocnik;
	}

}
