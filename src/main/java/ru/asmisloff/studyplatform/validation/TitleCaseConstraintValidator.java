package ru.asmisloff.studyplatform.validation;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

import static java.lang.Character.*;
import static ru.asmisloff.studyplatform.validation.Util.*;

public class TitleCaseConstraintValidator implements ConstraintValidator<TitleCase, String> {

    public TitleCaseConstraintValidator() {
    }

    @Override
    public void initialize(TitleCase constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}


class Util {

    static boolean isRussianLetter(char c) {
        return UnicodeBlock.of(c).equals(UnicodeBlock.CYRILLIC);
    }

    static boolean isRussianUppercase(char c) {
        return isRussianLetter(c) && isUpperCase(c);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isRussianLowercase(char c) {
        return isRussianLetter(c) && isLowerCase(c);
    }

    static boolean isEnglishLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isEnglishUppercase(char c) {
        return isEnglishLetter(c) && isUpperCase(c);
    }

    static boolean isEnglishLowercase(char c) {
        return isEnglishLetter(c) && isLowerCase(c);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isPunctuation(char c) {
        return StringUtils.contains("\"',:", c);
    }
}

class CommonRules {

    /** Слова в заголовках разделяются одним пробелом */
    @Nullable
    String checkDuplicatedSpaces(@NotNull String[] words) {
        if (words.length > 1) {
            int cnt = 0;
            for (int i = 1; i < words.length - 1; ++i) {
                if (words[i].isEmpty()) {
                    ++cnt;
                }
            }
            if (cnt > 0) {
                return "Двойные пробелы (%d)".formatted(cnt);
            }
        }
        return null;
    }

    @Nullable
    String checkLeadingSpace(@NotNull String[] words) {
        if (words.length != 0 && words[0].isEmpty()) {
            return "Заголовок не может начинаться с пробела";
        }
        return null;
    }

    @Nullable
    String checkTrailingSpace(@NotNull String[] words) {
        if (words.length != 0 && words[words.length - 1].isEmpty()) {
            return "Заголовок не может заканчиваться пробелом";
        }
        return null;
    }
}


class AnyLanguageRules {

    @Nullable
    String checkMixingOfLanguages(@NotNull String[] words) {
        int enCnt = 0;
        int ruCnt = 0;
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (isEnglishLetter(c)) ++enCnt;
                if (isRussianLetter(c)) ++ruCnt;
            }
        }
        if (enCnt > 0 && ruCnt > 0) {
            return "Смешение русских (%d) и английских символов в строке является недопустимым (%d)"
                .formatted(ruCnt, enCnt);
        }
        return null;
    }

    @Nullable
    String checkAllCharacters(@NotNull String[] words) {
        int cnt = 0;
        for (var word : words) {
            for (int i = 0; i < words.length; i++) {
                char c = word.charAt(i);
                if (!isRussianLetter(c) && !isEnglishLetter(c) && !isPunctuation(c)) {
                    ++cnt;
                }
            }
        }
        if (cnt > 0) {
            return "Запрещенные символы (%d)".formatted(cnt);
        }
        return null;
    }
}

class RussianRules {

    String checkFirstCharacterOfFirstWord(String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                if (!isRussianUppercase(word.charAt(0))) {
                    return "Первое слово должно быть написано с большой русской буквы";
                }
                return null;
            }
        }
        return null;
    }

    String checkFirstCharactersOfAllWordsExceptFirst(String[] words) {
        int wordCnt = 0;
        int cnt = 0;
        for (String word : words) {
            if (!word.isEmpty() && ++wordCnt > 1 && !isRussianLowercase(word.charAt(0))) ++cnt;
        }
        return cnt > 0
            ? "Все слова кроме первого должны быть написаны с маленькой русской буквы (%d)".formatted(cnt)
            : null;
    }

    String checkAllCharactersExceptFirstLetters(String[] words) {
        int cnt = 0;
        for (String word : words) {
            for (int i = 1; i < word.length(); ++i) {
                char c = word.charAt(i);
                if (!isRussianLowercase(c) && !isPunctuation(c)) ++cnt;
            }
        }
        return cnt > 0
            ? "Слова могут содержать только маленькие русские буквы и знаки препинания (%d)".formatted(cnt)
            : null;
    }
}

class EnglishRules {

    public EnglishRules() {
        serviceWords = new String[]{ "a", "an", "but", "for", "not", "the", "or" };
        Arrays.sort(serviceWords);
    }

    String checkFirstCharacterOfFirstWord(String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                if (!isEnglishUppercase(word.charAt(0))) {
                    return "Первое слово должно быть написано с большой английской буквы";
                }
                return null;
            }
        }
        return null;
    }

    String checkFirstCharacterOfLastWord(String[] words) {
        for (int i = words.length - 1; i >= 0; i--) {
            String word = words[i];
            if (!word.isEmpty()) {
                if (!isEnglishUppercase(word.charAt(0))) {
                    return "Последнее слово должно быть написано с большой английской буквы";
                }
                return null;
            }
        }
        return null;
    }

    String checkAllServiceWordsAreLowercase(String[] words) {
        int cnt = 0;
        for (int i = begin(words) + 1; i < end(words) - 1; i++) {
            String word = words[i];
            if (isServiceWord(word) && !isLowerCase(word.charAt(0))) {
                ++cnt;
            }
        }
        return cnt > 0
            ? "Служебные слова должны начинаться с маленькой буквы (%d)".formatted(cnt)
            : null;
    }

    String checkAllNonServiceWordsAreUppercase(String[] words) {
        int cnt = 0;
        for (int i = begin(words) + 1; i < end(words) - 1; i++) {
            String word = words[i];
            if (!isServiceWord(word) && !isEnglishUppercase(word.charAt(0))) {
                ++cnt;
            }
        }
        return cnt > 0
            ? "Все слова должны начинаться с большой английской буквы (%d)".formatted(cnt)
            : null;
    }

    String checkAllCharacters(String[] words) {
        int cnt = 0;
        for (String word : words) {
            for (int i = 1; i < word.length(); ++i) {
                char c = word.charAt(i);
                if (!isEnglishLowercase(c) && !isPunctuation(c)) ++cnt;
            }
        }
        return cnt > 0
            ? "Слова могут содержать только маленькие английские буквы и знаки препинания (%d)".formatted(cnt)
            : null;
    }

    private int begin(String[] words) {
        int begin = 0;
        while (begin < words.length && words[begin].isEmpty()) ++begin;
        return begin;
    }

    private int end(String[] words) {
        int end = words.length - 1;
        while (end >= 0 && words[end].isEmpty()) --end;
        return end + 1;
    }

    private boolean isServiceWord(String word) {
        return Arrays.binarySearch(serviceWords, word, StringUtils::compareIgnoreCase) >= 0;
    }

    private final String[] serviceWords;
}