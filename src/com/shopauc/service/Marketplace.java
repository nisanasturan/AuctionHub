package com.shopauc.service;

import com.shopauc.exception.*;
import com.shopauc.model.*;
import com.shopauc.util.FileManager;
import com.shopauc.util.PasswordUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Marketplace {

    private static Marketplace instance;

    public static Marketplace getInstance() {
        if (instance == null) instance = new Marketplace();
        return instance;
    }

    private List<User> users;
    private List<Product> products;
    private List<Order> orders;
    private List<CartItem> cart;
    private User currentUser;

    private Marketplace() {
        users    = FileManager.loadUsers();
        products = FileManager.loadProducts();
        orders   = new ArrayList<>();
        cart     = new ArrayList<>();
    }

    public User login(String email, String password) {
        String hashed = PasswordUtil.hash(password);
        for (User u : users)
            if (u.getEmail().equals(email) && u.getPassword().equals(hashed)) {
                currentUser = u;
                return u;
            }
        return null;
    }

    public boolean register(String name, String email, String password, String role) {
        for (User u : users)
            if (u.getEmail().equals(email)) return false;
        int newId = users.isEmpty() ? 1 :
                Collections.max(users.stream().map(User::getId).toList()) + 1;
        String hashed = PasswordUtil.hash(password);
        User newUser = role.equals("BUYER")
                ? new Buyer(newId, name, email, hashed)
                : new Seller(newId, name, email, hashed);
        users.add(newUser);
        FileManager.saveUsers(users);
        return true;
    }

    public void logout() { currentUser = null; }
    public User getCurrentUser() { return currentUser; }
    public List<Product> getProducts() { return products; }

    public List<Product> getProductsBySeller(int sellerId) {
        return products.stream().filter(p -> p.getSellerId() == sellerId).toList();
    }

    public void addProduct(String name, double price, int stock, String category) {
        int newId = products.isEmpty() ? 1 :
                Collections.max(products.stream().map(Product::getId).toList()) + 1;
        products.add(new Product(newId, name, price, stock, currentUser.getId(), category));
        FileManager.saveProducts(products);
    }

    public void addAuctionProduct(String name, double startingPrice, int hoursUntilEnd, String category) {
        int newId = products.isEmpty() ? 1 :
                Collections.max(products.stream().map(Product::getId).toList()) + 1;
        LocalDateTime endTime = LocalDateTime.now().plusHours(hoursUntilEnd);
        products.add(new AuctionProduct(newId, name, startingPrice, currentUser.getId(), endTime, category));
        FileManager.saveProducts(products);
    }

    public void addToCart(Product product, int quantity) throws InsufficientStockException {
        if (product.getStock() < quantity)
            throw new InsufficientStockException(product.getName());
        for (CartItem ci : cart)
            if (ci.getProduct().getId() == product.getId()) {
                ci.setQuantity(ci.getQuantity() + quantity);
                return;
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
        for (CartItem ci : cart)
            ci.getProduct().setStock(ci.getProduct().getStock() - ci.getQuantity());
        int newId = orders.isEmpty() ? 1 :
                Collections.max(orders.stream().map(Order::getId).toList()) + 1;
        Order order = new Order(newId, currentUser.getId(), new ArrayList<>(cart));
        orders.add(order);
        FileManager.saveProducts(products);
        clearCart();
        return order;
    }

    public void placeBid(AuctionProduct ap, double amount)
            throws InvalidBidException, AuctionExpiredException {
        if (ap.isExpired())
            throw new AuctionExpiredException(ap.getName());
        if (amount < ap.getStartingPrice())
            throw new InvalidBidException(ap.getStartingPrice(), amount);
        if (amount <= ap.getCurrentBid())
            throw new InvalidBidException(ap.getCurrentBid(), amount);
        ap.placeBid(amount, currentUser.getId(), currentUser.getName());
        FileManager.saveProducts(products);
    }

    public List<Order> getOrdersByBuyer(int buyerId) {
        return orders.stream().filter(o -> o.getBuyerId() == buyerId).toList();
    }
}