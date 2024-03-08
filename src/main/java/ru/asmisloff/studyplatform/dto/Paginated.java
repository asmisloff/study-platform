package ru.asmisloff.studyplatform.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Paginated<T> {

    private final int page;
    private final int totalPages;
    private final List<T> content;

    public static <T> Paginated<T> from(Page<T> page) {
        return new Paginated<>(page.getNumber(), page.getTotalPages(), page.getContent());
    }
}
