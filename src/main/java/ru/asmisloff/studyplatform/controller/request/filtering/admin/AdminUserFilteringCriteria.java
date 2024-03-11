package ru.asmisloff.studyplatform.controller.request.filtering.admin;

import ru.asmisloff.studyplatform.controller.request.parameter.DbIdFilteringCriteria;
import ru.asmisloff.studyplatform.controller.request.parameter.StringFilteringCriteria;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

import static java.util.Objects.requireNonNullElse;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

public class AdminUserFilteringCriteria {

    private final DbIdFilteringCriteria id;
    private final StringFilteringCriteria login;
    private final StringFilteringCriteria firstName;
    private final StringFilteringCriteria lastName;

    public AdminUserFilteringCriteria(
        DbIdFilteringCriteria id,
        StringFilteringCriteria login,
        StringFilteringCriteria firstName,
        StringFilteringCriteria lastName
    ) {
        this.id = requireNonNullElse(id, DbIdFilteringCriteria.UNDEFINED);
        this.login = requireNonNullElse(login, StringFilteringCriteria.UNDEFINED);
        this.firstName = requireNonNullElse(firstName, StringFilteringCriteria.UNDEFINED);
        this.lastName = requireNonNullElse(lastName, StringFilteringCriteria.UNDEFINED);
    }


    public AbstractViolation validate() {
        return useConstraints(this, "Параметры фильтрации", true)
            .wrap(id.validate("id", false, true));
    }
}
