package com.shopauc.view;

import com.shopauc.exception.InsufficientStockException;
import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CartScreen extends JFrame {

    public CartScreen() {
        setTitle("AuctionHub — Cart");
        setSize(520, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.BG_LIGHT);
        setContentPane(root);

        // ── Header ──────────────────────────────────────────
        GradientPanel header = new GradientPanel(ThemeManager.ORANGE, ThemeManager.PINK);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 20, 20, 20));
        header.setPreferredSize(new Dimension(520, 80));

        JLabel titleLabel = new JLabel("🛒  My Cart");
        titleLabel.setFont(ThemeManager.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);

        RoundedButton backBtn = new RoundedButton("← Back",
                new Color(255,255,255,60), new Color(255,255,255,30));
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);

        // ── List ────────────────────────────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.BG_LIGHT);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        Marketplace mp = Marketplace.getInstance();
        List<CartItem> items = mp.getCart();

        if (items.isEmpty()) {
            JLabel empty = new JLabel("Your cart is empty.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_GRAY);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (CartItem ci : items) {
                listPanel.add(makeCartCard(ci));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Footer ──────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(ThemeManager.WHITE);
        footer.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel total = new JLabel("Total: $" + String.format("%.2f", mp.getCartTotal()));
        total.setFont(ThemeManager.FONT_HEADING);
        total.setForeground(ThemeManager.TEXT_DARK);

        RoundedButton checkoutBtn = new RoundedButton("Checkout",
                ThemeManager.PURPLE, ThemeManager.PINK);
        checkoutBtn.addActionListener(e -> {
            try {
                Order order = mp.checkout();
                if (order != null) {
                    JOptionPane.showMessageDialog(this,
                            "Order #" + order.getId() + " placed!\nTotal: $"
                            + String.format("%.2f", order.getTotalAmount()),
                            "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainScreen().setVisible(true);
                }
            } catch (InsufficientStockException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Stock Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(total, BorderLayout.WEST);
        footer.add(checkoutBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
    }

    private JPanel makeCartCard(CartItem ci) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ThemeManager.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel icon = new JLabel("🛍");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JPanel iconWrap = new JPanel(new BorderLayout());
        iconWrap.setBackground(ThemeManager.AMBER_SOFT);
        iconWrap.setPreferredSize(new Dimension(52, 52));
        iconWrap.add(icon, BorderLayout.CENTER);
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(ci.getProduct().getName());
        name.setFont(ThemeManager.FONT_SUB);
        name.setForeground(ThemeManager.TEXT_DARK);

        JLabel qty = new JLabel("Qty: " + ci.getQuantity());
        qty.setFont(ThemeManager.FONT_SMALL);
        qty.setForeground(ThemeManager.TEXT_GRAY);

        JLabel price = new JLabel("$" + String.format("%.2f", ci.getTotalPrice()));
        price.setFont(ThemeManager.FONT_BODY);
        price.setForeground(ThemeManager.ORANGE);

        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(qty);
        info.add(Box.createVerticalStrut(2));
        info.add(price);

        RoundedButton removeBtn = new RoundedButton("Remove",
                new Color(220, 53, 69), new Color(200, 35, 51));
        removeBtn.addActionListener(e -> {
            Marketplace.getInstance().removeFromCart(ci.getProduct().getId());
            dispose();
            new CartScreen().setVisible(true);
        });

        card.add(iconWrap, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(removeBtn, BorderLayout.EAST);

        return card;
    }
}