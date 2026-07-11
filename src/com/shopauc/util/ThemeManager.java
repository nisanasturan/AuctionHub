package com.shopauc.util;

import java.awt.*;

public class ThemeManager {

    // Ana renkler
    public static final Color ACCENT       = new Color(37, 99, 235);
    public static final Color ACCENT_HOVER = new Color(29, 78, 216);
    public static final Color ACCENT_LIGHT = new Color(219, 234, 254);

    public static final Color SUCCESS      = new Color(22, 163, 74);
    public static final Color SUCCESS_LIGHT= new Color(220, 252, 231);
    public static final Color DANGER       = new Color(220, 38, 38);
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);
    public static final Color WARNING      = new Color(217, 119, 6);
    public static final Color WARNING_LIGHT= new Color(254, 243, 199);

    // Yüzeyler
    public static final Color PAGE_BG     = new Color(245, 245, 244);
    public static final Color SURFACE     = Color.WHITE;
    public static final Color SURFACE_1   = new Color(250, 250, 249);

    // Metin
    public static final Color TEXT_PRIMARY   = new Color(28, 25, 23);
    public static final Color TEXT_SECONDARY = new Color(87, 83, 78);
    public static final Color TEXT_MUTED     = new Color(168, 162, 158);

    // Border
    public static final Color BORDER       = new Color(231, 229, 228);
    public static final Color BORDER_STRONG= new Color(214, 211, 209);

    // Kategoriler için renkler
    public static final Color CAT_TECH    = new Color(219, 234, 254);
    public static final Color CAT_HOME    = new Color(220, 252, 231);
    public static final Color CAT_COSM    = new Color(253, 232, 243);
    public static final Color CAT_FASH    = new Color(254, 243, 199);
    public static final Color CAT_SPORT   = new Color(237, 233, 254);
    public static final Color CAT_OTHER   = new Color(245, 245, 244);

    // Fontlar
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_SUB     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 11);

    public static Color getCategoryColor(String category) {
        if (category == null) return CAT_OTHER;
        switch (category) {
            case "Technology":   return CAT_TECH;
            case "Home & Living":return CAT_HOME;
            case "Cosmetics":    return CAT_COSM;
            case "Fashion":      return CAT_FASH;
            case "Sports":       return CAT_SPORT;
            default:             return CAT_OTHER;
        }
    }

    public static Color getCategoryTextColor(String category) {
        if (category == null) return TEXT_SECONDARY;
        switch (category) {
            case "Technology":   return new Color(29, 78, 216);
            case "Home & Living":return new Color(21, 128, 61);
            case "Cosmetics":    return new Color(157, 23, 77);
            case "Fashion":      return new Color(146, 64, 14);
            case "Sports":       return new Color(109, 40, 217);
            default:             return TEXT_SECONDARY;
        }
    }

    public static final String[] CATEGORIES = {
        "Technology", "Home & Living", "Cosmetics", "Fashion", "Sports", "General"
    };
}