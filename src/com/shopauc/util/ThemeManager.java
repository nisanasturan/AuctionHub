package com.shopauc.util;

import java.awt.*;

public class ThemeManager {

    public static final Color ACCENT       = new Color(99, 102, 241);
    public static final Color ACCENT_HOVER = new Color(79, 82, 221);
    public static final Color ACCENT_LIGHT = new Color(49, 52, 91);

    public static final Color SUCCESS       = new Color(34, 197, 94);
    public static final Color SUCCESS_LIGHT = new Color(20, 60, 35);
    public static final Color DANGER        = new Color(239, 68, 68);
    public static final Color DANGER_LIGHT  = new Color(70, 25, 25);
    public static final Color WARNING       = new Color(234, 179, 8);
    public static final Color WARNING_LIGHT = new Color(70, 55, 10);

    public static final Color PAGE_BG    = new Color(10, 10, 15);
    public static final Color SURFACE    = new Color(18, 18, 28);
    public static final Color SURFACE_1  = new Color(26, 26, 40);

    public static final Color TEXT_PRIMARY   = new Color(237, 237, 243);
    public static final Color TEXT_SECONDARY = new Color(160, 160, 180);
    public static final Color TEXT_MUTED     = new Color(90, 90, 110);

    public static final Color BORDER        = new Color(40, 40, 60);
    public static final Color BORDER_STRONG = new Color(60, 60, 85);

    public static final Color CAT_TECH  = new Color(30, 40, 80);
    public static final Color CAT_HOME  = new Color(20, 55, 35);
    public static final Color CAT_COSM  = new Color(65, 20, 50);
    public static final Color CAT_FASH  = new Color(65, 45, 10);
    public static final Color CAT_SPORT = new Color(45, 20, 75);
    public static final Color CAT_OTHER = new Color(30, 30, 45);

    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_SUB     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 11);

    public static final String[] CATEGORIES = {
        "Clothing", "Electronics", "Home & Living", "Personal Care", "Sports & Outdoor"
    };

    public static Color getCategoryColor(String category) {
        if (category == null) return CAT_OTHER;
        switch (category) {
            case "Electronics":      return CAT_TECH;
            case "Home & Living":    return CAT_HOME;
            case "Personal Care":    return CAT_COSM;
            case "Clothing":         return CAT_FASH;
            case "Sports & Outdoor": return CAT_SPORT;
            default:                 return CAT_OTHER;
        }
    }

    public static Color getCategoryTextColor(String category) {
        if (category == null) return TEXT_SECONDARY;
        switch (category) {
            case "Electronics":      return new Color(130, 160, 255);
            case "Home & Living":    return new Color(80, 200, 120);
            case "Personal Care":    return new Color(220, 100, 180);
            case "Clothing":         return new Color(220, 170, 60);
            case "Sports & Outdoor": return new Color(170, 110, 255);
            default:                 return TEXT_SECONDARY;
        }
    }

    public static String getCategoryIcon(String category) {
        if (category == null) return "?";
        switch (category) {
            case "Electronics":      return "💻";
            case "Home & Living":    return "🏠";
            case "Personal Care":    return "✨";
            case "Clothing":         return "👗";
            case "Sports & Outdoor": return "⚽";
            default:                 return "📦";
        }
    }
}