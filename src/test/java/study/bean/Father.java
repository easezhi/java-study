package study.bean;

public class Father implements Who {
    public String name = "父";
    public String surname;
    public int age = 50;

    public String covered = "Father covered"; // 会被子类覆盖
    public String getCovered() {
        return covered;
    }
    public String getCoveredByFather() {
        return covered;
    }

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
