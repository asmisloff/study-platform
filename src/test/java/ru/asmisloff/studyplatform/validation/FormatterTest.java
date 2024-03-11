package ru.asmisloff.studyplatform.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.asmisloff.studyplatform.validation.Formatter.tr;

class FormatterTest {

    @Test
    void testTr() {
        assertEquals("0,11", tr(0.11111, 2));
    }
}