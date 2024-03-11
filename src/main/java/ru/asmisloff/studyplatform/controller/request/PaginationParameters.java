package ru.asmisloff.studyplatform.controller.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.asmisloff.studyplatform.validation.AbstractViolation;
import ru.asmisloff.studyplatform.validation.Constraints;
import ru.asmisloff.studyplatform.validation.StringViolation;

import static java.util.Objects.requireNonNullElse;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

@Getter
public abstract class PaginationParameters {

    private final String page;
    private final String size;
    private final String sortDirection;
    private final String[] sortBy;

    public PaginationParameters(
        String page,
        String size,
        String sortDirection,
        String[] sortBy
    ) {
        this.page = requireNonNullElse(page, "0");
        this.size = requireNonNullElse(size, String.valueOf(Integer.MAX_VALUE));
        this.sortDirection = requireNonNullElse(sortDirection, "ASC");
        this.sortBy = requireNonNullElse(sortBy, defaultSortBy());
    }

    public AbstractViolation validate() {
        var v = useConstraints(this, "Параметры пагинации", true);
        for (String param : sortBy) {
            String prop = StringUtils.substringBefore(param, ':');
            String direction = StringUtils.substringAfter(param, ':');
            boolean asc = StringUtils.compareIgnoreCase(direction, "asc") == 0;
            boolean desc = StringUtils.compareIgnoreCase(direction, "desc") == 0;
            StringViolation paramViolation = useConstraints(param, "sortBy", true, 0, Integer.MAX_VALUE);
            paramViolation.addRule("Неизвестное поле", Constraints.contains(allowableSortProperties(), prop));
            paramViolation.addRule("Неизвестное направление сортировки", asc || desc || direction.isEmpty());
            v.wrap(paramViolation);
        }
        return v;
    }

    public Pageable pageable() {
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
            orders[i] = new Sort.Order(direction, decorated(field));
        }
        int pageIndex = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        return PageRequest.of(pageIndex, pageSize, Sort.by(orders));
    }

    protected abstract String[] defaultSortBy();

    protected abstract String[] allowableSortProperties();

    protected abstract String decorated(String sortParameter);
}
