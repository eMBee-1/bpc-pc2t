import java.util.List;

abstract class Kniha {
    String nazov;
    List<String> autor;
    int rok;
    boolean dostupnost;

    
    public Kniha(String nazov, List<String> autor, int rok, boolean dostupnost) {
        this.nazov = nazov;
        this.autor = autor;
        this.rok = rok;
        this.dostupnost = dostupnost;
    }


    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public List<String> getAutor() {
        return autor;
    }

    public void setAutor(List<String> autor) {
        this.autor = autor;
    }

    public int getRok () {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public boolean getDostupnost() {
        return dostupnost;
    }

    public void setDostupnost(boolean dostupnost) {
        this.dostupnost = dostupnost;
    }
}