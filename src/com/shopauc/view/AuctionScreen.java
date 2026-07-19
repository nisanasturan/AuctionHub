package com.shopauc.view;

import com.shopauc.exception.*;
import com.shopauc.model.*;
import com.shopauc.service.Marketplace;
import com.shopauc.util.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;

public class AuctionScreen extends JFrame {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm");
    private JPanel listPanel;
    private JComboBox<String> categoryBox;
    private JComboBox<String> sortBox;
    private JComboBox<String> statusBox;

    public AuctionScreen() {
        setTitle("AuctionHub — Auctions");
        setSize(620, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.PAGE_BG);
        setContentPane(root);

        root.add(makeTopBar(), BorderLayout.NORTH);
        root.add(makeContent(), BorderLayout.CENTER);
    }

    private JPanel makeTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ThemeManager.SURFACE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel title = new JLabel("Live Auctions");
        title.setFont(ThemeManager.FONT_HEADING);
        title.setForeground(ThemeManager.TEXT_PRIMARY);

        RoundedButton backBtn = new RoundedButton("Back", true);
        backBtn.setForeground(ThemeManager.TEXT_PRIMARY);
        backBtn.addActionListener(e -> { dispose(); new MainScreen().setVisible(true); });

        bar.add(title, BorderLayout.WEST);
        bar.add(backBtn, BorderLayout.EAST);
        return bar;
    }

    private JPanel makeContent() {
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

        String[] statuses = {"Active", "Ended", "All"};
        statusBox = new JComboBox<>(statuses);
        statusBox.setFont(ThemeManager.FONT_BODY);
        statusBox.setBackground(ThemeManager.SURFACE_1);
        statusBox.setForeground(ThemeManager.TEXT_PRIMARY);

        String[] cats = {"All Categories", "Clothing", "Electronics",
                         "Home & Living", "Personal Care", "Sports & Outdoor"};
        categoryBox = new JComboBox<>(cats);
        categoryBox.setFont(ThemeManager.FONT_BODY);
        categoryBox.setBackground(ThemeManager.SURFACE_1);
        categoryBox.setForeground(ThemeManager.TEXT_PRIMARY);

        row1.add(makeLabel("Status:"));
        row1.add(statusBox);
        row1.add(makeLabel("Category:"));
        row1.add(categoryBox);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row2.setOpaque(false);

        String[] sorts = {"Ending Soon", "Newly Added", "Price: Low to High", "Price: High to Low"};
        sortBox = new JComboBox<>(sorts);
        sortBox.setFont(ThemeManager.FONT_BODY);
        sortBox.setBackground(ThemeManager.SURFACE_1);
        sortBox.setForeground(ThemeManager.TEXT_PRIMARY);

        RoundedButton filterBtn = new RoundedButton("Filter", ThemeManager.ACCENT, Color.WHITE);
        filterBtn.addActionListener(e -> refreshList());

        row2.add(makeLabel("Sort:"));
        row2.add(sortBox);
        row2.add(filterBtn);

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

        String category = (String) categoryBox.getSelectedItem();
        String sort     = (String) sortBox.getSelectedItem();
        String status   = (String) statusBox.getSelectedItem();

        List<AuctionProduct> auctions = Marketplace.getInstance().getAuctions().stream()
                .filter(ap -> "All Categories".equals(category) || category.equals(ap.getCategory()))
                .filter(ap -> {
                    if ("Active".equals(status)) return !ap.isExpired();
                    if ("Ended".equals(status))  return ap.isExpired();
                    return true;
                })
                .collect(Collectors.toList());

        if ("Ending Soon".equals(sort))
            auctions.sort(Comparator.comparing(AuctionProduct::getEndTime));
        else if ("Newly Added".equals(sort))
            auctions.sort(Comparator.comparing(AuctionProduct::getStartTime).reversed());
        else if ("Price: Low to High".equals(sort))
            auctions.sort(Comparator.comparingDouble(AuctionProduct::getCurrentBid));
        else if ("Price: High to Low".equals(sort))
            auctions.sort(Comparator.comparingDouble(AuctionProduct::getCurrentBid).reversed());

        if (auctions.isEmpty()) {
            JLabel empty = new JLabel("No auctions found.");
            empty.setFont(ThemeManager.FONT_BODY);
            empty.setForeground(ThemeManager.TEXT_MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(40));
            listPanel.add(empty);
        } else {
            for (AuctionProduct ap : auctions) {
                listPanel.add(makeAuctionCard(ap));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel makeAuctionCard(AuctionProduct ap) {
        boolean expired  = ap.isExpired();
        int uid          = Marketplace.getInstance().getCurrentUser().getId();
        boolean isWinner = ap.isWinner(uid);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ThemeManager.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        expired ? ThemeManager.BORDER : ThemeManager.ACCENT, 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Title row
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        JLabel brandLabel = new JLabel(ap.getBrand());
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        brandLabel.setForeground(ThemeManager.TEXT_PRIMARY);

        JLabel nameLabel = new JLabel(ap.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(ThemeManager.TEXT_SECONDARY);

        namePanel.add(brandLabel);
        namePanel.add(nameLabel);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        badges.setOpaque(false);

        JLabel catBadge = new JLabel(ap.getCategory());
        catBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catBadge.setForeground(ThemeManager.getCategoryTextColor(ap.getCategory()));
        catBadge.setOpaque(true);
        catBadge.setBackground(ThemeManager.getCategoryColor(ap.getCategory()));
        catBadge.setBorder(new EmptyBorder(3, 8, 3, 8));

        JLabel statusBadge = new JLabel(expired ? "ENDED" : "LIVE");
        statusBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statusBadge.setForeground(Color.WHITE);
        statusBadge.setOpaque(true);
        statusBadge.setBackground(expired ? ThemeManager.TEXT_MUTED : ThemeManager.DANGER);
        statusBadge.setBorder(new EmptyBorder(3, 8, 3, 8));

        badges.add(catBadge);
        badges.add(statusBadge);

        titleRow.add(namePanel, BorderLayout.WEST);
        titleRow.add(badges, BorderLayout.EAST);

        // Info grid
        JPanel grid = new JPanel(new GridLayout(3, 2, 10, 6));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(12, 0, 10, 0));

        grid.add(makeInfoBox("Starting price",
                "$" + String.format("%.2f", ap.getStartingPrice()),
                ThemeManager.ACCENT_LIGHT, ThemeManager.ACCENT));
        grid.add(makeInfoBox("Current bid",
                "$" + String.format("%.2f", ap.getCurrentBid()),
                ThemeManager.SUCCESS_LIGHT, ThemeManager.SUCCESS));
        grid.add(makeInfoBox("Highest bidder",
                ap.getHighestBidderName(),
                ThemeManager.WARNING_LIGHT, ThemeManager.WARNING));
        grid.add(makeInfoBox("Total bids",
                ap.getBidCount() + " bids",
                ThemeManager.SURFACE_1, ThemeManager.TEXT_SECONDARY));
        grid.add(makeInfoBox("Started",
                ap.getStartTime().format(FMT),
                ThemeManager.SURFACE_1, ThemeManager.TEXT_SECONDARY));
        grid.add(makeInfoBox("Ends",
                ap.getEndTime().format(FMT),
                expired ? ThemeManager.DANGER_LIGHT : ThemeManager.SUCCESS_LIGHT,
                expired ? ThemeManager.DANGER : ThemeManager.SUCCESS));

        // Time label
        JPanel timeRow = new JPanel(new BorderLayout());
        timeRow.setOpaque(false);
        timeRow.setBorder(new EmptyBorder(0, 0, 8, 0));

        String timeText;
        if (expired) {
            timeText = isWinner ? "Auction ended — You won!" : "Auction ended";
        } else {
            long mins  = ChronoUnit.MINUTES.between(LocalDateTime.now(), ap.getEndTime());
            long hours = mins / 60;
            long remM  = mins % 60;
            timeText   = hours > 0 ? "Time left: " + hours + "h " + remM + "m"
                                   : "Time left: " + remM + " minutes";
        }

        JLabel timeLabel = new JLabel(timeText);
        timeLabel.setFont(ThemeManager.FONT_SUB);
        timeLabel.setForeground(expired
                ? (isWinner ? ThemeManager.SUCCESS : ThemeManager.DANGER)
                : ThemeManager.WARNING);
        timeRow.add(timeLabel, BorderLayout.WEST);

        // Button row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);

        if (!expired) {
            RoundedButton bidBtn = new RoundedButton("Place bid",
                    ThemeManager.ACCENT, Color.WHITE);
            bidBtn.addActionListener(e -> showBidDialog(ap));
            btnRow.add(bidBtn);
        } else if (isWinner) {
            JLabel wonLabel = new JLabel("Congratulations! You won.");
            wonLabel.setFont(ThemeManager.FONT_SUB);
            wonLabel.setForeground(ThemeManager.SUCCESS);
            btnRow.add(wonLabel);
        }

        card.add(titleRow);
        card.add(grid);
        card.add(timeRow);
        card.add(btnRow);

        return card;
    }

    private JPanel makeInfoBox(String label, String value, Color bg, Color textColor) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(bg);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)));

        JLabel lbl = new JLabel(label);
        lbl.setFont(ThemeManager.FONT_SMALL);
        lbl.setForeground(ThemeManager.TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(ThemeManager.FONT_SUB);
        val.setForeground(textColor);

        box.add(lbl);
        box.add(Box.createVerticalStrut(2));
        box.add(val);
        return box;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ThemeManager.FONT_LABEL);
        l.setForeground(ThemeManager.TEXT_SECONDARY);
        return l;
    }

    private void showBidDialog(AuctionProduct ap) {
        String input = JOptionPane.showInputDialog(this,
                "Current bid: $" + String.format("%.2f", ap.getCurrentBid())
                + "\nMinimum: $" + String.format("%.2f", ap.getCurrentBid() + 0.01)
                + "\n\nEnter your bid:");
        if (input == null || input.isBlank()) return;
        try {
            double amount = Double.parseDouble(input.trim());
            Marketplace.getInstance().placeBid(ap, amount);
            JOptionPane.showMessageDialog(this,
                    "Bid of $" + String.format("%.2f", amount) + " placed!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new AuctionScreen().setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidBidException | AuctionExpiredException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Bid rejected", JOptionPane.WARNING_MESSAGE);
        }
    }
}