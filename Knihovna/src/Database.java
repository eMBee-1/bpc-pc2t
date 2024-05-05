import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {

    private Connection conn; 

    public Connection connect() { 
        conn = null; 
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:DatabazaKnih.db");
            return conn;                      
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver sa nenasiel: " + e.getMessage());
        } catch (SQLException e) { 
            System.out.println("Pripojenie zlyhalo: " + e.getMessage());
        }
        return conn;
    }

    public boolean isConnected() {
        return conn != null;
    }

    public void disconnect() { 
        if (conn != null) {
            try {
                conn.close();  
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void createTables() {
        if (connect() == null) {
            System.out.println("Pripojenie k databaze zlyhalo.");
            return;
        }

        String sqlBooks = "CREATE TABLE IF NOT EXISTS books (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nazov TEXT NOT NULL," +
                            "autor TEXT NOT NULL," +
                            "zaner TEXT NOT NULL," +
                            "rocnik INTEGER," +
                            "rok INTEGER NOT NULL," +
                            "stav TEXT NOT NULL," +
                            "typ TEXT NOT NULL );";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlBooks);
        } catch (SQLException e) {
            System.out.println("Chyba pri vytvarani tabulky: " + e.getMessage());
        } finally {
            disconnect();
        }
    }
    
    public List<Kniha> nacitajDatabazu() {
        List<Kniha> books = new ArrayList<>();
        String sql = "SELECT nazov, autor, zaner, rocnik, rok, stav, typ FROM books";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nazov = rs.getString("nazov");
                List<String> autori = Arrays.asList(rs.getString("autor").split(", "));
                int rok = rs.getInt("rok");
                String dostupnost = rs.getString("stav");
                String typKnihy = rs.getString("typ");
                
                if (typKnihy.equalsIgnoreCase("roman")) {
                    String zaner = rs.getString("zaner");
                    books.add(new Romany(nazov, autori, zaner, rok, dostupnost, typKnihy));
                } else if (typKnihy.equalsIgnoreCase("ucebnica")) {
                    int rocnik = rs.getInt("rocnik");
                    books.add(new Ucebnice(nazov, autori, rocnik, rok, dostupnost, typKnihy));
                } 
            }
        } catch (SQLException e) {
            System.out.println("Chyba pri získavaní kníh: " + e.getMessage());
        }
        return books;
    }

    public void ulozDatabazu(List<Kniha> books) {
        if (connect() == null) {
            System.out.println("Nepodarilo sa pripojiť k databáze.");
            return;
        }
        try {
            // Clear the existing data
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM books");
            }
            // Insert new data
            String sql = "INSERT INTO books (nazov, autor, typ, zaner, rocnik, rok, stav) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Kniha kniha : books) {
                    pstmt.setString(1, kniha.getNazov());
                    pstmt.setString(2, String.join(", ", kniha.getAutori()));
                    pstmt.setString(3, kniha.getTyp());
                    pstmt.setString(4, kniha instanceof Romany ? ((Romany) kniha).getZaner() : "");
                    pstmt.setInt(5, kniha instanceof Ucebnice ? ((Ucebnice) kniha).getRocnik() : 0);
                    pstmt.setInt(6, kniha.getRok());
                    pstmt.setString(7, kniha.getDostupnost());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Chyba pri ukladani knih: " + e.getMessage());
        } finally {
            disconnect();
        }
    }
}
