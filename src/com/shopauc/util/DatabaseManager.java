package com.shopauc.util;

import com.shopauc.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:" +
            System.getProperty("user.home") + "/AuctionHubData/auctionhub.db";

    private static DatabaseManager instance;

    public static DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    private DatabaseManager() {
        new java.io.File(System.getProperty("user.home") + "/AuctionHubData/").mkdirs();
        initTables();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void initTables() {
        String users = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                )""";

        String products = """
                CREATE TABLE IF NOT EXISTS products (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    brand TEXT NOT NULL,
                    price REAL NOT NULL,
                    stock INTEGER NOT NULL,
                    seller_id INTEGER NOT NULL,
                    category TEXT NOT NULL
                )""";

        String auctions = """
                CREATE TABLE IF NOT EXISTS auctions (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    brand TEXT NOT NULL,
                    starting_price REAL NOT NULL,
                    current_bid REAL NOT NULL,
                    highest_bidder_id INTEGER,
                    highest_bidder_name TEXT,
                    seller_id INTEGER NOT NULL,
                    category TEXT NOT NULL,
                    start_time TEXT NOT NULL,
                    end_time TEXT NOT NULL,
                    bid_count INTEGER DEFAULT 0
                )""";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(users);
            stmt.execute(products);
            stmt.execute(auctions);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public boolean saveUser(User user, String role) {
        String sql = "INSERT OR IGNORE INTO users (id, name, email, password, role) VALUES (?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, role);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                if (role.equals("BUYER")) users.add(new Buyer(id, name, email, password));
                else users.add(new Seller(id, name, email, password));
            }
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    public int getNextUserId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM users";
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) { return 1; }
    }

    public void saveProduct(Product p) {
        String sql = "INSERT OR REPLACE INTO products (id, name, brand, price, stock, seller_id, category) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getSellerId());
            ps.setString(7, p.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving product: " + e.getMessage());
        }
    }

    public void updateProductStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
        }
    }

    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getInt("seller_id"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
        return products;
    }

    public int getNextProductId() {
        String sql = "SELECT MAX(max_id) FROM (SELECT COALESCE(MAX(id),0)+1 as max_id FROM products UNION ALL SELECT COALESCE(MAX(id),0)+1 FROM auctions)";
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) { return 1; }
    }

    public void saveAuction(AuctionProduct ap) {
        String sql = """
                INSERT OR REPLACE INTO auctions
                (id, name, brand, starting_price, current_bid, highest_bidder_id,
                 highest_bidder_name, seller_id, category, start_time, end_time, bid_count)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?)""";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ap.getId());
            ps.setString(2, ap.getName());
            ps.setString(3, ap.getBrand());
            ps.setDouble(4, ap.getStartingPrice());
            ps.setDouble(5, ap.getCurrentBid());
            ps.setInt(6, ap.getHighestBidderId());
            ps.setString(7, ap.getHighestBidderName());
            ps.setInt(8, ap.getSellerId());
            ps.setString(9, ap.getCategory());
            ps.setString(10, ap.getStartTime().toString());
            ps.setString(11, ap.getEndTime().toString());
            ps.setInt(12, ap.getBidCount());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving auction: " + e.getMessage());
        }
    }

    public List<AuctionProduct> loadAuctions() {
        List<AuctionProduct> auctions = new ArrayList<>();
        String sql = "SELECT * FROM auctions";
        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AuctionProduct ap = new AuctionProduct(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("starting_price"),
                        rs.getInt("seller_id"),
                        LocalDateTime.parse(rs.getString("end_time")),
                        rs.getString("category"));
                ap.placeBid(rs.getDouble("current_bid"),
                        rs.getInt("highest_bidder_id"),
                        rs.getString("highest_bidder_name") != null ?
                        rs.getString("highest_bidder_name") : "No bids yet");
                auctions.add(ap);
            }
        } catch (SQLException e) {
            System.err.println("Error loading auctions: " + e.getMessage());
        }
        return auctions;
    }
}