import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AplikaciaKniznica extends Kniznica {

	public static void main(String[] args) {
		
		AplikaciaKniznica aplikacia = new AplikaciaKniznica();
		Scanner sc = new Scanner(System.in);

		boolean program = true;

		System.out.println("--------------------------------------");
		System.out.println("Vitajte v aplikacii na spravu kniznice");
		System.out.println("--------------------------------------");

		while (program) {
			System.out.println("1 - pridajte knihu");
			System.out.println("2 - upravte knihu");
			System.out.println("3 - odstrante knihu");
			System.out.println("4 - zmente dostupnost knihy");
			System.out.println("5 - vypiste knihy");
			System.out.println("6 - vyhladajte knihu");
			System.out.println("7 - vypiste knihy podla autora");
			System.out.println("8 - vypiste knihy podla zanru");
			System.out.println("9 - vypiste vypozicane knihy");
			System.out.println("10 - ulozit do suboru");
			System.out.println("11 - nacitat zo suboru");
			System.out.println("12 - ulozit do databazy");
			System.out.println("13 - nacitat z databazy");
			System.out.println("14 - koniec");
			System.out.println("Zadaj moznost: ");
			int moznost = sc.nextInt();
			String nazov = "";
			int rok = 0;
			String zaner = "";
			int rocnik = 0;
			boolean dostupnost = false;

			switch (moznost) {
			case 1:
				System.out.println("Zadajte pocet autorov knihy: ");
				int pocetAutorov = sc.nextInt();
				sc.nextLine();
				List<String> autor = new ArrayList<>(pocetAutorov);
				for (int i = 0; i < pocetAutorov; i++) {
					System.out.println("Zadajte autora: ");
					autor.add(sc.nextLine());
				}
				System.out.println("Zadajte typ knihy: ");
				System.out.println("1 - Romany");
				System.out.println("2 - Ucebnice");
				int typ = sc.nextInt();
				sc.nextLine();
				System.out.println("Zadajte nazov knihy: ");
				nazov = sc.nextLine();
				System.out.println("Zadajte rok vydania: ");
				rok = sc.nextInt();
				System.out.println("Vyberte dostupnost knihy: ");
				System.out.println("1 - dostupna");
				System.out.println("2 - vypozicana");
				int dostupnosti = sc.nextInt();
				switch (dostupnosti) {
					case 1:
						dostupnost = true;
						break;
					case 2:
						dostupnost = false;
						break;
				
					default:
						break;
				}

				if (typ == 1) {
					System.out.println("Zadajte zaner knihy: ");
					zaner = sc.nextLine();
					sc.nextLine();
					aplikacia.pridajKnihu(new Romany(nazov, autor, rok, dostupnost, zaner));
				}
				else {
					System.out.println("Zadajte rocnik knihy: ");
					rocnik = sc.nextInt();
					aplikacia.pridajKnihu(new Ucebnice(nazov, autor, rok, dostupnost, rocnik));
				}
				break;
			case 2:
				System.out.println("Zadajte nazov knihy: ");
				

				break; /*
			case 3:
				aplikacia.odstranKnihu();
				break;
			case 4:
				aplikacia.zmenDostupnostKnihy();
				break; */
			case 5:
				aplikacia.vypisKnihy();
				break;/*
			case 6:
				aplikacia.vzhladajKnihu();
				break;
			case 7:
				System.out.println("Zadaj autora: ");
				String autorVyh = sc.nextLine();
				aplikacia.vypisKnihyPodlaAutora(autorVyh);
				break;
			case 8:
				System.out.println("Zadaj zaner: ");
				String zanerVyh = sc.nextLine();
				aplikacia.vypisKnihyPodlaZanru(zanerVyh);
				break;
			case 9:
				aplikacia.vypisVypozicaneKnihy();
				break;
			case 10:
				aplikacia.ulozDoSuboru();
				break;
			case 11:
				aplikacia.nacitajZoSuboru();
				break;
			case 12:
				aplikacia.ulozDoDatabazy();
				break;
			case 13:
				aplikacia.nacitajZDatabazy();
				break;*/
			case 14:
				System.out.println("Dovidenia.");
				program = false;
				break;
			default:
				System.out.println("Prosim vyberte moznost z ponuky.");
			}
		}

		

	}

}
