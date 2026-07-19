package com.shopauc.view;

import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class AddProductScreen extends JFrame {

    public AddProductScreen() {
        setTitle("AuctionHub — Add Product");
        setSize(420, 560);
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

        JLabel title = new JLabel("Add Product");
        title.setFont(ThemeManager.FONT_HEADING);
        title.setForeground(ThemeManager.TEXT_PRIMARY);

        RoundedButton backBtn = new RoundedButton("Back", true);
        backBtn.setForeground(ThemeManager.TEXT_PRIMARY);
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);

        JPanel form = new JPanel();
        form.setBackground(ThemeManager.PAGE_BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel card = new JPanel();
        card.setBackground(ThemeManager.SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextField nameField  = makeField();
        JTextField brandField = makeField();
        JTextField priceField = makeField();
        JTextField stockField = makeField();

        JComboBox<String> catBox = new JComboBox<>(ThemeManager.CATEGORIES);
        catBox.setFont(ThemeManager.FONT_BODY);
        catBox.setBackground(ThemeManager.SURFACE_1);
        catBox.setForeground(ThemeManager.TEXT_PRIMARY);
        catBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        catBox.setAlignmentX(LEFT_ALIGNMENT);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(ThemeManager.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        RoundedButton addBtn = new RoundedButton("List product", ThemeManager.ACCENT, Color.WHITE);
        addBtn.setAlignmentX(LEFT_ALIGNMENT);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        card.add(makeLabel("Product name"));
        card.add(Box.createVerticalStrut(4));
        card.add(nameField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Brand"));
        card.add(Box.createVerticalStrut(4));
        card.add(brandField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Price ($)"));
        card.add(Box.createVerticalStrut(4));
        card.add(priceField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Stock quantity"));
        card.add(Box.createVerticalStrut(4));
        card.add(stockField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Category"));
        card.add(Box.createVerticalStrut(4));
        card.add(catBox);
        card.add(Box.createVerticalStrut(6));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(addBtn);

        addBtn.addActionListener(e -> {
            String name     = nameField.getText().trim();
            String brand    = brandField.getText().trim();
            String priceStr = priceField.getText().trim();
            String stockStr = stockField.getText().trim();
            String category = (String) catBox.getSelectedItem();
            if (name.isEmpty() || brand.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            try {
                double price = Double.parseDouble(priceStr);
                int stock    = Integer.parseInt(stockStr);
                if (price <= 0 || stock < 0) {
                    errorLabel.setText("Price must be > 0 and stock >= 0.");
                    return;
                }
                Marketplace.getInstance().addProduct(name, brand, price, stock, category);
                JOptionPane.showMessageDialog(this, name + " listed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainScreen().setVisible(true);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Price and stock must be numbers.");
            }
        });

        form.add(card);
        root.add(topBar, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ThemeManager.FONT_LABEL);
        l.setForeground(ThemeManager.TEXT_SECONDARY);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(ThemeManager.FONT_BODY);
        f.setForeground(ThemeManager.TEXT_PRIMARY);
        f.setBackground(ThemeManager.SURFACE_1);
        f.setCaretColor(ThemeManager.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(LEFT_ALIGNMENT);
        return f;
    }
}