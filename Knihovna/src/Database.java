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
            System.out.println("JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) { 
            System.out.println("Connection failed: " + e.getMessage());
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
            System.out.println("Unable to connect to database.");
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
            System.out.println("Error creating table: " + e.getMessage());
        } finally {
            disconnect();
        }
    }
    public void addKniha(Kniha kniha) {
        String sql = "INSERT INTO books (nazov, autor, typ, zaner, rocnik, rok, stav) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kniha.getNazov());
            pstmt.setString(2, String.join(", ", kniha.getAutori()));
            pstmt.setString(3, kniha.getTyp());
            pstmt.setInt(6, kniha.getRok());
            pstmt.setString(7, kniha.getDostupnost());

            if (kniha instanceof Romany) {
                pstmt.setString(4, ((Romany) kniha).getZaner());
                pstmt.setInt(5, 0);
            } else if (kniha instanceof Ucebnice) {
                pstmt.setInt(5, ((Ucebnice) kniha).getRocnik());
                pstmt.setString(4, "");
            }
            

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Chyba pri vkladaní knihy: " + e.getMessage());
        }
    }
    
    public List<Kniha> getAllBooks() {
        List<Kniha> books = new ArrayList<>();
        String sql = "SELECT nazov, autor, zaner, rok, rocnik, stav, typ FROM books";
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

    
    public void removeBook(String nazov) {
        String sql = "DELETE FROM books WHERE nazov = ?";

        if (connect() == null) {
            System.out.println("Unable to connect to database.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nazov);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book removed successfully.");
            } else {
                System.out.println("No book found with nazov: " + nazov);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        } finally {
            disconnect();
        }
    }
    

    public void updateBook(String nazov, List<String> autor, Integer rok, String dostupnost) {
        String sql = "UPDATE books SET ";
        List<String> updates = new ArrayList<>();

        if (autor != null && !autor.isEmpty()) {
            sql += "autor = ?, ";
            updates.add("autor");
        }
        if (rok != 0) {
            sql += "rocnik = ?, ";
            updates.add("rocnik");
        }
        
        if (dostupnost != null) {
            sql += "stav = ?, ";
            updates.add("stav");
        }

        sql = sql.replaceAll(", $", " ");
        sql += "WHERE nazov = ?;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (String update : updates) {
                switch (update) {
                    case "autor":
                        pstmt.setString(index++, String.join(", ", autor));
                        break;
                    case "rok":
                        pstmt.setInt(index++, rok);
                        break;
                    case "stav":
                        pstmt.setString(index++, dostupnost);
                        break;
                }
            }
            pstmt.setString(index, nazov);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("No book found with nazov: " + nazov);
            }
        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        } finally {
            disconnect();
        }
    }


    
    public List<Kniha> getBooksByAuthor(String author) {
        List<Kniha> books = new ArrayList<>();
        String sql = "SELECT nazov, autor, zaner, rocnik, stav, typ FROM books WHERE autor LIKE ? ORDER BY rocnik ASC";
    
        if (connect() == null) {
            System.out.println("Unable to connect to database.");
            return books;
        }
    
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + author + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nazov = rs.getString("nazov");
                    String typKnihy = rs.getString("typ");
                    List<String> autor = List.of(rs.getString("autor").split(", "));
                    int rok = rs.getInt("rok");
                    String dostupnost = rs.getString("stav");
                    
                    if (typKnihy.equalsIgnoreCase("roman")) {
                        String zaner = rs.getString("zaner");
                        books.add(new Romany(nazov, autor, zaner, rok, dostupnost, typKnihy));
                    } else if (typKnihy.equalsIgnoreCase("ucebnica")) {
                        int rocnik = rs.getInt("rocnik");
                        books.add(new Ucebnice(nazov, autor, rocnik, rok, dostupnost, typKnihy));
                    } 
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving books by author: " + e.getMessage());
        } finally {
            disconnect();
        }
        return books;
    }
    
    
    public List<Kniha> getBooksByGenre(String zaner) {
        List<Kniha> books = new ArrayList<>();
        String sql = "SELECT nazov, autor, rok, zaner, stav, typ FROM books WHERE zaner = ? ORDER BY nazov ASC";

        if (connect() == null) {
            System.out.println("Unable to connect to database.");
            return books;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, zaner);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nazov = rs.getString("nazov");
                    
                    List<String> autor = List.of(rs.getString("autor").split(", "));
                    int rok = rs.getInt("rok");
                    String dostupnost = rs.getString("stav");
                    String zaner1 = rs.getString("zaner");
                    String typKnihy = rs.getString("typ");
                    books.add(new Romany(nazov, autor, zaner1, rok, dostupnost, typKnihy));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving books by zaner: " + e.getMessage());
        } finally {
            disconnect();
        }
        return books;
    }
    
    public Kniha findBookByName(String nazov) {
        String sql = "SELECT * FROM books WHERE nazov = ?";
        Kniha book = null;

        if (connect() == null) {
            System.out.println("Unable to connect to database.");
            return null;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nazov);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String typKnihy = rs.getString("typ");
                    List<String> autor = List.of(rs.getString("autor").split(", "));
                    int rok = rs.getInt("rok");
                    String dostupnost = rs.getString("stav");
                    
                    if (typKnihy.equalsIgnoreCase("roman")) {
                        String zaner = rs.getString("zaner");
                        book = new Romany(nazov, autor, zaner, rok, dostupnost, typKnihy);
                    } else if (typKnihy.equalsIgnoreCase("ucebnica")) {
                        int rocnik = rs.getInt("rocnik");
                        book = new Ucebnice(nazov, autor, rocnik, rok, dostupnost, typKnihy);
                    } 
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving book: " + e.getMessage());
        } finally {
            disconnect();
        }
        return book;
    }

        
    public List<Kniha> getBorrowedBooks() {
        List<Kniha> borrowedBooks = new ArrayList<>();
        String sql = "SELECT nazov, typ, stav FROM books WHERE stav = 'vypozicana'";

        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String nazov = rs.getString("nazov");
                String typKnihy = rs.getString("typ");
                String dostupnost = rs.getString("stav");

                borrowedBooks.add(new Kniha(nazov, null, 0, dostupnost, typKnihy));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving borrowed books: " + e.getMessage());
        }
        return borrowedBooks;
    }

    public void updateBookAvailability(String nazov3, String dostupnost3) {
        String sql = "UPDATE books SET stav = ? WHERE nazov = ?";

        if (connect() == null) {
            System.out.println("Unable to connect to database.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dostupnost3);
            pstmt.setString(2, nazov3);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Dostupnost knihy " + nazov3 + " bola zmenena na " + dostupnost3 + ".");
            } else {
                System.out.println("Kniha s nazvom " + nazov3 + " nebola najdena.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating book availability: " + e.getMessage());
        } finally {
            disconnect();
        }
    }
}




