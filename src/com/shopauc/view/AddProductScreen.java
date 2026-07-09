package com.shopauc.view;

import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddProductScreen extends JFrame {

    public AddProductScreen() {
        setTitle("AuctionHub — Add Product");
        setSize(420, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GradientPanel mainPanel = new GradientPanel(ThemeManager.PURPLE, ThemeManager.PINK);
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(36, 32, 24, 32));

        JLabel logo = new JLabel("AuctionHub");
        logo.setFont(ThemeManager.FONT_TITLE);
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("List a new product");
        sub.setFont(ThemeManager.FONT_SMALL);
        sub.setForeground(new Color(255, 255, 255, 180));
        sub.setAlignmentX(LEFT_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);

        JPanel card = new JPanel();
        card.setBackground(ThemeManager.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 32, 32, 32));

        JLabel title = new JLabel("New Product ➕");
        title.setFont(ThemeManager.FONT_HEADING);
        title.setForeground(ThemeManager.TEXT_DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JTextField nameField  = makeField();
        JTextField priceField = makeField();
        JTextField stockField = makeField();

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        RoundedButton addBtn = new RoundedButton("Add Product",
                ThemeManager.PURPLE, ThemeManager.PINK);
        addBtn.setAlignmentX(LEFT_ALIGNMENT);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        RoundedButton backBtn = new RoundedButton("← Back",
                new Color(200,200,210), new Color(180,180,190));
        backBtn.setForeground(ThemeManager.TEXT_DARK);
        backBtn.setAlignmentX(LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(makeLabel("Product Name"));
        card.add(Box.createVerticalStrut(4));
        card.add(nameField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Price ($)"));
        card.add(Box.createVerticalStrut(4));
        card.add(priceField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Stock Quantity"));
        card.add(Box.createVerticalStrut(4));
        card.add(stockField);
        card.add(Box.createVerticalStrut(6));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(addBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(backBtn);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String stockStr = stockField.getText().trim();
            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);
                if (price <= 0 || stock < 0) {
                    errorLabel.setText("Price must be > 0 and stock >= 0.");
                    return;
                }
                Marketplace.getInstance().addProduct(name, price, stock);
                JOptionPane.showMessageDialog(this,
                        name + " listed successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainScreen().setVisible(true);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Price and stock must be numbers.");
            }
        });

        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ThemeManager.FONT_LABEL);
        l.setForeground(ThemeManager.TEXT_GRAY);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(ThemeManager.FONT_BODY);
        f.setForeground(ThemeManager.TEXT_DARK);
        f.setBackground(ThemeManager.BG_LIGHT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        f.setAlignmentX(LEFT_ALIGNMENT);
        return f;
    }
}