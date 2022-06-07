package easezhi.study.excel;

import easezhi.study.excel.FieldSpec;

public class ExcelColumnError {
    public enum ErrorType {
        REQUIRE("必输项不能为空") {
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "为必输项";
            }
        },
        MAX_LENGTH("字符个数超长") {
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "最多输入" + fieldSpec.max + "个字符";
            }
        },
        NUMBER_FORMAT("数值格式有误") {
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "输入的数字格式有误";
            }
        },
        INTEGER_PRECISION("整数超位数限制") {
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "最多输入" + fieldSpec.precision + "位整数";
            }
        },
        FLOAT_PRECISION("小数超精确度限制"){
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "最多输入" + (fieldSpec.precision - fieldSpec.scale) + "位整数" + fieldSpec.scale
                    + "位小数";
            }
        },
        DATE_FORMAT("日期解析有误") {
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "输入的日期有误";
            }
        },
        DATE_FORMAT_STRING("日期格式有误") { // 字符串格式的日期
            public String getMsg(FieldSpec fieldSpec) {
                return fieldSpec.title + "需要输入" + fieldSpec.format + "格式的日期";
            }
        }
        ;

        ErrorType(String desc) {
            this.desc = desc;
        }

        final private String desc;
        public String getDesc() {
            return this.desc;
        }

        public String getMsg(FieldSpec fieldSpec) {
            return getDesc();
        }
    }

    public final ErrorType errorType;
    public final String fieldName;
    public final String title;

    FieldSpec fieldSpec;

    public ExcelColumnError(ErrorType errorType, FieldSpec fieldSpec) {
        this.errorType = errorType;
        this.fieldName = fieldSpec.fieldName;
        this.title = fieldSpec.title;
        this.fieldSpec = fieldSpec;
    }

    public String getDefaultErrorMsg() {
        return errorType.getMsg(fieldSpec);
    }
}
