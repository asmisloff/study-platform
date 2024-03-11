package ru.asmisloff.studyplatform.validation;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {

    private static final NumberFormat ruFmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("ru"));
    private static final int MAX_PRECISION = 20;

    static {
        ruFmt.setGroupingUsed(false);
    }

    public static String tr(double n, int precision) {
        if (precision == 0) {
            return String.valueOf(Math.round(n));
        }
        ruFmt.setMaximumFractionDigits(precision);
        return ruFmt.format(n);
    }

    public static String tr(double n) {
        return tr(n, MAX_PRECISION);
    }
}
