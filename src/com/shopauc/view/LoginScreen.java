package com.shopauc.view;
import com.shopauc.model.User;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("AuctionHub — Login");
        setSize(420, 580); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); setResizable(false);
        GradientPanel main = new GradientPanel(ThemeManager.PURPLE, ThemeManager.PINK);
        main.setLayout(new BorderLayout()); setContentPane(main);
        JPanel header = new JPanel(); header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(48,36,28,36));
        JLabel logo = new JLabel("AuctionHub"); logo.setFont(ThemeManager.FONT_TITLE); logo.setForeground(Color.WHITE); logo.setAlignmentX(LEFT_ALIGNMENT);
        JLabel sub = new JLabel("Marketplace & Auction Platform"); sub.setFont(ThemeManager.FONT_SMALL); sub.setForeground(new Color(255,255,255,180)); sub.setAlignmentX(LEFT_ALIGNMENT);
        header.add(logo); header.add(Box.createVerticalStrut(4)); header.add(sub);
        JPanel card = new JPanel(); card.setBackground(ThemeManager.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28,32,32,32));
        JLabel welcome = new JLabel("Welcome back!"); welcome.setFont(ThemeManager.FONT_HEADING); welcome.setForeground(ThemeManager.TEXT_DARK); welcome.setAlignmentX(LEFT_ALIGNMENT);
        JTextField emailField = makeField();
        JPasswordField passField = new JPasswordField(); styleField(passField);
        RoundedButton loginBtn = new RoundedButton("Login", ThemeManager.PURPLE, ThemeManager.PINK);
        loginBtn.setAlignmentX(LEFT_ALIGNMENT); loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        RoundedButton registerBtn = new RoundedButton("Create Account", ThemeManager.PINK, ThemeManager.ORANGE);
        registerBtn.setAlignmentX(LEFT_ALIGNMENT); registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        JLabel errorLabel = new JLabel(" "); errorLabel.setFont(ThemeManager.FONT_SMALL); errorLabel.setForeground(new Color(220,53,69)); errorLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(welcome); card.add(Box.createVerticalStrut(24));
        card.add(makeLabel("Email")); card.add(Box.createVerticalStrut(4)); card.add(emailField);
        card.add(Box.createVerticalStrut(14)); card.add(makeLabel("Password")); card.add(Box.createVerticalStrut(4)); card.add(passField);
        card.add(Box.createVerticalStrut(6)); card.add(errorLabel); card.add(Box.createVerticalStrut(8));
        card.add(loginBtn); card.add(Box.createVerticalStrut(10)); card.add(registerBtn);
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (email.isEmpty() || pass.isEmpty()) { errorLabel.setText("Please fill in all fields."); return; }
            User user = Marketplace.getInstance().login(email, pass);
            if (user == null) { errorLabel.setText("Invalid email or password."); }
            else { dispose(); new MainScreen().setVisible(true); }
        });
        registerBtn.addActionListener(e -> { dispose(); new RegisterScreen().setVisible(true); });
        main.add(header, BorderLayout.NORTH); main.add(card, BorderLayout.CENTER);
    }
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(ThemeManager.FONT_LABEL); l.setForeground(ThemeManager.TEXT_GRAY); l.setAlignmentX(LEFT_ALIGNMENT); return l;
    }
    private JTextField makeField() { JTextField f = new JTextField(); styleField(f); return f; }
    private void styleField(JTextField f) {
        f.setFont(ThemeManager.FONT_BODY); f.setForeground(ThemeManager.TEXT_DARK); f.setBackground(ThemeManager.BG_LIGHT);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,230),1,true), new EmptyBorder(10,14,10,14)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,44)); f.setAlignmentX(LEFT_ALIGNMENT);
    }
}