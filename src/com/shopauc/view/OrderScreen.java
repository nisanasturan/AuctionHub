package com.shopauc.view;

import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class OrderScreen extends JFrame {

    public OrderScreen() {
        setTitle("AuctionHub — Orders");
        setSize(560, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeManager.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel title = new JLabel("My Orders");
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
        List<Order> orders = mp.getOrdersByBuyer(mp.getCurrentUser().getId());

        if (orders.isEmpty()) {
            JLabel empty = new JLabel("No orders yet.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (Order o : orders) {
                listPanel.add(makeOrderCard(o));
                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(topBar, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
    }

    private JPanel makeOrderCard(Order o) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ThemeManager.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(14, 14, 14, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel dot = new JPanel();
        dot.setBackground(ThemeManager.SUCCESS_LIGHT);
        dot.setPreferredSize(new Dimension(44, 44));
        dot.setBorder(BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel orderId = new JLabel("Order #" + o.getId());
        orderId.setFont(ThemeManager.FONT_SUB);
        orderId.setForeground(ThemeManager.TEXT_PRIMARY);

        JLabel date = new JLabel(o.getOrderDate().toString().replace("T", " ").substring(0, 16));
        date.setFont(ThemeManager.FONT_SMALL);
        date.setForeground(ThemeManager.TEXT_MUTED);

        JLabel status = new JLabel(o.getStatus());
        status.setFont(new Font("Segoe UI", Font.BOLD, 10));
        status.setForeground(ThemeManager.SUCCESS);
        status.setOpaque(true);
        status.setBackground(ThemeManager.SUCCESS_LIGHT);
        status.setBorder(new EmptyBorder(2, 6, 2, 6));

        info.add(orderId);
        info.add(Box.createVerticalStrut(3));
        info.add(date);
        info.add(Box.createVerticalStrut(3));
        info.add(status);

        JLabel total = new JLabel("$" + String.format("%.2f", o.getTotalAmount()));
        total.setFont(ThemeManager.FONT_HEADING);
        total.setForeground(ThemeManager.TEXT_PRIMARY);

        card.add(dot, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(total, BorderLayout.EAST);

        return card;
    }
}