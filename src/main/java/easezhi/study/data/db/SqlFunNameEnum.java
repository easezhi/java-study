package easezhi.study.data.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SqlFunNameEnum {
    CURRENT_TIMESTAMP("CURRENT_TIMESTAMP"),
    ;

    private final String value;
}
