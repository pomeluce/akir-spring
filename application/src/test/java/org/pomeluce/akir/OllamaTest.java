package org.pomeluce.akir;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2025/3/5 21:14
 * @className : OllamaTest
 * @description : Ollama 本地大模型测试
 */
@SpringBootTest
public class OllamaTest {
    private @Resource OllamaChatModel model;

    public @Test void testMessage() {
        String message = "我现在需要在 springboot 项目中集成 ollama 本地大模型, 我该如何操作?";
        String result = model.call(message);
        System.out.println(result);
    }
}
