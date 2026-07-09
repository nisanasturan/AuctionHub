package com.shopauc.view;

import com.shopauc.exception.InsufficientStockException;
import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductListScreen extends JFrame {

    public ProductListScreen() {
        setTitle("AuctionHub — Products");
        setSize(520, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.BG_LIGHT);
        setContentPane(root);

        // ── Header ──────────────────────────────────────────
        GradientPanel header = new GradientPanel(ThemeManager.PURPLE, ThemeManager.PINK);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 20, 20, 20));
        header.setPreferredSize(new Dimension(520, 80));

        JLabel titleLabel = new JLabel("🛍  Products");
        titleLabel.setFont(ThemeManager.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);

        RoundedButton backBtn = new RoundedButton("← Back",
                new Color(255,255,255,60), new Color(255,255,255,30));
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);

        // ── Product List ─────────────────────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.BG_LIGHT);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        Marketplace mp = Marketplace.getInstance();
        boolean isSeller = mp.getCurrentUser() instanceof Seller;

        List<Product> products = isSeller
                ? mp.getProductsBySeller(mp.getCurrentUser().getId())
                : mp.getProducts().stream()
                    .filter(p -> !(p instanceof AuctionProduct))
                    .toList();

        if (products.isEmpty()) {
            JLabel empty = new JLabel("No products found.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_GRAY);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (Product p : products) {
                listPanel.add(makeProductCard(p, isSeller));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
    }

    private JPanel makeProductCard(Product p, boolean isSeller) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ThemeManager.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Icon
        JLabel icon = new JLabel("📦");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        icon.setPreferredSize(new Dimension(48, 48));
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel iconWrap = new JPanel(new BorderLayout());
        iconWrap.setBackground(ThemeManager.PURPLE_SOFT);
        iconWrap.setPreferredSize(new Dimension(52, 52));
        iconWrap.add(icon, BorderLayout.CENTER);

        // Info
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(p.getName());
        name.setFont(ThemeManager.FONT_SUB);
        name.setForeground(ThemeManager.TEXT_DARK);

        JLabel price = new JLabel("$" + String.format("%.2f", p.getPrice()));
        price.setFont(ThemeManager.FONT_BODY);
        price.setForeground(ThemeManager.PURPLE);

        JLabel stock = new JLabel("Stock: " + p.getStock());
        stock.setFont(ThemeManager.FONT_SMALL);
        stock.setForeground(ThemeManager.TEXT_GRAY);

        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(price);
        info.add(Box.createVerticalStrut(2));
        info.add(stock);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        if (!isSeller) {
            RoundedButton addBtn = new RoundedButton("Add to Cart",
                    ThemeManager.PURPLE, ThemeManager.PINK);
            addBtn.addActionListener(e -> {
                try {
                    Marketplace.getInstance().addToCart(p, 1);
                    JOptionPane.showMessageDialog(this,
                            p.getName() + " added to cart!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (InsufficientStockException ex) {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(), "Out of Stock",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
            card.add(addBtn, BorderLayout.EAST);
        }

        return card;
    }
}