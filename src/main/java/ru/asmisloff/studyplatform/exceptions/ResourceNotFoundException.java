package ru.asmisloff.studyplatform.exceptions;

import ru.asmisloff.studyplatform.entity.Resource;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Resource resource, long id) {
        super("Ресурс не найден: %s id = %d".formatted(resource.inRussian(), id));
    }
}
