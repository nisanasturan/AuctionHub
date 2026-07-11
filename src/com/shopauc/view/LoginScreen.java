package com.shopauc.view;

import com.shopauc.model.User;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("AuctionHub");
        setSize(400, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Logo
        JLabel logo = new JLabel("AuctionHub");
        logo.setFont(ThemeManager.FONT_TITLE);
        logo.setForeground(ThemeManager.ACCENT);
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Buy, sell, and bid with confidence");
        tagline.setFont(ThemeManager.FONT_SMALL);
        tagline.setForeground(ThemeManager.TEXT_MUTED);
        tagline.setAlignmentX(CENTER_ALIGNMENT);

        // Card
        JPanel card = new JPanel();
        card.setBackground(ThemeManager.SURFACE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(24, 24, 24, 24)));
        card.setAlignmentX(CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel formTitle = new JLabel("Sign in");
        formTitle.setFont(ThemeManager.FONT_HEADING);
        formTitle.setForeground(ThemeManager.TEXT_PRIMARY);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        JTextField emailField = makeField("Email address");
        JPasswordField passField = (JPasswordField) makeField("Password");

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(ThemeManager.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        RoundedButton loginBtn = new RoundedButton("Sign in",
                ThemeManager.ACCENT, Color.WHITE);
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeManager.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        RoundedButton registerBtn = new RoundedButton("Create an account", true);
        registerBtn.setForeground(ThemeManager.TEXT_PRIMARY);
        registerBtn.setAlignmentX(LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        card.add(formTitle);
        card.add(Box.createVerticalStrut(20));
        card.add(makeLabel("Email"));
        card.add(Box.createVerticalStrut(4));
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));
        card.add(makeLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        card.add(passField);
        card.add(Box.createVerticalStrut(4));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));
        card.add(registerBtn);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass  = new String(((JPasswordField) passField).getPassword()).trim();
            if (email.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }
            User user = Marketplace.getInstance().login(email, pass);
            if (user == null) {
                errorLabel.setText("Invalid email or password.");
            } else {
                dispose();
                new MainScreen().setVisible(true);
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterScreen().setVisible(true);
        });

        center.add(logo);
        center.add(Box.createVerticalStrut(4));
        center.add(tagline);
        center.add(Box.createVerticalStrut(24));
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

    private JTextField makeField(String placeholder) {
        JTextField f = placeholder.equals("Password")
                ? new JPasswordField() : new JTextField();
        f.setFont(ThemeManager.FONT_BODY);
        f.setForeground(ThemeManager.TEXT_PRIMARY);
        f.setBackground(ThemeManager.SURFACE_1);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(LEFT_ALIGNMENT);
        return f;
    }
}