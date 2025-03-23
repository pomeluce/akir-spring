package org.pomeluce.akir.controller.system;

import com.pig4cloud.captcha.ArithmeticCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.pomeluce.akir.common.annotation.RestApiController;
import org.pomeluce.akir.common.constants.JwtKeyConstants;
import org.pomeluce.akir.common.constants.RedisKeyConstants;
import org.pomeluce.akir.common.core.controller.BaseController;
import org.pomeluce.akir.common.core.redis.RedisClient;
import org.pomeluce.akir.common.utils.id.IdGenerator;
import org.pomeluce.akir.core.web.service.AkirLoginService;
import org.pomeluce.akir.server.system.domain.model.Captcha;
import org.pomeluce.akir.server.system.domain.model.LoginBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27 23:38
 * @className : LoginController
 * @description : 登录控制器
 */
@RestApiController("/auth")
@Tag(name = "登录控制器")
public class SystemLoginController extends BaseController {
    private @Resource AkirLoginService service;
    private @Resource RedisClient redisClient;

    @Operation(summary = "用户登录")
    public @PostMapping("/login") String login(@RequestBody LoginBody loginBody) {
        service.verifyCaptcha(loginBody.uid(), loginBody.captcha());
        return JwtKeyConstants.TOKEN_PREFIX + service.login(loginBody.account(), loginBody.password());
    }

    /**
     * 获取验证码
     *
     * @return 返回验证码对象
     */
    public @GetMapping("captcha") Captcha captcha() {
        ArithmeticCaptcha cap = new ArithmeticCaptcha();
        cap.supportAlgorithmSign(4);
        cap.setDifficulty(100);
        String uid = IdGenerator.randomUUID(true);
        String answer = cap.text();
        redisClient.hset(RedisKeyConstants.CAPTCHA_ANSWER_KEY, uid, answer);
        return new Captcha(uid, cap.toBase64());
    }
}
