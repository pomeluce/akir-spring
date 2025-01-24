package org.pomeluce.meagle;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.pomeluce.meagle.common.utils.spring.SecurityUtils;
import org.pomeluce.meagle.core.system.domain.entity.User;
import org.pomeluce.meagle.core.system.repository.SystemUserRepository;
import org.pomeluce.meagle.core.web.service.MeagleTokenService;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/27下午11:46
 * @className : SecurityUtilsTest
 * @description : SecurityUtils 测试类
 */
@SpringBootTest
public class SecurityUtilsTest {
    private @Resource SystemUserRepository repository;
    private @Resource MeagleTokenService service;

    public @Test void findAll() {
        List<User> selectList = repository.findAll();
        selectList.forEach(System.out::println);
    }

    public @Test void encoderPasswordTest() {
        System.out.println(SecurityUtils.encoderPassword("123456"));
    }

    public @Test void getJwtTest() {
        // Jws<Claims> jws = service.getJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNzAxMDA0MDcyNjM4Iiwic3ViIjoicmlmeS1jcmVhdGUtdG9rZW4iLCJpYXQiOjE3MDEwMDQwNzIsImlzcyI6InJhcGlkaWZ5IiwiZXhwIjoxNzAxMDA1ODcyLCJhY2Nlc3NfdG9rZW4iOiI4ZDJlNzc4MS0wZTkxLTQxMDktODc0Mi0zZDg4Y2Q3NjI1MzUifQ.6SQhWZJXicfJkluhe6Lt_AU3GzlAVssd2NAMuW9COuE");
    }
}
