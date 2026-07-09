package com.shopauc.util;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class RoundedButton extends JButton {

    private Color colorStart;
    private Color colorEnd;
    private int radius;

    public RoundedButton(String text, Color colorStart, Color colorEnd) {
        super(text);
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.radius = 12;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(ThemeManager.FONT_SUB);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius * 2, radius * 2));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width + 24, d.height + 10);
    }
}