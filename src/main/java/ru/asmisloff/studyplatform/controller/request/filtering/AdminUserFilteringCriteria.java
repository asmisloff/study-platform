package ru.asmisloff.studyplatform.controller.request.filtering;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.asmisloff.studyplatform.controller.request.PagingAndSearchParameters;
import ru.asmisloff.studyplatform.controller.request.parameter.DatabaseID;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

import static java.util.Objects.requireNonNullElse;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

@Getter
public class AdminUserFilteringCriteria {

    private final DatabaseID id;
    private final String login;
    private final String firstName;
    private final String lastName;

    public AdminUserFilteringCriteria(
        @Nullable DatabaseID id,
        @Nullable String login,
        @Nullable String firstName,
        @Nullable String lastName
    ) {
        this.id = requireNonNullElse(id, DatabaseID.UNDEFINED);
        this.login = StringUtils.trim(login);
        this.firstName = StringUtils.trim(firstName);
        this.lastName = StringUtils.trim(lastName);
    }

    public AbstractViolation validate(PagingAndSearchParameters pagingAndSearchParameters) {
        return useConstraints(this, "Параметры запроса списка пользователей", true)
            .wrap(id.validate("id", false, true))
            .wrap(useConstraints(pagingAndSearchParameters, allowedSortProperties()));
    }

    protected String[] allowedSortProperties() {
        return new String[]{ "id", "login", "firstName", "lastName" };
    }

    protected String sortFieldMapping(@NotNull String property) {
        return switch (property) {
            default -> property;
        };
    }

    public Pageable pageable(PagingAndSearchParameters p) {
        String[] sortBy = p.getSortBy();
        Sort.Order[] orders = new Sort.Order[sortBy.length];
        for (int i = 0; i < sortBy.length; ++i) {
            String param = sortBy[i];
            String field = StringUtils.substringBefore(param, ':');
            String directionMark = StringUtils.substringAfter(param, ':');
            Sort.Direction direction;
            if (StringUtils.equalsIgnoreCase(directionMark, "asc") || directionMark.isEmpty()) {
                direction = Sort.Direction.ASC;
            } else if (StringUtils.equalsIgnoreCase(directionMark, "desc")) {
                direction = Sort.Direction.DESC;
            } else {
                throw new IllegalArgumentException("Неизвестный маркер направления сортировки: " + directionMark);
            }
            orders[i] = new Sort.Order(direction, sortFieldMapping(field));
        }
        int page = Integer.parseInt(p.getPage());
        int size = Integer.parseInt(p.getSize());
        return PageRequest.of(page, size, Sort.by(orders));
    }
}
