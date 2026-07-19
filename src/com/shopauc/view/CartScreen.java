package com.shopauc.view;

import com.shopauc.exception.InsufficientStockException;
import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class CartScreen extends JFrame {

    public CartScreen() {
        setTitle("AuctionHub — Cart");
        setSize(560, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeManager.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel title = new JLabel("My Cart");
        title.setFont(ThemeManager.FONT_HEADING);
        title.setForeground(ThemeManager.TEXT_PRIMARY);

        RoundedButton backBtn = new RoundedButton("Back", true);
        backBtn.setForeground(ThemeManager.TEXT_PRIMARY);
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.PAGE_BG);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        Marketplace mp = Marketplace.getInstance();
        List<CartItem> items = mp.getCart();

        if (items.isEmpty()) {
            JLabel empty = new JLabel("Your cart is empty.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (CartItem ci : items) {
                listPanel.add(makeCartCard(ci));
                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(ThemeManager.PAGE_BG);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(ThemeManager.SURFACE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ThemeManager.BORDER),
                new EmptyBorder(14, 20, 14, 20)));

        JPanel totalPanel = new JPanel();
        totalPanel.setOpaque(false);
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));

        JLabel totalLabel = new JLabel("Total");
        totalLabel.setFont(ThemeManager.FONT_SMALL);
        totalLabel.setForeground(ThemeManager.TEXT_MUTED);

        JLabel totalAmount = new JLabel("$" + String.format("%.2f", mp.getCartTotal()));
        totalAmount.setFont(ThemeManager.FONT_TITLE);
        totalAmount.setForeground(ThemeManager.TEXT_PRIMARY);

        totalPanel.add(totalLabel);
        totalPanel.add(totalAmount);

        RoundedButton checkoutBtn = new RoundedButton("Checkout", ThemeManager.ACCENT, Color.WHITE);
        checkoutBtn.addActionListener(e -> {
            try {
                Order order = mp.checkout();
                if (order != null) {
                    JOptionPane.showMessageDialog(this,
                            "Order #" + order.getId() + " confirmed!\nTotal: $"
                            + String.format("%.2f", order.getTotalAmount()),
                            "Order placed", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainScreen().setVisible(true);
                }
            } catch (InsufficientStockException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Stock error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(totalPanel, BorderLayout.WEST);
        footer.add(checkoutBtn, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
    }

    private JPanel makeCartCard(CartItem ci) {
        Product p = ci.getProduct();

        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ThemeManager.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // İkon
        JPanel iconBox = new JPanel(new BorderLayout());
        iconBox.setBackground(ThemeManager.getCategoryColor(p.getCategory()));
        iconBox.setPreferredSize(new Dimension(52, 52));
        iconBox.setBorder(BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true));

        JLabel iconLabel = new JLabel(ThemeManager.getCategoryIcon(p.getCategory()),
                SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconBox.add(iconLabel, BorderLayout.CENTER);

        // Bilgi
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        // Marka koyu
        JLabel brandLabel = new JLabel(p.getBrand());
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brandLabel.setForeground(ThemeManager.TEXT_PRIMARY);

        // Ürün adı soluk
        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(ThemeManager.TEXT_SECONDARY);

        JLabel qty = new JLabel("Qty: " + ci.getQuantity());
        qty.setFont(ThemeManager.FONT_SMALL);
        qty.setForeground(ThemeManager.TEXT_MUTED);

        info.add(brandLabel);
        info.add(Box.createVerticalStrut(2));
        info.add(nameLabel);
        info.add(Box.createVerticalStrut(3));
        info.add(qty);

        // Sağ
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel price = new JLabel("$" + String.format("%.2f", ci.getTotalPrice()));
        price.setFont(ThemeManager.FONT_SUB);
        price.setForeground(ThemeManager.TEXT_PRIMARY);
        price.setAlignmentX(RIGHT_ALIGNMENT);

        RoundedButton removeBtn = new RoundedButton("Remove", ThemeManager.DANGER, Color.WHITE);
        removeBtn.setFont(ThemeManager.FONT_SMALL);
        removeBtn.addActionListener(e -> {
            Marketplace.getInstance().removeFromCart(p.getId());
            dispose();
            new CartScreen().setVisible(true);
        });

        right.add(price);
        right.add(Box.createVerticalStrut(6));
        right.add(removeBtn);

        card.add(iconBox, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}