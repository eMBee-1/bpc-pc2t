import java.util.List;

public class Kniha {
    private String nazov;
    private List<String> autori;
    private int rok;
    private String dostupnost;
    private String typKnihy; 



    public Kniha(String nazov, List<String> autori, int rok, String dostupnost, String typKnihy) {
        this.nazov = nazov;
        this.autori = autori;
        this.rok = rok;
        this.dostupnost = dostupnost;
        this.typKnihy = typKnihy;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public List<String> getAutori() {
        return autori;
    }

    public void setAutori(List<String> autori) {
        this.autori = autori;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public String getDostupnost() {
        return dostupnost;
    }

    public void setDostupnost(String dostupnost) {
        this.dostupnost = dostupnost;
    }

    public String getTyp() {
        return typKnihy;
    }

    public void setTyp(String typKnihy) {
        this.typKnihy = typKnihy;
    }
}
