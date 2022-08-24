package study.bean;

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
        return "Son";
    }

    public static void whoAmI() {
        System.out.println("I am Son");
    }

    public int getAge() {
        return age;
    }
}
