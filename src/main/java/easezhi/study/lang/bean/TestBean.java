package easezhi.study.lang.bean;

import easezhi.study.lang.bean.mapper.*;
import easezhi.study.resource.pojo.*;

public class TestBean {
    public static void main(String[] args) {
        Person per = new Person().setName("ease").setAge(22).setGender("男");
        var perDto = PersonMapper.INSTANCE.toDto(per);
        System.out.println(per);
        System.out.println(perDto);
//        var obj = new MyBean();
//        obj.setName("bean");
//        System.out.println(obj);
    }
}
