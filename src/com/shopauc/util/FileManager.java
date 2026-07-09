package com.shopauc.util;
import com.shopauc.model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class FileManager {
    private static final String USERS_FILE = "users.txt";
    private static final String PRODUCTS_FILE = "products.txt";
    private static final String ORDERS_FILE = "orders.txt";

    public static void saveUsers(List<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                String type = (u instanceof Buyer) ? "BUYER" : "SELLER";
                pw.println(type+","+u.getId()+","+u.getName()+","+u.getEmail()+","+u.getPassword());
            }
        } catch (IOException e) { System.err.println("Error saving users: "+e.getMessage()); }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File f = new File(USERS_FILE);
        if (!f.exists()) return users;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;
                int id = Integer.parseInt(p[1]);
                String name=p[2], email=p[3], password=p[4];
                if (p[0].equals("BUYER")) users.add(new Buyer(id,name,email,password));
                else users.add(new Seller(id,name,email,password));
            }
        } catch (IOException e) { System.err.println("Error loading users: "+e.getMessage()); }
        return users;
    }

    public static void saveProducts(List<Product> products) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product pr : products) {
                if (pr instanceof AuctionProduct) {
                    AuctionProduct ap = (AuctionProduct) pr;
                    pw.println("AUCTION,"+ap.getId()+","+ap.getName()+","+ap.getStartingPrice()+","+ap.getCurrentBid()+","+ap.getSellerId()+","+ap.getEndTime());
                } else {
                    pw.println("PRODUCT,"+pr.getId()+","+pr.getName()+","+pr.getPrice()+","+pr.getStock()+","+pr.getSellerId());
                }
            }
        } catch (IOException e) { System.err.println("Error saving products: "+e.getMessage()); }
    }

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        File f = new File(PRODUCTS_FILE);
        if (!f.exists()) return products;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p[0].equals("PRODUCT") && p.length >= 6) {
                    products.add(new Product(Integer.parseInt(p[1]),p[2],Double.parseDouble(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5])));
                } else if (p[0].equals("AUCTION") && p.length >= 7) {
                    AuctionProduct ap = new AuctionProduct(Integer.parseInt(p[1]),p[2],Double.parseDouble(p[3]),Integer.parseInt(p[5]),LocalDateTime.parse(p[6]));
                    ap.placeBid(Double.parseDouble(p[4]), -1);
                    products.add(ap);
                }
            }
        } catch (IOException e) { System.err.println("Error loading products: "+e.getMessage()); }
        return products;
    }

    public static void saveOrders(List<Order> orders) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            for (Order o : orders) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getId()).append(",").append(o.getBuyerId());
                for (CartItem ci : o.getItems()) sb.append(",").append(ci.getProduct().getId()).append(":").append(ci.getQuantity());
                pw.println(sb);
            }
        } catch (IOException e) { System.err.println("Error saving orders: "+e.getMessage()); }
    }
}