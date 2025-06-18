package org.pomeluce.akir;

import com.blazebit.persistence.PagedList;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.akir.common.core.page.PaginationSupport;
import org.akir.common.utils.spring.SecurityUtils;
import org.akir.server.system.domain.entity.User;
import org.akir.server.system.domain.enums.UserStatus;
import org.akir.server.system.repository.SystemUserRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

/**
 * @author : marcus
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
        Optional<PagedList<User>> result = repository.find(User.builder().email("@gmail.com").build(), PaginationSupport.pageable());
        result.ifPresent(System.out::println);
    }
}
