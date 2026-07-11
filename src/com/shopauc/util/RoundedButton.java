package com.shopauc.util;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class RoundedButton extends JButton {

    private Color bg;
    private Color fg;
    private boolean outline;

    public RoundedButton(String text, Color bg, Color fg) {
        super(text);
        this.bg = bg;
        this.fg = fg;
        this.outline = false;
        init();
    }

    public RoundedButton(String text, boolean outline) {
        super(text);
        this.bg = ThemeManager.SURFACE;
        this.fg = ThemeManager.TEXT_PRIMARY;
        this.outline = outline;
        init();
    }

    private void init() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(fg);
        setFont(ThemeManager.FONT_SUB);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (outline) {
            g2.setColor(ThemeManager.BORDER_STRONG);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
            g2.setColor(ThemeManager.SURFACE);
            g2.fill(new RoundRectangle2D.Double(1, 1, getWidth()-2, getHeight()-2, 7, 7));
        } else {
            g2.setColor(getModel().isPressed() ? bg.darker() :
                       getModel().isRollover() ? bg.brighter() : bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
        }
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width + 20, d.height + 8);
    }
}