import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Kniznica {

    public Database db;

    public Kniznica(Database db) {
        this.db = db;
    }

    public void pridajKnihu(Kniha kniha) {
        if (db.connect() != null) {
            db.addKniha(kniha);
            db.disconnect();
        } else {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
        }
    }

    public void odstranKnihu(String nazov) {
        if (db.connect() != null) {
            db.removeBook(nazov);
            db.disconnect();
        } else {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
        }
    }

    public void vypisKnihy() {
        if (db.connect() != null) {
            List<Kniha> books = db.getAllBooks();
            db.disconnect();
            

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
        } else {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
        }
    }

    public void vypisKnihyPodlaAutora(String autor) {
        if (db.connect() != null) {
            List<Kniha> books = db.getBooksByAuthor(autor);
            db.disconnect();
            if (books.isEmpty()) {
                System.out.println("Žiadne knihy nenájdené pre autora: " + autor);
            } else {
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
        } else {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
        }
    }
    
    public void vypisKnihyPodlaZanru(String zaner) {
        if (db.connect() != null) {
            List<Kniha> books = db.getBooksByGenre(zaner);
            db.disconnect();
            if (books.isEmpty()) {
                System.out.println("Žiadne knihy nenájdené pre žáner: " + zaner);
            } else {
                for (Kniha kniha : books) {
                    System.out.println("Nazov: " + kniha.getNazov() + ", Autor: " + String.join(", ", kniha.getAutori()) + ", Zaner: " + zaner + ", Rok: " + kniha.getRok() + ", Dostupnost: " + kniha.getDostupnost());
                }
            }
        } else {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
        }
    }
 
    public void vyhladajKnihu(String nazov) {
        Kniha kniha = db.findBookByName(nazov);
        if (kniha == null) {
            System.out.println("Knihu s názvom '" + nazov + "' sa nepodarilo nájsť.");
        } else {
            System.out.println("Nájdená kniha:");
                System.out.print("Nazov: " + kniha.getNazov() + ", Autor: " + String.join(", ", kniha.getAutori()) + ", ");
                if (kniha instanceof Romany) {
                    System.out.print("Zaner: " + ((Romany) kniha).getZaner() + ", ");
                } else if (kniha instanceof Ucebnice) {
                    System.out.print("Rocnik: " + ((Ucebnice) kniha).getRocnik() + ", ");
                }
                System.out.println("Rok: " + kniha.getRok() + ", Dostupnost: " + 
                kniha.getDostupnost());
            }        
    }
    
    public void ulozKnihuDoSuboru(String nazovKnihy) {
        Kniha kniha = db.findBookByName(nazovKnihy);
        if (kniha == null) {
            System.out.println("Knihu s názvom '" + nazovKnihy + "' sa nepodarilo nájsť.");
        } else {
            try (PrintWriter out = new PrintWriter(new FileWriter(nazovKnihy + ".txt"))) {
                out.println("Názov: " + kniha.getNazov());
                out.println("Autori: " + String.join(", ", kniha.getAutori()));
                out.println("Typ: " + kniha.getTyp());
                if (kniha instanceof Romany) {
                    out.println("Žáner: " + ((Romany) kniha).getZaner());
                } else if (kniha instanceof Ucebnice) {
                    out.println("Ročník: " + ((Ucebnice) kniha).getRocnik());
                }
                out.println("Rok: " + kniha.getRok());
                out.println("Dostupnosť: " + kniha.getDostupnost());
                System.out.println("Informácie o knihe boli uložené do súboru 'Detail_knihy.txt'.");
            } catch (IOException e) {
                System.out.println("Chyba pri zapisovaní do súboru: " + e.getMessage());
            }
        }
    }
    
    public void nacitajKnihuZoSuboru(String cestaSuboru) {
        String cestaSuboru2 = cestaSuboru; 
        try (BufferedReader reader = new BufferedReader(new FileReader(cestaSuboru2))) {
            String line;
            String nazov = "";
            List<String> autori = null;
            int rok = 0;
            String dostupnost = "";
            String zaner = "";
            String typKnihy = "";
            int rocnik = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Názov: ")) {
                    nazov = line.split(": ")[1];
                } else if (line.startsWith("Autori: ")) {
                    autori = Arrays.asList(line.split(": ")[1].split(", "));
                } else if (line.startsWith("Rok: ")) {
                    rok = Integer.parseInt(line.split(": ")[1]);
                } else if (line.startsWith("Dostupnosť: ")) {
                    dostupnost = line.split(": ")[1];
                } else if (line.startsWith("Typ: ")) {
                    typKnihy = line.split(": ")[1];
                } else if (line.startsWith("Žáner: ")) {
                    zaner = line.split(": ")[1];
                } else if (line.startsWith("Rocnik: ")) {
                    rocnik = Integer.parseInt(line.split(": ")[1]);
                }
            }

            if (!nazov.isEmpty()) {
                if (typKnihy.equals("Román")) {
                    Kniha kniha = new Romany(nazov, autori, zaner, rok, dostupnost, typKnihy);
                    System.out.println("Názov: " + kniha.getNazov());
                    System.out.println("Autori: " + String.join(", ", kniha.getAutori()));
                    System.out.println("Rok: " + kniha.getRok());
                    System.out.println("Dostupnosť: " + (kniha.getDostupnost()));
                    System.out.println("Žáner: " + ((Romany) kniha).getZaner());
                    System.out.println("Typ: " + kniha.getTyp());

                } else if (typKnihy.equals("Učebnica")) {
                    Kniha kniha = new Ucebnice(nazov, autori, rocnik, rok, dostupnost, typKnihy);
                    System.out.println("Názov: " + kniha.getNazov());
                    System.out.println("Autori: " + String.join(", ", kniha.getAutori()));
                    System.out.println("Rok: " + kniha.getRok());
                    System.out.println("Dostupnosť: " + (kniha.getDostupnost()));
                    System.out.println("Rocnik: " + ((Ucebnice) kniha).getRocnik());
                    System.out.println("Typ: " + kniha.getTyp());
                }

            } else {
                System.out.println("Súbor neobsahuje žiadne údaje o knihe.");
            }

        } catch (IOException e) {
            System.out.println("Chyba pri čítaní zo súboru: " + e.getMessage());
        } 
    } 
    
    public void vypisVypozicaneKnihy() {
        List<Kniha> borrowedBooks = db.getBorrowedBooks();
        if (borrowedBooks.isEmpty()) {
            System.out.println("Žiadne vypožičané knihy.");
        } else {
            for (Kniha kniha : borrowedBooks) {
                System.out.println("Názov: " + kniha.getNazov() + ", Typ: " + kniha.getTyp());
            }
        } 
    

    }
    
    public void zmenDostupnostKnihy(String nazov3, String dostupnost3) {
        if (db.connect() != null) {
            db.updateBookAvailability(nazov3, dostupnost3);
            db.disconnect();
        } else {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
        }
    }
}
