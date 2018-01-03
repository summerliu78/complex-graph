package com.yinker.tinyv.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/6/22.
 */
public class Person {
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pid", "123");
        map.put("name", "张三");
        map.put("age", 23);
        map.put("xxx", "XXX");

        // 通过map的数据来创建Person类型的JavaBean对象
        Person p = CommonUtils.toBean(map, Person.class);
        System.out.println(p);
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;
    private String pid;
    private int age;
}
