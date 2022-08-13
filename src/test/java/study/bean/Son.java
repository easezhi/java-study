package study.bean;

public class Son extends Father {
    public String name = "子";
    public int age = 23;

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
