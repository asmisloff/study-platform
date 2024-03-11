package ru.asmisloff.studyplatform.validation;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleViolationTest {

    @Test
    void getTitle() {
        var v = new DoubleViolation("Параметр", 0.33);
        assertEquals("Параметр = 0.33", v.getTitle());
    }

    @Test
    void defConstraints() {
        var v = DoubleViolation.defConstraints("Параметр", 0.33, 1.0, 2.0, 3, null);
        assertHasError(v, "Значение должно находиться в диапазоне 1...2", true);
    }

    private void assertHasError(AbstractViolation v, String s, boolean topLevel) {
        if (v.what != null) {
            for (String msg : v.what) {
                if (msg.equals(s)) {
                    return;
                }
            }
        }
        if (v.nested != null) {
            for (AbstractViolation nested : v.nested) {
                assertHasError(nested, s, false);
            }
        }
        if (topLevel) {
            throw new AssertionFailedError();
        }
    }

    @Test
    void addRule() {
    }

    @Test
    void addRuleWithPredicate() {
    }

    @Test
    void addProhibition() {
    }

    @Test
    void addProhibitionWithPredicate() {
    }
}