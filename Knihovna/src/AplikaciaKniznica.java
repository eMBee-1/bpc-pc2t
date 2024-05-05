import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AplikaciaKniznica extends Kniznica {
    private Database db;

    public AplikaciaKniznica(Database db) {
        super(db);
        this.db = db;
       
        if (db.connect() != null) {
            db.createTables();
            List<Kniha> loadedBooks = db.nacitajDatabazu();
            db.disconnect();
            super.loadBooks();
        } else {
            System.err.println("Databaza nebola vytvorena.");
        }
    }

    public static void main(String[] args) {
        AplikaciaKniznica aplikacia = new AplikaciaKniznica(new Database());
        Scanner sc = new Scanner(System.in);
        boolean program = true;

        System.out.println("--------------------------------------");
        System.out.println("Vitajte v aplikacii na spravu kniznice");
        System.out.println("--------------------------------------");


        while (program) {
            System.out.println("1 - Pridajte knihu");
            System.out.println("2 - Upravte knihu");
            System.out.println("3 - Odstrante knihu");
            System.out.println("4 - Zmente dostupnost knihy");
            System.out.println("5 - Vypiste knihy");
            System.out.println("6 - Vyhladajte knihu");
            System.out.println("7 - Vypiste knihy podla autora");
            System.out.println("8 - Vypiste knihy podla zanru");
            System.out.println("9 - Vypiste vypozicane knihy");
            System.out.println("10 - Ulozit do suboru");
            System.out.println("11 - Nacitat zo suboru");
            System.out.println("12 - Ukoncit");
            int moznost = 0;

            while (true) {
                System.out.println("Zadajte moznost: ");
                try {
                    moznost = sc.nextInt();
                    if (moznost < 1 || moznost > 12) {
                        System.err.println("CHYBA! Zadali ste neplatnú hodnotu.");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("CHYBA! Zadali ste neplatnú hodnotu.");
                    sc.next(); 
                }
            }
            sc.nextLine(); 
                                           
            switch (moznost) {
                case 1:
                    System.out.println("Zadajte pocet autorov knihy: ");
                    int pocetAutorov = 0;
                    
                    while (true) {
                        try {
                            pocetAutorov = sc.nextInt();
                            sc.nextLine();  
                            break;
                        } catch (InputMismatchException e) {
                            System.err.println("CHYBA! Zadali ste neplatnú hodnotu.");
                            sc.next();
                        }
                    }

                    List<String> autor = new ArrayList<>(pocetAutorov);
                    for (int i = 0; i < pocetAutorov; i++) {
                        System.out.println("Zadajte autora: ");
                        String menoAutora;
                        while (true) {
                            menoAutora = sc.nextLine();
                            if (menoAutora.matches("^[a-zA-ZáéíóúýčďľňřšťžůäěöüßÁÉÍÓÚÝČĎĽŇŘŠŤŽŮÄĚÖÜß]+(?:\\s+[a-zA-ZáéíóúýčďľňřšťžůäěöüßÁÉÍÓÚÝČĎĽŇŘŠŤŽŮÄĚÖÜß]+)*$")) {
                                autor.add(menoAutora);
                                break;
                            } else {
                                System.err.println("CHYBA! Zadajte meno autora.");
                            }
                        }
                    }
                    
                    int typ = 0;
                    while (true) {
                        System.out.println("Zadajte typ knihy: ");
                        System.out.println("1 - Romany");
                        System.out.println("2 - Ucebnice");

                        if (sc.hasNextInt()) {
                            typ = sc.nextInt();
                            sc.nextLine();
                            if (typ == 1 || typ == 2) {
                                break;
                            } else {
                                System.err.println("CHYBA! Zadali ste neplatnú hodnotu.");
                            }
                        } else {
                            System.err.println("CHYBA! Zadali ste neplatnú hodnotu.");
                            sc.next();  
                        }
                    }

                    String typKnihy = (typ == 1) ? "ROMAN" : "UCEBNICA";
                    System.out.println("Zadajte nazov knihy: ");
                    String nazov = sc.nextLine();
                    System.out.println("Zadajte rok vydania: ");
                    int rok = 0;
                    boolean validnyRok = false;

                    while (!validnyRok) {
                        if (sc.hasNextInt()) {
                            rok = sc.nextInt();
                            sc.nextLine();  
                            if (rok >= 0 && rok <= 2024) {
                                validnyRok = true;
                            } else {
                                System.err.println("CHYBA! Neplatný rok. Zadajte rok v rozmedzí 0 - 2024.");
                            }
                        } else {
                            sc.next();  
                            System.err.println("CHYBA! Zadali ste neplatnú hodnotu. Zadajte číslo od 0 do 2024.");
                        }
                    }
                    int vyberDostupnost = 0;
                    while (true) {
                        System.out.println("Vyberte dostupnost knihy:");
                        System.out.println("1 - dostupná");
                        System.out.println("2 - vypožičaná");

                        if (sc.hasNextInt()) {
                        	vyberDostupnost = sc.nextInt();
                            sc.nextLine();  
                            if (vyberDostupnost == 1 || vyberDostupnost == 2) {
                                break;  
                            } else {
                                System.err.println("CHYBA! Zadali ste neplatnú hodnotu. Zadajte 1 alebo 2.");
                            }
                        } else {
                            System.err.println("CHYBA! Zadali ste neplatnú hodnotu. Zadajte 1 alebo 2.");
                            sc.next();  
                        }
                    }
                    String dostupnost = "";
                    switch (vyberDostupnost) {
                        case 1:
                            dostupnost = "dostupna";
                            break;
                        case 2:
                            dostupnost = "vypozicana";
                            break;
                        default:
                            break;
                    }

                    if (typ == 1) {
                        int vyberZaner = 0;
                        String zaner = "";
                        boolean validnyZaner = false;

                        while (!validnyZaner) {
                            System.out.println("Vyberte žáner knihy:");
                            System.out.println("1 - dobrodruzny");
                            System.out.println("2 - rytiersky");
                            System.out.println("3 - sci-fi");
                            System.out.println("4 - historicky");
                            System.out.println("5 - romanticky");

                            if (sc.hasNextInt()) {
                                vyberZaner = sc.nextInt();
                                sc.nextLine();  

                                switch (vyberZaner) {
                                    case 1:
                                        zaner = "dobrodruzny";
                                        validnyZaner = true;
                                        break;
                                    case 2:
                                        zaner = "rytiersky";
                                        validnyZaner = true;
                                        break;
                                    case 3:
                                        zaner = "sci-fi";
                                        validnyZaner = true;
                                        break;
                                    case 4:
                                        zaner = "historicky";
                                        validnyZaner = true;
                                        break;
                                    case 5:
                                        zaner = "romanticky";
                                        validnyZaner = true;
                                        break;
                                    default:
                                        System.err.println("CHYBA! Zadali ste neplatnú hodnotu. Zadajte číslo od 1 do 5.");
                                        break;
                                }
                            } 
                            else {
                                sc.next();  
                                System.err.println("CHYBA! Zadali ste neplatnú hodnotu. Zadajte číslo od 1 do 5.");
                            }
                        }

                        if (validnyZaner) {
                            aplikacia.pridajKnihu(new Romany(nazov, autor, zaner, rok, dostupnost, typKnihy));
                        }
                    }
                    else {
                        int rocnik = 0;
                        boolean validnyRocnik = false;

                        while (!validnyRocnik) {
                        	System.out.println("Zadajte ročník ucebnice: ");
                            if (sc.hasNextInt()) {
                            	
                                rocnik = sc.nextInt();
                                sc.nextLine();  
                                if (rocnik >= 1500 && rocnik <= 2024) {
                                    validnyRocnik = true;
                                } else {
                                    System.err.println("CHYBA! Neplatný ročník. Zadajte ročník v rozmedzí 1500 - 2024.");
                                }
                            } else {
                                sc.next();  
                                System.err.println("CHYBA! Zadali ste neplatnú hodnotu. Zadajte číslo od 1500 do 2024.");
                            }
                        }

                        if (validnyRocnik) {
                            aplikacia.pridajKnihu(new Ucebnice(nazov, autor, rocnik, rok, dostupnost, typKnihy));
                        }
                    }

                    break;
                case 2:
                    System.out.println("Zadajte názov knihy, ktorú chcete upraviť:");
                    String nazov1 = sc.nextLine();
                    System.out.println("Zadajte nových autorov (oddeľte čiarkami), alebo nechajte prázdne, ak nechcete meniť:");
                    String authorInput = sc.nextLine();
                    List<String> autor1 = !authorInput.isEmpty() ? Arrays.asList(authorInput.split(",\\s*")) : null;
                    System.out.println("Zadajte nový rok vydania, alebo nechajte prázdne, ak nechcete meniť:");
                    String rokInput = sc.nextLine();
                    Integer rok1 = !rokInput.isEmpty() ? Integer.parseInt(rokInput) : null;
                    System.out.println("Vyberte novú dostupnosť knihy:");
                    System.out.println("1 - dostupna");
                    System.out.println("2 - vypozicana");
                    int vyberDostupnost2 = sc.nextInt();
                    String dostupnost2 = "";
                    switch (vyberDostupnost2) {
                        case 1:
                            dostupnost2 = "dostupna";
                            break;
                        case 2:
                            dostupnost2 = "vypozicana";
                            break;
                        default:
                            break;
                    }
                    aplikacia.aktualizujKnihu(nazov1, autor1, rok1, dostupnost2);
                    break;
                case 3:
                    System.out.println("Zadajte nazov knihy, ktoru chcete odstranit: ");
                    String nazov2 = sc.nextLine();

                    aplikacia.odstranKnihu(nazov2);
                    break;
                case 4:
                    System.out.println("Zadajte nazov knihy, ktorej chcete zmenit dostupnost:");
                    String nazov3 = sc.nextLine();
                    System.out.println("Vyberte novú dostupnosť knihy:");
                    System.out.println("1 - dostupna");
                    System.out.println("2 - vypozicana");
                    int vyberDostupnost3 = sc.nextInt();
                    String dostupnost3 = "";
                    switch (vyberDostupnost3) {
                        case 1:
                            dostupnost3 = "dostupna";
                            break;
                        case 2:
                            dostupnost3 = "vypozicana";
                            break;
                        default:
                            break;
                    }
                    
                    aplikacia.zmenDostupnostKnihy(nazov3, dostupnost3);
                    break;
                case 5:
                    aplikacia.vypisKnihy();
                    break; 
                case 6:
                    System.out.println("Zadajte názov knihy, ktorú chcete vyhľadať:");
                    String hladanyNazov = sc.nextLine();
                    aplikacia.vyhladajKnihu(hladanyNazov);
                    break;
                case 7:
                    System.out.println("Zadajte meno autora:");
                    String autor2 = sc.nextLine();
                    aplikacia.vypisKnihyPodlaAutora(autor2);
                    break;
                case 8:
                System.out.println("Vyberte žáner knihy:");
                System.out.println("1 - dobrodruzny");
                System.out.println("2 - rytiersky");
                System.out.println("3 - sci-fi");
                System.out.println("4 - historicky");
                System.out.println("5 - romanticky");
                int vyberZaner = sc.nextInt();
                switch (vyberZaner) {
                    case 1:
                        String zaner = "dobrodruzny";
                        aplikacia.vypisKnihyPodlaZanru(zaner);
                        break;
                    case 2:
                        String zaner2 = "rytiersky";
                        aplikacia.vypisKnihyPodlaZanru(zaner2);
                        break;
                    case 3:
                        String zaner3 = "sci-fi";
                        aplikacia.vypisKnihyPodlaZanru(zaner3);
                        break;
                    case 4:
                        String zaner4 = "historicky";
                        aplikacia.vypisKnihyPodlaZanru(zaner4);
                        break;
                    case 5:
                        String zaner5 = "romanticky";
                        aplikacia.vypisKnihyPodlaZanru(zaner5);
                        break;
                    default:
                        break;
                }
                    ;
                    break;
               case 9:
                    aplikacia.vypisVypozicaneKnihy();
                    break;
                case 10:
                    System.out.println("Zadajte názov knihy, ktorú chcete uložiť:");
                    String hladanyNazovKnihy = sc.nextLine();
                    aplikacia.ulozKnihuDoSuboru(hladanyNazovKnihy);
                    break;
                case 11:
                    System.out.println("Zadajte názov súboru pre načítanie informácií o knihe:");
                    String cestaSuboru = sc.nextLine();
                    aplikacia.nacitajKnihuZoSuboru(cestaSuboru);
                    break;    
                case 12:
                    System.out.println("Ukladám zmeny do databázy...");
                    aplikacia.db.connect();
                    aplikacia.ulozZmeny();  
                    aplikacia.db.disconnect();
                    System.out.println("Dovidenia.");
                    program = false;
                    break;
                default:
                    System.out.println("Prosím vyberte možnosť z ponuky.");
            }
        }
        sc.close();
    }
}
