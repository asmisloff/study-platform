package ru.asmisloff.studyplatform.controller.request;

import lombok.Getter;

import static java.util.Objects.requireNonNullElse;

@Getter
public class PagingAndSearchParameters {

    private final String[] sortBy;
    private final String page;
    private final String size;
    private final String sortDirection;

    public PagingAndSearchParameters(
        String[] sortBy,
        String page,
        String size,
        String sortDirection
    ) {
        this.sortBy = requireNonNullElse(sortBy, new String[0]);
        this.page = requireNonNullElse(page, "0");
        this.size = requireNonNullElse(size, String.valueOf(Integer.MAX_VALUE));
        this.sortDirection = requireNonNullElse(sortDirection, "ASC");
    }
}
