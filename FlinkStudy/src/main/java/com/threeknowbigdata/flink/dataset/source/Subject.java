package com.threeknowbigdata.flink.dataset.source;

/**
 * 类描述：
 *
 * @ClassName Subject
 * @Description:
 * @Author: 土哥
 * @Date: 2021/8/13 14:04
 */
public class Subject {

    private int id;
    private String name;

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
