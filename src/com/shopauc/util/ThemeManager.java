package com.shopauc.util;

import java.awt.*;

public class ThemeManager {

    // ── Ana Renkler ─────────────────────────────────────────
    public static final Color PURPLE      = new Color(108, 61, 232);
    public static final Color PINK        = new Color(224, 64, 160);
    public static final Color ORANGE      = new Color(255, 107, 53);

    public static final Color BG_LIGHT    = new Color(244, 244, 248);
    public static final Color WHITE       = Color.WHITE;
    public static final Color TEXT_DARK   = new Color(26, 26, 46);
    public static final Color TEXT_GRAY   = new Color(136, 136, 136);
    public static final Color TEXT_LIGHT  = new Color(190, 190, 200);

    public static final Color CARD_BG     = Color.WHITE;
    public static final Color PURPLE_SOFT = new Color(240, 234, 254);
    public static final Color PINK_SOFT   = new Color(253, 234, 245);
    public static final Color AMBER_SOFT  = new Color(254, 243, 226);
    public static final Color TEAL_SOFT   = new Color(226, 246, 241);
    public static final Color TEAL        = new Color(29, 158, 117);

    // ── Fontlar ─────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SUB     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 11);

    // ── Gradient Yardımcısı ─────────────────────────────────
    public static GradientPaint getPrimaryGradient(int width, int height) {
        return new GradientPaint(0, 0, PURPLE, width, height, PINK);
    }

    public static GradientPaint getWarmGradient(int width, int height) {
        return new GradientPaint(0, 0, PINK, width, height, ORANGE);
    }
}