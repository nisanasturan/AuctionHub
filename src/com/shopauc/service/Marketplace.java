package com.shopauc.service;

import com.shopauc.exception.*;
import com.shopauc.model.*;
import com.shopauc.util.DatabaseManager;
import com.shopauc.util.PasswordUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Marketplace {

    private static Marketplace instance;

    public static Marketplace getInstance() {
        if (instance == null) instance = new Marketplace();
        return instance;
    }

    private List<User> users;
    private List<Product> products;
    private List<AuctionProduct> auctions;
    private List<Order> orders;
    private List<CartItem> cart;
    private User currentUser;

    private Marketplace() {
        DatabaseManager db = DatabaseManager.getInstance();
        users    = db.loadUsers();
        products = db.loadProducts();
        auctions = db.loadAuctions();
        orders   = new ArrayList<>();
        cart     = new ArrayList<>();
    }

    public User login(String email, String password) {
        String hashed = PasswordUtil.hash(password);
        for (User u : users)
            if (u.getEmail().equals(email) && u.getPassword().equals(hashed)) {
                currentUser = u; return u;
            }
        return null;
    }

    public boolean register(String name, String email, String password, String role) {
        for (User u : users) if (u.getEmail().equals(email)) return false;
        int newId = DatabaseManager.getInstance().getNextUserId();
        String hashed = PasswordUtil.hash(password);
        User newUser = role.equals("BUYER")
                ? new Buyer(newId, name, email, hashed)
                : new Seller(newId, name, email, hashed);
        users.add(newUser);
        DatabaseManager.getInstance().saveUser(newUser, role);
        return true;
    }

    public void logout() { currentUser = null; }
    public User getCurrentUser() { return currentUser; }
    public List<Product> getProducts() { return products; }
    public List<AuctionProduct> getAuctions() { return auctions; }

    public List<Product> getProductsBySeller(int sellerId) {
        return products.stream().filter(p -> p.getSellerId() == sellerId).collect(Collectors.toList());
    }

    public List<AuctionProduct> getAuctionsBySeller(int sellerId) {
        return auctions.stream().filter(a -> a.getSellerId() == sellerId).collect(Collectors.toList());
    }

    public void addProduct(String name, String brand, double price, int stock, String category) {
        int newId = DatabaseManager.getInstance().getNextProductId();
        Product p = new Product(newId, name, brand, price, stock, currentUser.getId(), category);
        products.add(p);
        DatabaseManager.getInstance().saveProduct(p);
    }

    public void addAuctionProduct(String name, String brand, double startingPrice,
                                   int hoursUntilEnd, String category) {
        int newId = DatabaseManager.getInstance().getNextProductId();
        LocalDateTime endTime = LocalDateTime.now().plusHours(hoursUntilEnd);
        AuctionProduct ap = new AuctionProduct(newId, name, brand, startingPrice,
                currentUser.getId(), endTime, category);
        auctions.add(ap);
        DatabaseManager.getInstance().saveAuction(ap);
    }

    public void addToCart(Product product, int quantity) throws InsufficientStockException {
        if (product.getStock() < quantity)
            throw new InsufficientStockException(product.getName());
        for (CartItem ci : cart)
            if (ci.getProduct().getId() == product.getId()) {
                ci.setQuantity(ci.getQuantity() + quantity); return;
            }
        cart.add(new CartItem(product, quantity));
    }

    public void removeFromCart(int productId) {
        cart.removeIf(ci -> ci.getProduct().getId() == productId);
    }

    public List<CartItem> getCart() { return cart; }

    public double getCartTotal() {
        return cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public void clearCart() { cart.clear(); }

    public Order checkout() throws InsufficientStockException {
        if (cart.isEmpty()) return null;
        for (CartItem ci : cart)
            if (ci.getProduct().getStock() < ci.getQuantity())
                throw new InsufficientStockException(ci.getProduct().getName());
        for (CartItem ci : cart) {
            int newStock = ci.getProduct().getStock() - ci.getQuantity();
            ci.getProduct().setStock(newStock);
            DatabaseManager.getInstance().updateProductStock(ci.getProduct().getId(), newStock);
        }
        int newId = orders.isEmpty() ? 1 : orders.stream().mapToInt(Order::getId).max().getAsInt() + 1;
        Order order = new Order(newId, currentUser.getId(), new ArrayList<>(cart));
        orders.add(order);
        clearCart();
        return order;
    }

    public void placeBid(AuctionProduct ap, double amount)
            throws InvalidBidException, AuctionExpiredException {
        if (ap.isExpired()) throw new AuctionExpiredException(ap.getName());
        if (amount < ap.getStartingPrice()) throw new InvalidBidException(ap.getStartingPrice(), amount);
        if (amount <= ap.getCurrentBid()) throw new InvalidBidException(ap.getCurrentBid(), amount);
        ap.placeBid(amount, currentUser.getId(), currentUser.getName());
        DatabaseManager.getInstance().saveAuction(ap);
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        return orders.stream().filter(o -> o.getBuyerId() == buyerId).collect(Collectors.toList());
    }
}