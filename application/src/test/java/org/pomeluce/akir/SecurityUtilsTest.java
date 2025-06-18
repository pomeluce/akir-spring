package org.pomeluce.akir;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.akir.common.utils.spring.SecurityUtils;
import org.akir.core.web.service.AkirTokenService;
import org.akir.server.system.domain.entity.User;
import org.akir.server.system.repository.SystemUserRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27下午11:46
 * @className : SecurityUtilsTest
 * @description : SecurityUtils 测试类
 */
@SpringBootTest
public class SecurityUtilsTest {
    private @Resource SystemUserRepository repository;
    private @Resource AkirTokenService service;

    public @Test void findAll() {
        List<User> selectList = repository.findAll();
        selectList.forEach(System.out::println);
    }

    public @Test void encoderPasswordTest() {
        System.out.println(SecurityUtils.encoderPassword("12345678"));
    }

    public @Test void getJwtTest() {
        // Jws<Claims> jws = service.getJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNzAxMDA0MDcyNjM4Iiwic3ViIjoicmlmeS1jcmVhdGUtdG9rZW4iLCJpYXQiOjE3MDEwMDQwNzIsImlzcyI6InJhcGlkaWZ5IiwiZXhwIjoxNzAxMDA1ODcyLCJhY2Nlc3NfdG9rZW4iOiI4ZDJlNzc4MS0wZTkxLTQxMDktODc0Mi0zZDg4Y2Q3NjI1MzUifQ.6SQhWZJXicfJkluhe6Lt_AU3GzlAVssd2NAMuW9COuE");
    }
}
