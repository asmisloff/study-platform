package ru.asmisloff.studyplatform.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(long id) {
        super("Ресурс не найден: id = %d".formatted(id));
    }
}
