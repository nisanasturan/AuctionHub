package com.shopauc.view;

import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class OrderScreen extends JFrame {

    public OrderScreen() {
        setTitle("AuctionHub — Orders");
        setSize(520, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.BG_LIGHT);
        setContentPane(root);

        GradientPanel header = new GradientPanel(ThemeManager.TEAL, ThemeManager.PURPLE);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 20, 20, 20));
        header.setPreferredSize(new Dimension(520, 80));

        JLabel titleLabel = new JLabel("My Orders");
        titleLabel.setFont(ThemeManager.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);

        RoundedButton backBtn = new RoundedButton("Back",
                new Color(255, 255, 255, 60), new Color(255, 255, 255, 30));
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.BG_LIGHT);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        Marketplace mp = Marketplace.getInstance();
        List<Order> orders = mp.getOrdersByBuyer(mp.getCurrentUser().getId());

        if (orders.isEmpty()) {
            JLabel empty = new JLabel("No orders yet.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_GRAY);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (Order o : orders) {
                listPanel.add(makeCard(o));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
    }

    private JPanel makeCard(Order o) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ThemeManager.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel iconWrap = new JPanel(new BorderLayout());
        iconWrap.setBackground(ThemeManager.TEAL_SOFT);
        iconWrap.setPreferredSize(new Dimension(52, 52));
        JLabel icon = new JLabel("O", SwingConstants.CENTER);
        icon.setFont(ThemeManager.FONT_HEADING);
        icon.setForeground(ThemeManager.TEAL);
        iconWrap.add(icon, BorderLayout.CENTER);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel orderId = new JLabel("Order #" + o.getId());
        orderId.setFont(ThemeManager.FONT_SUB);
        orderId.setForeground(ThemeManager.TEXT_DARK);

        JLabel total = new JLabel("Total: $" + String.format("%.2f", o.getTotalAmount()));
        total.setFont(ThemeManager.FONT_BODY);
        total.setForeground(ThemeManager.TEAL);

        JLabel status = new JLabel("Status: " + o.getStatus());
        status.setFont(ThemeManager.FONT_SMALL);
        status.setForeground(ThemeManager.TEXT_GRAY);

        JLabel date = new JLabel(o.getOrderDate().toString().replace("T", " ").substring(0, 16));
        date.setFont(ThemeManager.FONT_SMALL);
        date.setForeground(ThemeManager.TEXT_LIGHT);

        info.add(orderId);
        info.add(Box.createVerticalStrut(3));
        info.add(total);
        info.add(Box.createVerticalStrut(2));
        info.add(status);
        info.add(Box.createVerticalStrut(2));
        info.add(date);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        return card;
    }
}