package study.bean;

import lombok.Getter;
import lombok.Setter;

public class Son extends Father {
    private String sonPrivate;
    String sonPackage;

    public Son() {
        name = "子";
        age = 23;
    }

    public String covered; // 覆盖了基类的同名字段
    public String getCovered() {
        return covered;
    }


    public String who() {
        return "Son: " + name;
    }

    public static void whoAmI() {
        System.out.println("I am Son");
    }

    public int getAge() {
        return age;
    }

    private String privateMethod() {
        return "privateMethod";
    }

    String defaultMethod() {
        return "defaultMethod";
    }

    @Getter
    @Setter
    Son sibling;

    public Son genSibling() {
        var bro = new Son();
        bro.name = "弟";
        return bro;
    }
}
