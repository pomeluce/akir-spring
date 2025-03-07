package org.pomeluce.akir;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.pomeluce.akir.common.core.page.PaginationSupport;
import org.pomeluce.akir.common.utils.spring.SecurityUtils;
import org.pomeluce.akir.core.system.domain.entity.QUser;
import org.pomeluce.akir.core.system.domain.entity.User;
import org.pomeluce.akir.core.system.domain.enums.UserStatus;
import org.pomeluce.akir.core.system.repository.SystemUserRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2024/8/10 20:48
 * @className : UserTest
 * @description : User 测试类
 */
@SpringBootTest
public class UserTest {

    private @Resource SystemUserRepository repository;

    @Test
    void save() {
        String[] names = {"zhangsan", "lisi", "wangwu", "jack", "alan", "amy", "xiaoming", "liyang"};
        for (String name : names) {
            User user = User.builder()
                    .account(name)
                    .password(SecurityUtils.encoderPassword(name + "pass"))
                    .email(name + "@gmail.com")
                    .status(UserStatus.ENABLED)
                    .createBy(name)
                    .updateBy(name)
                    .build();
            repository.save(user);
        }
    }

    @Test
    void findAkirUserByAccountTest() {
        System.out.println(repository.findByAccount("zhangsan").orElseThrow());
    }

    @Test
    void findUserList() {
        Optional<List<User>> result = repository.find(User.builder().email("@qq.com").build(), PaginationSupport.pageable().setDefaultOrder(QUser.user.id.desc()));
        result.ifPresent(System.out::println);
    }
}
