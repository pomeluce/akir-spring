package org.pomeluce.akir;

import org.junit.jupiter.api.Test;
import org.pomeluce.akir.common.utils.beans.BeanUtils;
import org.pomeluce.akir.server.system.domain.entity.User;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/11/12下午1:49
 * @className : BeanUtilsTest
 * @description : bean 工具测试类
 */
public class BeanUtilsTest {

    public @Test void getPropertyDescriptorsTest() {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(User.class);
        Arrays.stream(descriptors).forEach(System.out::println);
        PropertyDescriptor descriptor = descriptors[0];
        System.out.println(descriptor.getReadMethod());
        System.out.println(descriptor.getWriteMethod());
        System.out.println(descriptor.getReadMethod().getReturnType().isAssignableFrom(descriptor.getWriteMethod().getParameterTypes()[0]));
    }

    public @Test void copyPropertiesTest() {
        User user1 = new User();
        user1.setAccount("2222222");
        // user1.setAge(20);
        user1.setEmail(null);

        User user2 = new User();
        // user2.setAge(18);
        user2.setEmail("aaaaaaaaa");
        BeanUtils.copyProperties(user1, user2);
        System.out.println(user2);
    }
}
