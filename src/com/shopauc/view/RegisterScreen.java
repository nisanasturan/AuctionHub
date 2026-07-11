package com.shopauc.view;

import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class RegisterScreen extends JFrame {

    public RegisterScreen() {
        setTitle("AuctionHub — Register");
        setSize(400, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(32, 40, 32, 40));

        JLabel logo = new JLabel("AuctionHub");
        logo.setFont(ThemeManager.FONT_TITLE);
        logo.setForeground(ThemeManager.ACCENT);
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JPanel card = new JPanel();
        card.setBackground(ThemeManager.SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(24, 24, 24, 24)));
        card.setAlignmentX(CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel formTitle = new JLabel("Create account");
        formTitle.setFont(ThemeManager.FONT_HEADING);
        formTitle.setForeground(ThemeManager.TEXT_PRIMARY);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        JTextField nameField  = makeField();
        JTextField emailField = makeField();
        JPasswordField passField = new JPasswordField();
        styleField(passField);

        // Role toggle
        JPanel rolePanel = new JPanel(new GridLayout(1, 2, 8, 0));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        rolePanel.setAlignmentX(LEFT_ALIGNMENT);

        JToggleButton buyerBtn  = makeRoleBtn("Buyer");
        JToggleButton sellerBtn = makeRoleBtn("Seller");
        ButtonGroup bg = new ButtonGroup();
        bg.add(buyerBtn); bg.add(sellerBtn);
        buyerBtn.setSelected(true);
        buyerBtn.setBackground(ThemeManager.ACCENT_LIGHT);
        buyerBtn.setForeground(ThemeManager.ACCENT);

        buyerBtn.addActionListener(e -> {
            buyerBtn.setBackground(ThemeManager.ACCENT_LIGHT);
            buyerBtn.setForeground(ThemeManager.ACCENT);
            sellerBtn.setBackground(ThemeManager.SURFACE_1);
            sellerBtn.setForeground(ThemeManager.TEXT_SECONDARY);
        });
        sellerBtn.addActionListener(e -> {
            sellerBtn.setBackground(ThemeManager.ACCENT_LIGHT);
            sellerBtn.setForeground(ThemeManager.ACCENT);
            buyerBtn.setBackground(ThemeManager.SURFACE_1);
            buyerBtn.setForeground(ThemeManager.TEXT_SECONDARY);
        });

        rolePanel.add(buyerBtn);
        rolePanel.add(sellerBtn);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(ThemeManager.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        RoundedButton registerBtn = new RoundedButton("Create account",
                ThemeManager.ACCENT, Color.WHITE);
        registerBtn.setAlignmentX(LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        RoundedButton backBtn = new RoundedButton("Back to sign in", true);
        backBtn.setForeground(ThemeManager.TEXT_PRIMARY);
        backBtn.setAlignmentX(LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        card.add(formTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(makeLabel("Full name"));
        card.add(Box.createVerticalStrut(4));
        card.add(nameField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Email"));
        card.add(Box.createVerticalStrut(4));
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        card.add(passField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Account type"));
        card.add(Box.createVerticalStrut(6));
        card.add(rolePanel);
        card.add(Box.createVerticalStrut(4));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(backBtn);

        registerBtn.addActionListener(e -> {
            String name  = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pass  = new String(passField.getPassword()).trim();
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            if (pass.length() < 6) {
                errorLabel.setText("Password must be at least 6 characters.");
                return;
            }
            String role = sellerBtn.isSelected() ? "SELLER" : "BUYER";
            if (!Marketplace.getInstance().register(name, email, pass, role)) {
                errorLabel.setText("This email is already registered.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Account created! Please sign in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginScreen().setVisible(true);
            }
        });

        backBtn.addActionListener(e -> { dispose(); new LoginScreen().setVisible(true); });

        center.add(logo);
        center.add(Box.createVerticalStrut(20));
        center.add(card);

        root.add(center, BorderLayout.CENTER);
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
        styleField(f);
        return f;
    }

    private void styleField(JTextField f) {
        f.setFont(ThemeManager.FONT_BODY);
        f.setForeground(ThemeManager.TEXT_PRIMARY);
        f.setBackground(ThemeManager.SURFACE_1);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JToggleButton makeRoleBtn(String text) {
        JToggleButton b = new JToggleButton(text);
        b.setFont(ThemeManager.FONT_SUB);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true));
        b.setBackground(ThemeManager.SURFACE_1);
        b.setForeground(ThemeManager.TEXT_SECONDARY);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}