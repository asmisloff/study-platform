package ru.asmisloff.studyplatform.validation;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.constraints.Null;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TitleCaseConstraintValidatorTest {

    @ParameterizedTest
    @CsvSource(
        delimiter = ':',
        value = { "Евгений Онегин:", "Евгений  Онегин:1", "Евгений   Онегин:2", "{}Евгений Онегин{}:", "{}{}Евгений  Онегин{}{}:3", ":", "{}{}:1" }
    )
    void testCheckDuplicatedSpaces(@Nullable String title, @Nullable String expected) {
        String[] words = exploded(title);
        String actual = StringUtils.substringBetween(
            commonRules.checkDuplicatedSpaces(words), "(", ")"
        );
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}* | Заголовок не может начинаться с пробела",
            "* | "
        }
    )
    void testCheckLeadingSpace(@NotNull String title, @Nullable String expected) {
        String actual = commonRules.checkLeadingSpace(exploded(title));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "*{} | Заголовок не может заканчиваться пробелом",
            "* | "
        }
    )
    void testCheckTrailingSpace(@NotNull String title, @Nullable String expected) {
        assertEquals(expected, commonRules.checkTrailingSpace(exploded(title)));
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = ':',
        value = {
            "asd:",
            "фыв:",
            "asd фыв:Смешение русских (3) и английских символов в строке является недопустимым (3)",
            "This is a wonderful день:Смешение русских (4) и английских символов в строке является недопустимым (16)"
        }
    )
    void testCheckMixingOfLanguages(@NotNull String title, @Nullable String expected) {
        String[] words = exploded(title);
        assertEquals(expected, anyLanguageRules.checkMixingOfLanguages(words));
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}Война и мир | ",
            "война и мир | Первое слово должно быть написано с большой русской буквы",
            "{}{}война и мир | Первое слово должно быть написано с большой русской буквы",
            "{}{}vойна и мир | Первое слово должно быть написано с большой русской буквы"
        }
    )
    void testFirstLetterInRussianTitle(String title, String expected) {
        String[] words = exploded(title);
        assertEquals(expected, russianRules.checkFirstCharacterOfFirstWord(words));
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}Война и мир | ",
            "воЙна и мИр | ",
            "{}{}война И Мир | Все слова кроме первого должны быть написаны с маленькой русской буквы (2)"
        }
    )
    void testFirstCharactersInWordsOfRussianTitle(String title, String expected) {
        String[] words = exploded(title);
        assertEquals(expected, russianRules.checkFirstCharactersOfAllWordsExceptFirst(words));
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}война и мир | ",
            "воЙна и Мир | Слова могут содержать только маленькие русские буквы и знаки препинания (1)",
            "{}{}воЙнА и мiр: | Слова могут содержать только маленькие русские буквы и знаки препинания (3)"
        }
    )
    void testAllCharactersExceptFirstLettersInRussianTitle(String title, String expected) {
        String[] words = exploded(title);
        assertEquals(expected, russianRules.checkAllCharactersExceptFirstLetters(words));
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}War and peace | ",
            "war and peace | Первое слово должно быть написано с большой английской буквы",
            "Вar and peace | Первое слово должно быть написано с большой английской буквы"
        }
    )
    void testFirstLetterInEnglishTitle(@Nullable String title, @Null String expected) {
        String[] words = exploded(title);
        String actual = englishRules.checkFirstCharacterOfFirstWord(words);
        assertEquals(expected, actual);
    }

    @NotNull
    private String[] exploded(@Nullable String title) {
        title = Objects.isNull(title) ? "" : StringUtils.replace(title, "{}", " ");
        return title.split("\s", -1);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}War and Peace | ",
            "war and peace | Последнее слово должно быть написано с большой английской буквы",
            "Вar and Пeace | Последнее слово должно быть написано с большой английской буквы",
            "Вar and for | Последнее слово должно быть написано с большой английской буквы"
        }
    )
    void testFirstCharacterOfLastWordInEnglishTitle(@Nullable String title, @Null String expected) {
        String[] words = exploded(title);
        String actual = englishRules.checkFirstCharacterOfLastWord(words);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}War and Peace | ",
            "The war And Peace For me, please | Служебные слова должны начинаться с маленькой буквы (1)",
            "War And peace For miss is not A cheese | Служебные слова должны начинаться с маленькой буквы (2)"
        }
    )
    void testCheckAllServiceWordsAreLowercase(@Nullable String title, @Nullable String expected) {
        String[] words = exploded(title);
        String actual = englishRules.checkAllServiceWordsAreLowercase(words);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}War And Peace | ",
            "The War And Peace For me, please | Все слова должны начинаться с большой английской буквы (1)",
            "War And peace For miss is not A cheese | Все слова должны начинаться с большой английской буквы (3)"
        }
    )
    void testCheckAllNonServiceWordsAreUppercaseInEnglishTitle(@Nullable String title, @Nullable String expected) {
        String[] words = exploded(title);
        String actual = englishRules.checkAllNonServiceWordsAreUppercase(words);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        value = {
            "{}{} | ",
            "{}{}War And Peace | ",
            "The War And PeacE For me, please | Слова могут содержать только маленькие английские буквы и знаки препинания (1)",
            "War And peace FoR m!ss is not A Чиzz | Слова могут содержать только маленькие английские буквы и знаки препинания (3)"
        }
    )
    void testCheckAllCharactersInEnglishTitle(@Nullable String title, @Nullable String expected) {
        String[] words = exploded(title);
        String actual = englishRules.checkAllCharacters(words);
        assertEquals(expected, actual);
    }

    private final TitleCaseConstraintValidator validator = new TitleCaseConstraintValidator();
    private final CommonRules commonRules = new CommonRules();
    private final AnyLanguageRules anyLanguageRules = new AnyLanguageRules();
    private final RussianRules russianRules = new RussianRules();
    private final EnglishRules englishRules = new EnglishRules();
}
