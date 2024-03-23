package ru.asmisloff.studyplatform.controller.request.filtering.admin;

import lombok.Getter;
import ru.asmisloff.studyplatform.controller.request.filtering.FilteringCriteria;
import ru.asmisloff.studyplatform.controller.request.parameter.DbIdFilteringCriteria;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

import static java.util.Objects.requireNonNullElse;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

@Getter
public class AdminUserFilteringCriteria extends FilteringCriteria {

    private final DbIdFilteringCriteria id;

    public AdminUserFilteringCriteria(
        DbIdFilteringCriteria id,
        String searchText
    ) {
        super(searchText);
        this.id = requireNonNullElse(id, DbIdFilteringCriteria.UNDEFINED);
    }

    public static AdminUserFilteringCriteria undefined() {
        return new AdminUserFilteringCriteria(DbIdFilteringCriteria.UNDEFINED, "");
    }

    public AbstractViolation validate() {
        return useConstraints(this, "Параметры фильтрации", true)
            .wrap(id.validate("id", false, true));
    }
}
