package ru.asmisloff.studyplatform.entity;

import org.apache.commons.lang3.StringUtils;

public enum RoleName {
    ROLE_OWNER, ROLE_ADMIN, ROLE_TEACHER, ROLE_STUDENT;

    public String withPrefix() {
        return this.name();
    }

    public String withoutPrefix() {
        return StringUtils.substringAfter(this.name(), "ROLE_");
    }
}
