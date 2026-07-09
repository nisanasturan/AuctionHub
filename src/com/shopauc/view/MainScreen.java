package com.shopauc.view;

import com.shopauc.model.Buyer;
import com.shopauc.model.Seller;
import com.shopauc.model.User;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainScreen extends JFrame {

    public MainScreen() {
        User user = Marketplace.getInstance().getCurrentUser();
        setTitle("AuctionHub — Home");
        setSize(480, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.BG_LIGHT);
        setContentPane(root);

        // ── Gradient Header ──────────────────────────────────
        GradientPanel header = new GradientPanel(ThemeManager.PURPLE, ThemeManager.PINK);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(32, 28, 28, 28));
        header.setPreferredSize(new Dimension(480, 160));

        JLabel greet = new JLabel("Good day,");
        greet.setFont(ThemeManager.FONT_SMALL);
        greet.setForeground(new Color(255, 255, 255, 180));
        greet.setAlignmentX(LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(user.getName());
        nameLabel.setFont(ThemeManager.FONT_TITLE);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        String roleText = (user instanceof Buyer) ? "🛍  Buyer" : "🏪  Seller";
        JLabel roleLabel = new JLabel(roleText);
        roleLabel.setFont(ThemeManager.FONT_SMALL);
        roleLabel.setForeground(new Color(255, 255, 255, 200));
        roleLabel.setAlignmentX(LEFT_ALIGNMENT);

        header.add(greet);
        header.add(Box.createVerticalStrut(4));
        header.add(nameLabel);
        header.add(Box.createVerticalStrut(8));
        header.add(roleLabel);

        // ── Menu Cards ───────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(0, 2, 14, 14));
        grid.setBackground(ThemeManager.BG_LIGHT);
        grid.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (user instanceof Buyer) {
            grid.add(makeCard("🛍", "Browse", "All products", ThemeManager.PURPLE_SOFT, ThemeManager.PURPLE, () -> {
                dispose(); new ProductListScreen().setVisible(true);
            }));
            grid.add(makeCard("🔨", "Auctions", "Live bids", ThemeManager.PINK_SOFT, ThemeManager.PINK, () -> {
                dispose(); new AuctionScreen().setVisible(true);
            }));
            grid.add(makeCard("🛒", "My Cart", "View cart", ThemeManager.AMBER_SOFT, ThemeManager.ORANGE, () -> {
                dispose(); new CartScreen().setVisible(true);
            }));
            grid.add(makeCard("📦", "Orders", "View history", ThemeManager.TEAL_SOFT, ThemeManager.TEAL, () -> {
                dispose(); new OrderScreen().setVisible(true);
            }));
        } else {
            grid.add(makeCard("➕", "Add Product", "List new item", ThemeManager.PURPLE_SOFT, ThemeManager.PURPLE, () -> {
                dispose(); new AddProductScreen().setVisible(true);
            }));
            grid.add(makeCard("📋", "My Products", "Manage listings", ThemeManager.PINK_SOFT, ThemeManager.PINK, () -> {
                dispose(); new ProductListScreen().setVisible(true);
            }));
            grid.add(makeCard("🔨", "Add Auction", "Start bidding", ThemeManager.AMBER_SOFT, ThemeManager.ORANGE, () -> {
                dispose(); new AddAuctionScreen().setVisible(true);
            }));
            grid.add(makeCard("📊", "Dashboard", "Coming soon", ThemeManager.TEAL_SOFT, ThemeManager.TEAL, () -> {
                JOptionPane.showMessageDialog(this, "Coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }));
        }

        // ── Logout Button ────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(ThemeManager.BG_LIGHT);
        bottom.setBorder(new EmptyBorder(0, 20, 16, 20));

        RoundedButton logoutBtn = new RoundedButton("Logout",
                new Color(200, 200, 210), new Color(180, 180, 190));
        logoutBtn.setForeground(ThemeManager.TEXT_DARK);
        logoutBtn.addActionListener(e -> {
            Marketplace.getInstance().logout();
            dispose();
            new LoginScreen().setVisible(true);
        });
        bottom.add(logoutBtn);

        root.add(header, BorderLayout.NORTH);
        root.add(grid, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel makeCard(String icon, String title, String sub,
                             Color bgColor, Color accentColor, Runnable action) {
        JPanel card = new JPanel();
        card.setBackground(ThemeManager.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 18, 20, 18));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel iconBox = new JPanel();
        iconBox.setBackground(bgColor);
        iconBox.setPreferredSize(new Dimension(46, 46));
        iconBox.setMaximumSize(new Dimension(46, 46));
        iconBox.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        iconBox.setAlignmentX(LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconBox.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeManager.FONT_SUB);
        titleLabel.setForeground(ThemeManager.TEXT_DARK);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(ThemeManager.FONT_SMALL);
        subLabel.setForeground(ThemeManager.TEXT_GRAY);
        subLabel.setAlignmentX(LEFT_ALIGNMENT);

        card.add(iconBox);
        card.add(Box.createVerticalStrut(12));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(subLabel);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { action.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(bgColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(ThemeManager.WHITE);
            }
        });

        return card;
    }
}