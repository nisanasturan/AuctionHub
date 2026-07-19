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
                : mp.getProducts();

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

        JPanel filterBar = new JPanel();
        filterBar.setLayout(new BoxLayout(filterBar, BoxLayout.Y_AXIS));
        filterBar.setBackground(ThemeManager.SURFACE);
        filterBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(8, 16, 8, 16)));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row1.setOpaque(false);

        searchField = new JTextField(16);
        searchField.setFont(ThemeManager.FONT_BODY);
        searchField.setForeground(ThemeManager.TEXT_PRIMARY);
        searchField.setBackground(ThemeManager.SURFACE_1);
        searchField.setCaretColor(ThemeManager.TEXT_PRIMARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(5, 10, 5, 10)));

       String[] cats = {"All Categories", "Clothing", "Electronics", "Home & Living", "Personal Care", "Sports & Outdoor"};
        categoryBox = new JComboBox<>(cats);
        categoryBox.setFont(ThemeManager.FONT_BODY);
        categoryBox.setBackground(ThemeManager.SURFACE_1);
        categoryBox.setForeground(ThemeManager.TEXT_PRIMARY);

        JLabel searchLbl = makeLabel("Search:");
        JLabel catLbl    = makeLabel("Category:");

        row1.add(searchLbl);
        row1.add(searchField);
        row1.add(catLbl);
        row1.add(categoryBox);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row2.setOpaque(false);

        String[] sorts = {"Default", "Price: Low to High", "Price: High to Low", "Name A-Z"};
        sortBox = new JComboBox<>(sorts);
        sortBox.setFont(ThemeManager.FONT_BODY);
        sortBox.setBackground(ThemeManager.SURFACE_1);
        sortBox.setForeground(ThemeManager.TEXT_PRIMARY);

        RoundedButton searchBtn = new RoundedButton("Search", ThemeManager.ACCENT, Color.WHITE);
        searchBtn.addActionListener(e -> refreshList());

        row2.add(makeLabel("Sort:"));
        row2.add(sortBox);
        row2.add(searchBtn);

        filterBar.add(row1);
        filterBar.add(row2);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.PAGE_BG);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(ThemeManager.PAGE_BG);

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
                .filter(p -> query.isEmpty() ||
                        p.getName().toLowerCase().contains(query) ||
                        p.getBrand().toLowerCase().contains(query))
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // İkon kutusu
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

        // Marka — koyu, kalın
        JLabel brandLabel = new JLabel(p.getBrand());
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brandLabel.setForeground(ThemeManager.TEXT_PRIMARY);

        // Ürün adı — soluk, ince
        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(ThemeManager.TEXT_SECONDARY);

        // Meta row
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

        info.add(brandLabel);
        info.add(Box.createVerticalStrut(2));
        info.add(nameLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(metaRow);

        // Sağ
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel price = new JLabel("$" + String.format("%.2f", p.getPrice()));
        price.setFont(ThemeManager.FONT_HEADING);
        price.setForeground(ThemeManager.TEXT_PRIMARY);
        price.setAlignmentX(RIGHT_ALIGNMENT);

        right.add(price);

        if (!isSeller) {
            RoundedButton addBtn = new RoundedButton("Add to cart", ThemeManager.ACCENT, Color.WHITE);
            addBtn.setFont(ThemeManager.FONT_SMALL);
            addBtn.addActionListener(e -> {
                try {
                    Marketplace.getInstance().addToCart(p, 1);
                    JOptionPane.showMessageDialog(this,
                            p.getBrand() + " " + p.getName() + " added to cart!",
                            "Added", JOptionPane.INFORMATION_MESSAGE);
                } catch (InsufficientStockException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(),
                            "Out of stock", JOptionPane.WARNING_MESSAGE);
                }
            });
            right.add(Box.createVerticalStrut(6));
            right.add(addBtn);
        }

        card.add(iconBox, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ThemeManager.FONT_LABEL);
        l.setForeground(ThemeManager.TEXT_SECONDARY);
        return l;
    }
}