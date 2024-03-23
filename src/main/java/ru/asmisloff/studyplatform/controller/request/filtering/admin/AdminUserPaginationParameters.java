package ru.asmisloff.studyplatform.controller.request.filtering.admin;

import ru.asmisloff.studyplatform.controller.request.PaginationParameters;

public class AdminUserPaginationParameters extends PaginationParameters {

    public AdminUserPaginationParameters(String page, String size, String sortDirection, String[] sortBy) {
        super(page, size, sortDirection, sortBy);
    }

    public static AdminUserPaginationParameters unpaged() {
        return new AdminUserPaginationParameters(null, null, null, null);
    }

    @Override
    protected String[] defaultSortBy() {
        return new String[]{ "id" };
    }

    @Override
    protected String[] allowableSortProperties() {
        return new String[]{ "id", "login", "firstName", "lastName" };
    }

    @Override
    protected String decorated(String sortParameter) {
        return sortParameter;
    }
}
