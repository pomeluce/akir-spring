package org.akir.server.system.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/22下午1:14
 * @className : LoginBody
 * @description : 登录请求参数实体
 */
@Schema(description = "登录请求参数")
public record LoginBody(
        @Schema(description = "请求唯一标识") String uid,
        @Schema(description = "账号") String account,
        @Schema(description = "密码") String password,
        @Schema(description = "验证码") String captcha
) {
}
