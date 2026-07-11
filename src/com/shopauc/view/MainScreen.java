package com.shopauc.view;

import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainScreen extends JFrame {

    public MainScreen() {
        User user = Marketplace.getInstance().getCurrentUser();
        setTitle("AuctionHub");
        setSize(520, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        // ── Top bar ─────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeManager.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(12, 20, 12, 20)));

        JLabel logoLabel = new JLabel("AuctionHub");
        logoLabel.setFont(ThemeManager.FONT_HEADING);
        logoLabel.setForeground(ThemeManager.ACCENT);

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        userInfo.setOpaque(false);

        JLabel userLabel = new JLabel(user.getName());
        userLabel.setFont(ThemeManager.FONT_SMALL);
        userLabel.setForeground(ThemeManager.TEXT_SECONDARY);

        String roleText = (user instanceof Buyer) ? "Buyer" : "Seller";
        JLabel roleBadge = new JLabel(roleText);
        roleBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        roleBadge.setForeground(ThemeManager.ACCENT);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(ThemeManager.ACCENT_LIGHT);
        roleBadge.setBorder(new EmptyBorder(2, 8, 2, 8));

        RoundedButton logoutBtn = new RoundedButton("Sign out", true);
        logoutBtn.setFont(ThemeManager.FONT_SMALL);
        logoutBtn.setForeground(ThemeManager.TEXT_SECONDARY);
        logoutBtn.addActionListener(e -> {
            Marketplace.getInstance().logout();
            dispose();
            new LoginScreen().setVisible(true);
        });

        userInfo.add(userLabel);
        userInfo.add(roleBadge);
        userInfo.add(logoutBtn);

        topBar.add(logoLabel, BorderLayout.WEST);
        topBar.add(userInfo, BorderLayout.EAST);

        // ── Grid ─────────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setBackground(ThemeManager.PAGE_BG);
        grid.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (user instanceof Buyer) {
            grid.add(makeCard("Browse Products", "Search and filter all items",
                    new Color(219, 234, 254), ThemeManager.ACCENT,
                    () -> { dispose(); new ProductListScreen().setVisible(true); }));
            grid.add(makeCard("Live Auctions", "Bid on active auctions",
                    new Color(254, 226, 226), ThemeManager.DANGER,
                    () -> { dispose(); new AuctionScreen().setVisible(true); }));
            grid.add(makeCard("My Cart", "View and checkout",
                    new Color(220, 252, 231), ThemeManager.SUCCESS,
                    () -> { dispose(); new CartScreen().setVisible(true); }));
            grid.add(makeCard("My Orders", "Order history",
                    new Color(254, 243, 199), ThemeManager.WARNING,
                    () -> { dispose(); new OrderScreen().setVisible(true); }));
        } else {
            grid.add(makeCard("Add Product", "List a new item for sale",
                    new Color(219, 234, 254), ThemeManager.ACCENT,
                    () -> { dispose(); new AddProductScreen().setVisible(true); }));
            grid.add(makeCard("My Listings", "Manage your products",
                    new Color(220, 252, 231), ThemeManager.SUCCESS,
                    () -> { dispose(); new ProductListScreen().setVisible(true); }));
            grid.add(makeCard("Start Auction", "Create a new auction",
                    new Color(254, 226, 226), ThemeManager.DANGER,
                    () -> { dispose(); new AddAuctionScreen().setVisible(true); }));
            grid.add(makeCard("My Auctions", "Manage auction listings",
                    new Color(254, 243, 199), ThemeManager.WARNING,
                    () -> { dispose(); new AuctionScreen().setVisible(true); }));
        }

        root.add(topBar, BorderLayout.NORTH);
        root.add(grid, BorderLayout.CENTER);
    }

    private JPanel makeCard(String title, String sub, Color bg, Color accent, Runnable action) {
        JPanel card = new JPanel();
        card.setBackground(ThemeManager.SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel dot = new JPanel();
        dot.setBackground(bg);
        dot.setPreferredSize(new Dimension(40, 40));
        dot.setMaximumSize(new Dimension(40, 40));
        dot.setBorder(BorderFactory.createLineBorder(bg.darker(), 1, true));
        dot.setAlignmentX(LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeManager.FONT_SUB);
        titleLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(ThemeManager.FONT_SMALL);
        subLabel.setForeground(ThemeManager.TEXT_MUTED);
        subLabel.setAlignmentX(LEFT_ALIGNMENT);

        card.add(dot);
        card.add(Box.createVerticalStrut(14));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subLabel);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.run(); }
            public void mouseEntered(MouseEvent e) {
                card.setBackground(ThemeManager.SURFACE_1);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ThemeManager.BORDER_STRONG, 1, true),
                        new EmptyBorder(20, 20, 20, 20)));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(ThemeManager.SURFACE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                        new EmptyBorder(20, 20, 20, 20)));
            }
        });

        return card;
    }
}