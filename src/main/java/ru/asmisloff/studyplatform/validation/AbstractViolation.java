package ru.asmisloff.studyplatform.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

@Getter
@Setter
public abstract class AbstractViolation {

    protected String name;
    protected List<String> what;
    protected List<AbstractViolation> nested;

    public void addError(String msg) {
        what = requireNonNullElse(what, new ArrayList<>());
        if (!what.contains(msg)) what.add(msg);
    }

    public AbstractViolation wrap(AbstractViolation v) {
        nested = requireNonNullElse(nested, new ArrayList<>());
        if (!isNullOrEmpty(v) && !nested.contains(v)) nested.add(v);
        return this;
    }

    public static boolean isNullOrEmpty(AbstractViolation v) {
        return v == null || isNullOrEmpty(v.what) && isNullOrEmpty(v.nested);
    }

    public void throwIfNotEmpty() {
        if (!isNullOrEmpty(what) || !isNullOrEmpty(nested)) {
            throw new IllegalStateException();
        }
    }

    @JsonProperty("title")
    public abstract String getTitle();

    private static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
