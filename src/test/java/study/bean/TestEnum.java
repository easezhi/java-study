package study.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestEnum {
    CREATE("create", "创建"),
    UPDATE("update", "变更"),
    REMOVE("remove", "删除"),
    ;

    private final String value;
    private final String description;
}
