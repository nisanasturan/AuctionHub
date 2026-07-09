package com.shopauc.view;

import com.shopauc.exception.*;
import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AuctionScreen extends JFrame {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm");

    public AuctionScreen() {
        setTitle("AuctionHub — Auctions");
        setSize(560, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.BG_LIGHT);
        setContentPane(root);

        // ── Header ──────────────────────────────────────────
        GradientPanel header = new GradientPanel(ThemeManager.PINK, ThemeManager.ORANGE);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 20, 20, 20));
        header.setPreferredSize(new Dimension(560, 80));

        JLabel titleLabel = new JLabel("Live Auctions");
        titleLabel.setFont(ThemeManager.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);

        RoundedButton backBtn = new RoundedButton("Back",
                new Color(255, 255, 255, 60), new Color(255, 255, 255, 30));
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);

        // ── List ────────────────────────────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ThemeManager.BG_LIGHT);
        listPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        List<Product> auctions = Marketplace.getInstance().getProducts()
                .stream().filter(p -> p instanceof AuctionProduct).toList();

        if (auctions.isEmpty()) {
            JLabel empty = new JLabel("No active auctions.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_GRAY);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (Product p : auctions) {
                listPanel.add(makeAuctionCard((AuctionProduct) p));
                listPanel.add(Box.createVerticalStrut(14));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
    }

    private JPanel makeAuctionCard(AuctionProduct ap) {
        boolean expired = ap.isExpired();
        int currentUserId = Marketplace.getInstance().getCurrentUser().getId();
        boolean isWinner = ap.isWinner(currentUserId);

        // Ana kart
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ThemeManager.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        expired ? new Color(200, 200, 210) : ThemeManager.PINK, 1, true),
                new EmptyBorder(16, 18, 16, 18)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        // ── Başlık satırı ────────────────────────────────────
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel nameLabel = new JLabel(ap.getName());
        nameLabel.setFont(ThemeManager.FONT_HEADING);
        nameLabel.setForeground(ThemeManager.TEXT_DARK);

        JLabel statusBadge = new JLabel(expired ? "ENDED" : "LIVE");
        statusBadge.setFont(ThemeManager.FONT_LABEL);
        statusBadge.setForeground(Color.WHITE);
        statusBadge.setOpaque(true);
        statusBadge.setBackground(expired ? new Color(150, 150, 160) : new Color(220, 53, 69));
        statusBadge.setBorder(new EmptyBorder(3, 8, 3, 8));

        titleRow.add(nameLabel, BorderLayout.WEST);
        titleRow.add(statusBadge, BorderLayout.EAST);

        // ── Info grid ────────────────────────────────────────
        JPanel infoGrid = new JPanel(new GridLayout(3, 2, 12, 6));
        infoGrid.setOpaque(false);
        infoGrid.setBorder(new EmptyBorder(12, 0, 12, 0));

        infoGrid.add(makeInfoBox("Starting Price",
                "$" + String.format("%.2f", ap.getStartingPrice()), ThemeManager.PURPLE_SOFT, ThemeManager.PURPLE));
        infoGrid.add(makeInfoBox("Current Bid",
                "$" + String.format("%.2f", ap.getCurrentBid()), ThemeManager.PINK_SOFT, ThemeManager.PINK));
        infoGrid.add(makeInfoBox("Highest Bidder",
                ap.getHighestBidderName(), ThemeManager.AMBER_SOFT, ThemeManager.ORANGE));
        infoGrid.add(makeInfoBox("Total Bids",
                ap.getBidCount() + " bids", ThemeManager.TEAL_SOFT, ThemeManager.TEAL));
        infoGrid.add(makeInfoBox("Started",
                ap.getStartTime().format(FMT), new Color(240, 240, 248), ThemeManager.TEXT_GRAY));
        infoGrid.add(makeInfoBox("Ends",
                ap.getEndTime().format(FMT),
                expired ? new Color(255, 235, 235) : new Color(235, 255, 245),
                expired ? new Color(180, 50, 50) : ThemeManager.TEAL));

        // ── Kalan süre ───────────────────────────────────────
        JPanel timeRow = new JPanel(new BorderLayout());
        timeRow.setOpaque(false);
        timeRow.setBorder(new EmptyBorder(0, 0, 10, 0));

        String timeText;
        if (expired) {
            timeText = isWinner ? "Auction ended — You won!" : "Auction ended";
        } else {
            long mins = ChronoUnit.MINUTES.between(LocalDateTime.now(), ap.getEndTime());
            long hours = mins / 60;
            long remMins = mins % 60;
            timeText = hours > 0 ? "Time left: " + hours + "h " + remMins + "m"
                    : "Time left: " + remMins + " minutes";
        }

        JLabel timeLabel = new JLabel(timeText);
        timeLabel.setFont(ThemeManager.FONT_SUB);
        timeLabel.setForeground(expired
                ? (isWinner ? ThemeManager.TEAL : new Color(180, 50, 50))
                : ThemeManager.ORANGE);
        timeRow.add(timeLabel, BorderLayout.WEST);

        // ── Bid butonu ───────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);

        if (!expired) {
            RoundedButton bidBtn = new RoundedButton("Place Bid",
                    ThemeManager.PINK, ThemeManager.ORANGE);
            bidBtn.addActionListener(e -> showBidDialog(ap, card));
            btnRow.add(bidBtn);
        } else if (isWinner) {
            JLabel wonLabel = new JLabel("Congratulations! You won this auction.");
            wonLabel.setFont(ThemeManager.FONT_SUB);
            wonLabel.setForeground(ThemeManager.TEAL);
            btnRow.add(wonLabel);
        }

        card.add(titleRow);
        card.add(infoGrid);
        card.add(timeRow);
        card.add(btnRow);

        return card;
    }

    private JPanel makeInfoBox(String label, String value, Color bg, Color accent) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(bg);
        box.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel lbl = new JLabel(label);
        lbl.setFont(ThemeManager.FONT_SMALL);
        lbl.setForeground(ThemeManager.TEXT_GRAY);

        JLabel val = new JLabel(value);
        val.setFont(ThemeManager.FONT_SUB);
        val.setForeground(accent);

        box.add(lbl);
        box.add(Box.createVerticalStrut(2));
        box.add(val);
        return box;
    }

    private void showBidDialog(AuctionProduct ap, JPanel card) {
        String input = JOptionPane.showInputDialog(this,
                "Current bid: $" + String.format("%.2f", ap.getCurrentBid())
                + "\nMinimum bid: $" + String.format("%.2f", ap.getCurrentBid() + 0.01)
                + "\n\nEnter your bid amount:");
        if (input == null || input.isBlank()) return;
        try {
            double amount = Double.parseDouble(input.trim());
            Marketplace.getInstance().placeBid(ap, amount);
            JOptionPane.showMessageDialog(this,
                    "Bid of $" + String.format("%.2f", amount) + " placed successfully!",
                    "Bid Placed", JOptionPane.INFORMATION_MESSAGE);
            // Ekranı yenile
            dispose();
            new AuctionScreen().setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidBidException | AuctionExpiredException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Bid Rejected", JOptionPane.WARNING_MESSAGE);
        }
    }
}