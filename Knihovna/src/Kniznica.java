import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



public class Kniznica {

    private List<Kniha> books = new ArrayList<>(); 
    private Database db;

    public Kniznica(Database db) {
        this.db = db;
    }

    public void loadBooks() {
        if (db.connect() != null) {
            this.books = db.nacitajDatabazu();
            db.disconnect();
        } else {
            System.err.println("Nepodarilo sa pripojiť k databáze na začiatku.");
        }
    }
    
    public void aktualizujKnihu(String nazov, List<String> autori, Integer rok, String dostupnost) {
        for (Kniha kniha : books) {
            if (kniha.getNazov().equals(nazov)) {
                if (autori != null && !autori.isEmpty()) {
                    kniha.setAutori(autori);
                }
                if (rok != null) {
                    kniha.setRok(rok);
                }
                if (dostupnost != null) {
                    kniha.setDostupnost(dostupnost);
                }
                break; 
            }
        }
    }


    public void ulozZmeny() {
        if (db.connect() != null) {
            db.ulozDatabazu(books);
            db.disconnect();
        } else {
            System.err.println("Nepodarilo sa pripojiť k databáze pri ukončení.");
        }
    }

    public void pridajKnihu(Kniha kniha) {
        books.add(kniha);
        System.out.println("Kniha '" + kniha.getNazov() + "' bola pridaná do knižnice.");
    }

    public void odstranKnihu(String nazov) {
        boolean bolOdstraneny = books.removeIf(kniha -> kniha.getNazov().equals(nazov));
        if (bolOdstraneny) {
            System.out.println("Kniha '" + nazov + "' bola úspešne odstránená z knižnice.");
        } else {
            System.out.println("Kniha '" + nazov + "' sa v knižnici nenachádza.");
        }
    }

    public void vypisKnihy() {
        Collections.sort(books, Comparator.comparing(Kniha::getNazov));
        for (Kniha kniha : books) {
            System.out.print("Nazov: " + kniha.getNazov() + ", Autor: " + String.join(", ", kniha.getAutori()) + ", ");
            if (kniha instanceof Romany) {
                System.out.print("Zaner: " + ((Romany) kniha).getZaner() + ", ");
            } else if (kniha instanceof Ucebnice) {
                System.out.print("Rocnik: " + ((Ucebnice) kniha).getRocnik() + ", ");
            }
            System.out.println("Rok: " + kniha.getRok() + ", Dostupnost: " + kniha.getDostupnost());
        }
    }

    public Kniha vyhladajKnihu(String nazov) {
        return books.stream()
            .filter(k -> k.getNazov().equalsIgnoreCase(nazov))
            .findFirst()
            .orElse(null);
    }

    public void vypisKnihyPodlaAutora(String autor) {
        List<Kniha> filteredBooks = books.stream()
            .filter(kniha -> kniha.getAutori().contains(autor))
            .collect(Collectors.toList());
        if (filteredBooks.isEmpty()) {
            System.out.println("Žiadne knihy nenájdené pre autora: " + autor);
        } else {
            filteredBooks.forEach(this::zobrazDetaily);
        }
    }

    public void vypisKnihyPodlaZanru(String zaner) {
        List<Kniha> filteredBooks = books.stream()
            .filter(kniha -> kniha instanceof Romany && ((Romany)kniha).getZaner().equalsIgnoreCase(zaner))
            .collect(Collectors.toList());
        if (filteredBooks.isEmpty()) {
            System.out.println("Žiadne knihy nenájdené pre žáner: " + zaner);
        } else {
            filteredBooks.forEach(this::zobrazDetaily);
        }
    }

    public void vypisVypozicaneKnihy() {
        List<Kniha> borrowedBooks = books.stream()
            .filter(kniha -> kniha.getDostupnost().equalsIgnoreCase("vypozicana"))
            .collect(Collectors.toList());
        if (borrowedBooks.isEmpty()) {
            System.out.println("Žiadne vypožičané knihy.");
        } else {
            borrowedBooks.forEach(this::zobrazDetaily);
        }
    }

    public void zmenDostupnostKnihy(String nazov, String novaDostupnost) {
        Kniha kniha = vyhladajKnihu(nazov);
        if (kniha != null) {
            kniha.setDostupnost(novaDostupnost);
        } else {
            System.out.println("Knihu s názvom '" + nazov + "' sa nepodarilo nájsť.");
        }
    }

    private void zobrazDetaily(Kniha kniha) {
        System.out.println("Nazov: " + kniha.getNazov() + ", Autori: " + String.join(", ", kniha.getAutori()) + ", Zaner: " + ((Romany)kniha).getZaner() + ", Rok: " + kniha.getRok() + ", Dostupnost: " + kniha.getDostupnost());
    }

    public void ulozKnihuDoSuboru(String nazovKnihy) {
        Kniha kniha = vyhladajKnihu(nazovKnihy);
        if (kniha == null) {
            System.out.println("Knihu s názvom '" + nazovKnihy + "' sa nepodarilo nájsť.");
        } else {
            try (PrintWriter out = new PrintWriter(new FileWriter(nazovKnihy + ".txt"))) {
                out.println("Názov: " + kniha.getNazov());
                out.println("Autori: " + String.join(", ", kniha.getAutori()));
                if (kniha instanceof Romany) {
                    out.println("Žáner: " + ((Romany) kniha).getZaner());
                } else if (kniha instanceof Ucebnice) {
                    out.println("Ročník: " + ((Ucebnice) kniha).getRocnik());
                }
                out.println("Rok: " + kniha.getRok());
                out.println("Dostupnosť: " + kniha.getDostupnost());
                out.println("Typ: " + kniha.getTyp());
            } catch (IOException e) {
                System.out.println("Chyba pri zapisovaní do súboru: " + e.getMessage());
            }
        }
    }

    public void nacitajKnihuZoSuboru(String nazovKnihy) {
        String nazovSuboru = nazovKnihy + ".txt"; 
        try (BufferedReader reader = new BufferedReader(new FileReader(nazovSuboru))) {
            String line;
            System.out.println("Informácie o knihe '" + nazovKnihy + "':");
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; 
                System.out.println(line); 
            }
        } catch (IOException e) {
            System.out.println("Chyba pri čítaní zo súboru '" + nazovSuboru + "': " + e.getMessage());
        }
    }

}
