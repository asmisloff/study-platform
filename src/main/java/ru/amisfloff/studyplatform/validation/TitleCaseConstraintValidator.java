package ru.amisfloff.studyplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static java.lang.Character.isUpperCase;

public class TitleCaseConstraintValidator implements ConstraintValidator<TitleCase, String> {

    @Override
    public void initialize(TitleCase constraintAnnotation) {
        lang = constraintAnnotation.lang();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (isEmpty(value, context)) {
            return false;
        }

        String[] words = StringUtils.split(value, ' ');
        boolean result = isNotSpace(words[0], context);
        result = isNotSpace(words[words.length - 1], context) && result;
        for (int i = 0; i < words.length; ++i) {
            String w = words[i];
            result = isNotSpace(w, context) && result;
            result = checkFirstLetter(words, i, context) && result;
            result = checkSymbols(w, context) && result;
        }
        return result;
    }

    private boolean isEmpty(String value, ConstraintValidatorContext context) {
        if (value.isEmpty()) {
            addError(context, "Пустая строка");
            return true;
        }
        return false;
    }

    private boolean isNotSpace(String word, ConstraintValidatorContext context) {
        if (word.isBlank()) {
            addError(context, "Не допускаютя двойные пробелы");
            return false;
        }
        return true;
    }

    private boolean checkFirstLetter(String[] words, int i, ConstraintValidatorContext context) {
        String w = words[i];
        boolean itIsUppercase = isUpperCase(w.charAt(0));
        boolean result = true;
        switch (lang) {
            case En -> {
                boolean specialWord = isSpecialWord(w);
                if (!itIsUppercase) {
                    if (i == 0 || i == words.length - 1) {
                        result = false;
                        addError(context, "Первое и последнее слово в заголовке должны быть написаны с большой буквы.");
                    } else {
                        if (!specialWord) {
                            result = false;
                            addError(
                                context,
                                """
                                    Все остальные слова также должны быть написаны с большой буквы, если они не относятся
                                    к предлогам или союзам: a, but, for, or, not, the, an""");
                        }
                    }
                } else if (specialWord) {
                    result = false;
                    addError(context, "Предлоги и союзы пишутся с маленькой буквы");
                }
            }
            case Ru -> {
                if (i == 0 && !itIsUppercase) {
                    result = false;
                    addError(context, "Первое слово должно быть написано с большой буквы");
                } else if (i > 0 && itIsUppercase) {
                    result = false;
                    addError(context, "Все слова кроме первого должны быть написаны с маленькой буквы");
                }
            }
            case Any -> {
            }
        }
        return result;
    }

    private boolean checkSymbols(String word, ConstraintValidatorContext context) {
        boolean en = false;
        boolean ru = false;
        boolean illegalChar = false;
        boolean result = true;
        String allowedSigns = "\"',: ";
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
                en = true;
            } else if (c >= 'А' && c <= 'Я' || c >= 'а' && c <= 'я') {
                ru = true;
            } else if (!StringUtils.contains(allowedSigns, c)) {
                illegalChar = true;
            }
            if (en && ru && illegalChar) {
                break;
            }
        }
        if (ru && en) {
            result = false;
            addError(context, "Смешение русских и английских символов в строке является недопустимым");
        }
        if (illegalChar) {
            result = false;
            addError(context, "Недопустимые символы");
        }
        return result;
    }

    private boolean isSpecialWord(String w) {
        for (String sw : specialWords) {
            if (StringUtils.equalsIgnoreCase(sw, w)) {
                return true;
            }
        }
        return false;
    }

    private void addError(ConstraintValidatorContext context, String s) {
        context
            .buildConstraintViolationWithTemplate(s)
            .addConstraintViolation();
    }

    private TitleCase.Lang lang;
    private final String[] specialWords = new String[]{ "a", "but", "for", "or", "not", "the", "an" };
}
