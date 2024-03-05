package ru.asmisloff.studyplatform.entity;

public enum Resource {

    COURSE("курс"),
    LESSON("Урок"),
    USER("пользователь");

    private final String russianName;

    public String inRussian() {
        return russianName;
    }

    Resource(String russianName) {
        this.russianName = russianName;
    }
}
