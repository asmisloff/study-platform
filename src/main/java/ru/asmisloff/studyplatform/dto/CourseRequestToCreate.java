package ru.asmisloff.studyplatform.dto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import ru.asmisloff.studyplatform.validation.AbstractViolation;
import ru.asmisloff.studyplatform.validation.StringViolation;

import static java.lang.Character.isLetter;
import static java.lang.Character.isUpperCase;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

public record CourseRequestToCreate(String title) {

    public CourseRequestToCreate(String title) {
        this.title = StringUtils.trim(title);
    }

    public AbstractViolation validate() {
        var v = useConstraints(this, "Создание курса", true);
        var titleViolation = useConstraints(title, "Наименование", true, 1, 50);
        addCustomRulesForTitle(titleViolation);
        return v.wrap(titleViolation);
    }

    private void addCustomRulesForTitle(StringViolation titleViolation) {
        @Nullable String[] titleWords = StringUtils.split(title);
        titleViolation
                .addRule("Первое слово должно начинаться с большой буквы", firstCharacterIsUppercase(title))
                .addRule("Все слова кроме первого должны начинаться с маленькой буквы",
                        allWordsExceptFirstStartFromLowercase(titleWords));

    }

    private boolean firstCharacterIsUppercase(@Nullable String s) {
        if (s != null) {
            var first = s.charAt(0);
            return isUpperCase(first) && isLetter(first);
        } else {
            return false;
        }
    }

    private boolean allWordsExceptFirstStartFromLowercase(@Nullable String[] words) {
        if (words == null) return false;
        for (int i = 1; i < words.length; ++i) {
            if (!firstCharacterIsUppercase(words[i])) {
                return false;
            }
        }
        return true;
    }
}
