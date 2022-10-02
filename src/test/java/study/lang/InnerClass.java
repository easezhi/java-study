package study.lang;

import java.util.function.Function;

public class InnerClass {
    private String getPrivateMsg() {
        return "InnerClass private";
    }

    String getPackageMsg() {
        return "InnerClass Package";
    }

    public String getPublicMsg() {
        return "InnerClass public";
    }

    public Function<InnerClass, String> getPrivateMapper() {
        return InnerClass::getPrivateMsg;
    }

    public Function<InnerClass, String> getLambdaMapper() {
        return (obj) -> obj.getPrivateMsg();
    }
}
