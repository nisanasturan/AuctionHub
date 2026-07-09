package com.shopauc.view;

import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegisterScreen extends JFrame {

    public RegisterScreen() {
        setTitle("AuctionHub — Register");
        setSize(420, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GradientPanel mainPanel = new GradientPanel(ThemeManager.PINK, ThemeManager.ORANGE);
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // ── Header ──────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(48, 36, 28, 36));

        JLabel logo = new JLabel("AuctionHub");
        logo.setFont(ThemeManager.FONT_TITLE);
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Create your account");
        sub.setFont(ThemeManager.FONT_SMALL);
        sub.setForeground(new Color(255, 255, 255, 180));
        sub.setAlignmentX(LEFT_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);

        // ── Form Card ───────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(ThemeManager.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 32, 32, 32));

        JLabel title = new JLabel("Join us today 🚀");
        title.setFont(ThemeManager.FONT_HEADING);
        title.setForeground(ThemeManager.TEXT_DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JTextField nameField  = makeField("Full name");
        JTextField emailField = makeField("Email address");
        JPasswordField passField = new JPasswordField();
        styleField(passField);

        // Role seçimi
        JLabel roleLabel = makeLabel("I want to");
        JPanel rolePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        rolePanel.setOpaque(false);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        rolePanel.setAlignmentX(LEFT_ALIGNMENT);

        JToggleButton buyerBtn  = makeRoleButton("🛍  Buyer");
        JToggleButton sellerBtn = makeRoleButton("🏪  Seller");
        ButtonGroup group = new ButtonGroup();
        group.add(buyerBtn);
        group.add(sellerBtn);
        buyerBtn.setSelected(true);
        buyerBtn.setBackground(ThemeManager.PURPLE_SOFT);
        buyerBtn.setForeground(ThemeManager.PURPLE);

        buyerBtn.addActionListener(e -> {
            buyerBtn.setBackground(ThemeManager.PURPLE_SOFT);
            buyerBtn.setForeground(ThemeManager.PURPLE);
            sellerBtn.setBackground(ThemeManager.BG_LIGHT);
            sellerBtn.setForeground(ThemeManager.TEXT_GRAY);
        });
        sellerBtn.addActionListener(e -> {
            sellerBtn.setBackground(ThemeManager.PINK_SOFT);
            sellerBtn.setForeground(ThemeManager.PINK);
            buyerBtn.setBackground(ThemeManager.BG_LIGHT);
            buyerBtn.setForeground(ThemeManager.TEXT_GRAY);
        });

        rolePanel.add(buyerBtn);
        rolePanel.add(sellerBtn);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        RoundedButton registerBtn = new RoundedButton("Create Account",
                ThemeManager.PINK, ThemeManager.ORANGE);
        registerBtn.setAlignmentX(LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        RoundedButton backBtn = new RoundedButton("Back to Login",
                ThemeManager.PURPLE, ThemeManager.PINK);
        backBtn.setAlignmentX(LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(makeLabel("Full Name"));
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
        card.add(roleLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(rolePanel);
        card.add(Box.createVerticalStrut(6));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(backBtn);

        // ── Actions ─────────────────────────────────────────
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
            boolean success = Marketplace.getInstance().register(name, email, pass, role);
            if (!success) {
                errorLabel.setText("This email is already registered.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Account created! Please login.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginScreen().setVisible(true);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });

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

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    private void styleField(JTextField f) {
        f.setFont(ThemeManager.FONT_BODY);
        f.setForeground(ThemeManager.TEXT_DARK);
        f.setBackground(ThemeManager.BG_LIGHT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        f.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JToggleButton makeRoleButton(String text) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(ThemeManager.FONT_SUB);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(ThemeManager.BG_LIGHT);
        btn.setForeground(ThemeManager.TEXT_GRAY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}