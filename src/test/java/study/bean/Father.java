package study.bean;

public class Father {
    public String name = "父";
    public int age = 50;

    public String surname; // 子类没有重写

    public int howOld() {
        return age;
    }

    public String who() {
        return "Father";
    }

    public static void whoAmI() {
        System.out.println("I am Father");
    }
}
