package com.threeknowbigdata.flink.datastream.entity;

/**
 * 类描述：
 *
 * @ClassName Teacher
 * @Description:
 * @Author: 土哥
 * @Date: 2021/9/27 下午2:05
 */
public class Teacher {

    private String id;
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Teacher(String id, int age) {
        this.id = id;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", age=" + age +
                '}';
    }
}
