package com.shopauc.view;

import com.shopauc.exception.InsufficientStockException;
import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;

public class ProductListScreen extends JFrame {

    private JPanel listPanel;
    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> sortBox;
    private List<Product> allProducts;
    private boolean isSeller;

    public ProductListScreen() {
        setTitle("AuctionHub — Products");
        setSize(640, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Marketplace mp = Marketplace.getInstance();
        isSeller = mp.getCurrentUser() instanceof Seller;
        allProducts = isSeller
                ? mp.getProductsBySeller(mp.getCurrentUser().getId())
                : mp.getProducts().stream()
                    .filter(p -> !(p instanceof AuctionProduct))
                    .collect(Collectors.toList());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        root.add(makeTopBar(), BorderLayout.NORTH);
        root.add(makeBody(), BorderLayout.CENTER);
    }

    private JPanel makeTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ThemeManager.SURFACE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel title = new JLabel(isSeller ? "My Listings" : "Browse Products");
        title.setFont(ThemeManager.FONT_HEADING);
        title.setForeground(ThemeManager.TEXT_PRIMARY);

        RoundedButton backBtn = new RoundedButton("Back", true);
        backBtn.setForeground(ThemeManager.TEXT_PRIMARY);
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        bar.add(title, BorderLayout.WEST);
        bar.add(backBtn, BorderLayout.EAST);
        return bar;
    }

    private JPanel makeBody() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ThemeManager.PAGE_BG);

        // ── Filter bar (2 satır) ─────────────────────────────
        JPanel filterBar = new JPanel();
        filterBar.setLayout(new BoxLayout(filterBar, BoxLayout.Y_AXIS));
        filterBar.setBackground(ThemeManager.SURFACE);
        filterBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(8, 16, 8, 16)));

        // Satır 1: Search + Category
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row1.setOpaque(false);

        searchField = new JTextField(16);
        searchField.setFont(ThemeManager.FONT_BODY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(5, 10, 5, 10)));

        String[] cats = {"All Categories", "Technology", "Home & Living",
                         "Cosmetics", "Fashion", "Sports", "General"};
        categoryBox = new JComboBox<>(cats);
        categoryBox.setFont(ThemeManager.FONT_BODY);

        JLabel searchLbl = new JLabel("Search:");
        searchLbl.setFont(ThemeManager.FONT_LABEL);
        searchLbl.setForeground(ThemeManager.TEXT_SECONDARY);

        JLabel catLbl = new JLabel("Category:");
        catLbl.setFont(ThemeManager.FONT_LABEL);
        catLbl.setForeground(ThemeManager.TEXT_SECONDARY);

        row1.add(searchLbl);
        row1.add(searchField);
        row1.add(catLbl);
        row1.add(categoryBox);

        // Satır 2: Sort + Search butonu
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row2.setOpaque(false);

        String[] sorts = {"Default", "Price: Low to High", "Price: High to Low", "Name A-Z"};
        sortBox = new JComboBox<>(sorts);
        sortBox.setFont(ThemeManager.FONT_BODY);

        JLabel sortLbl = new JLabel("Sort:");
        sortLbl.setFont(ThemeManager.FONT_LABEL);
        sortLbl.setForeground(ThemeManager.TEXT_SECONDARY);

        RoundedButton searchBtn = new RoundedButton("Search", ThemeManager.ACCENT, Color.WHITE);
        searchBtn.addActionListener(e -> refreshList());

        row2.add(sortLbl);
        row2.add(sortBox);
        row2.add(searchBtn);

        filterBar.add(row1);
        filterBar.add(row2);

        // ── List panel ───────────────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.PAGE_BG);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        wrapper.add(filterBar, BorderLayout.NORTH);
        wrapper.add(scroll, BorderLayout.CENTER);

        refreshList();
        return wrapper;
    }

    private void refreshList() {
        listPanel.removeAll();

        String query    = searchField.getText().trim().toLowerCase();
        String category = (String) categoryBox.getSelectedItem();
        String sort     = (String) sortBox.getSelectedItem();

        List<Product> filtered = allProducts.stream()
                .filter(p -> query.isEmpty() || p.getName().toLowerCase().contains(query))
                .filter(p -> "All Categories".equals(category) || category.equals(p.getCategory()))
                .collect(Collectors.toList());

        if ("Price: Low to High".equals(sort))
            filtered.sort(Comparator.comparingDouble(Product::getPrice));
        else if ("Price: High to Low".equals(sort))
            filtered.sort(Comparator.comparingDouble(Product::getPrice).reversed());
        else if ("Name A-Z".equals(sort))
            filtered.sort(Comparator.comparing(Product::getName));

        if (filtered.isEmpty()) {
            JLabel empty = new JLabel("No products found.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (Product p : filtered) {
                listPanel.add(makeProductCard(p));
                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel makeProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(ThemeManager.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel dot = new JPanel();
        dot.setBackground(ThemeManager.getCategoryColor(p.getCategory()));
        dot.setPreferredSize(new Dimension(48, 48));
        dot.setBorder(BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(p.getName());
        name.setFont(ThemeManager.FONT_SUB);
        name.setForeground(ThemeManager.TEXT_PRIMARY);

        JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        metaRow.setOpaque(false);

        JLabel catBadge = new JLabel(p.getCategory());
        catBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catBadge.setForeground(ThemeManager.getCategoryTextColor(p.getCategory()));
        catBadge.setOpaque(true);
        catBadge.setBackground(ThemeManager.getCategoryColor(p.getCategory()));
        catBadge.setBorder(new EmptyBorder(2, 6, 2, 6));

        JLabel stock = new JLabel("Stock: " + p.getStock());
        stock.setFont(ThemeManager.FONT_SMALL);
        stock.setForeground(ThemeManager.TEXT_MUTED);

        metaRow.add(catBadge);
        metaRow.add(stock);

        info.add(name);
        info.add(Box.createVerticalStrut(4));
        info.add(metaRow);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel price = new JLabel("$" + String.format("%.2f", p.getPrice()));
        price.setFont(ThemeManager.FONT_HEADING);
        price.setForeground(ThemeManager.TEXT_PRIMARY);
        price.setAlignmentX(RIGHT_ALIGNMENT);

        right.add(price);

        if (!isSeller) {
            RoundedButton addBtn = new RoundedButton("Add to cart",
                    ThemeManager.ACCENT, Color.WHITE);
            addBtn.setFont(ThemeManager.FONT_SMALL);
            addBtn.addActionListener(e -> {
                try {
                    Marketplace.getInstance().addToCart(p, 1);
                    JOptionPane.showMessageDialog(this,
                            p.getName() + " added to cart!",
                            "Added", JOptionPane.INFORMATION_MESSAGE);
                } catch (InsufficientStockException ex) {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(), "Out of stock",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
            right.add(Box.createVerticalStrut(6));
            right.add(addBtn);
        }

        card.add(dot, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}