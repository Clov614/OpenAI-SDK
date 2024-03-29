package cn.iaimi.openaisdk;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2024/3/23
 */
public class AlibabaAITest {

    public static void main(String[] args) throws Exception{
        Constants.apiKey = "sk-xxxxx";
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content("你好，周末去哪里玩？").build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_PLUS).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .enableSearch(true)
                        .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
        msgManager.add(result);
        System.out.println(JsonUtils.toJson(result));
        param.setPrompt("找个近点的");
        param.setMessages(msgManager.get());
        result = gen.call(param);
        System.out.println(result);
        System.out.println(JsonUtils.toJson(result));


    }
}
