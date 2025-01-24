package org.pomeluce.meagle;

import org.junit.jupiter.api.Test;
import org.pomeluce.meagle.common.utils.ObjectUtils;
import org.pomeluce.meagle.core.system.domain.entity.User;

import java.io.Serializable;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/11/12下午12:05
 * @className : ObjectUtilsTest
 * @description : object 工具测试类
 */
public class ObjectUtilsTest {
    public @Test void mergeTest() {
        Cat c1 = new Cat(null, 3, "black", "mammal");
        Cat c2 = new Cat("lulu", 18, "yellow", null);
        System.out.println(ObjectUtils.merge(c1, c2));
    }

    public @Test void cloneTest() {
        Cat c1 = new Cat("lulu", 3, "black", "mammal");
        Dog dog = new Dog();
        dog.name = "mimi";
        c1.setDog(dog);
        Cat c2 = ObjectUtils.clone(c1);
        System.out.println(c1);
        c2.getDog().name = "jiujiu";
        c2.setName("xiaohei");
        System.out.println(c1);
        System.out.println(c2);
    }

    public @Test void deepCloneTest() {
        User user1 = new User();
        user1.setAccount("2222222");

        User user2 = ObjectUtils.deepClone(user1);
        user2.setAccount("33333333");

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user1.equals(user2));
        System.out.println(user1 == user2);
    }
}

class Cat implements Serializable {
    private String name;
    private int age;
    private String color;
    private String type;
    private Dog dog;

    public Cat(String name, int age, String color, String type) {
        this.name = name;
        this.age = age;
        this.color = color;
        this.type = type;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                ", type='" + type + '\'' +
                ", dog=" + dog +
                '}';
    }
}

class Dog {
    String name;

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
