package cn.iaimi.openaisdk.aisender.openai.impl;

import cn.iaimi.openaisdk.aisender.openai.Sender;
import cn.iaimi.openaisdk.api.OpenAiApi;
import cn.iaimi.openaisdk.common.BaseResponse;
import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.exception.BusinessException;
import cn.iaimi.openaisdk.model.dto.ai.ConfigInfo;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionRequest;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionResponse;
import cn.iaimi.openaisdk.model.dto.ai.Message;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author clov614
 * {@code @date} 2024/1/25 10:58
 */
@NoArgsConstructor
@AllArgsConstructor
public class SenderImpl implements Sender {

    private ConfigInfo configInfo;

    @Resource
    private OpenAiApi openAiApi;

    @Override
    public Message chat(String message) {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setMessages(Arrays.asList(new Message("user", message)));

        BaseResponse<CreateChatCompletionResponse> chatCompletion =
                openAiApi.createChatCompletion(request, configInfo);
        // 错误处理
        if (chatCompletion.getCode() != 0) {
            throw new BusinessException(ErrorCode.CHAT_ERROR, chatCompletion.getMessage());
        }

        CreateChatCompletionResponse completionData = chatCompletion.getData();

        return completionData.getChoices().get(0).getMessage();
    }

    @Override
    public Message chatPresets(String message, String systemSets) {
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message("system", systemSets));
        messageList.add(new Message("user", message));

        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setMessages(messageList);

        BaseResponse<CreateChatCompletionResponse> chatCompletion =
                openAiApi.createChatCompletion(request, configInfo);
        // 错误处理
        if (chatCompletion.getCode() != 0) {
            throw new BusinessException(ErrorCode.CHAT_ERROR, chatCompletion.getMessage());
        }

        CreateChatCompletionResponse completionData = chatCompletion.getData();
        return completionData.getChoices().get(0).getMessage();
    }
}
