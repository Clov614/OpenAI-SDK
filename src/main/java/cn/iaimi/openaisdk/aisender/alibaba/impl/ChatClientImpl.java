package cn.iaimi.openaisdk.aisender.alibaba.impl;

import cn.hutool.core.util.IdUtil;
import cn.iaimi.openaisdk.AliChatAiSdkConfig;
import cn.iaimi.openaisdk.aisender.alibaba.ChatClient;
import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.exception.BusinessException;
import com.aliyun.broadscope.bailian.sdk.AccessTokenClient;
import com.aliyun.broadscope.bailian.sdk.ApplicationClient;
import com.aliyun.broadscope.bailian.sdk.models.CompletionsRequest;
import com.aliyun.broadscope.bailian.sdk.models.CompletionsResponse;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author clov614
 * {@code @date} 2024/1/26 17:51
 */
@NoArgsConstructor

public class ChatClientImpl implements ChatClient {

    protected ApplicationClient client;

    protected CompletionsRequest request;

    /**
     * 最大历史消息数
     */
    protected int maxMsgSize;

    protected Deque<CompletionsRequest.ChatQaPair> history;

    @Resource
    private AliChatAiSdkConfig aliChatAiSdkConfig;
    @Resource
    private AccessTokenClient accessTokenClient;


    @Override
    public ChatClient createClient(boolean isHistory) {
        client = ApplicationClient.builder().token(accessTokenClient.getToken()).build();

        // 获取配置信息
        // 构建request
        this.request = new CompletionsRequest()
                .setAppId(aliChatAiSdkConfig.getAppId());
        if (isHistory) { // 开启历史消息模式
            request = request.setSessionId(IdUtil.simpleUUID());
            history = new LinkedList<>();
            maxMsgSize = aliChatAiSdkConfig.getMsgMaxSize();
        }
        return this;
    }

    @Override
    public String chat(String message) {
        request = request.setPrompt(message);
        CompletionsResponse response = client.completions(request);
        if (!response.isSuccess()) {
            String error = "completions Error requestId: " + response.getRequestId() + " code: " + response.getCode()
                    + " msg: " + response.getMessage();
            throw new BusinessException(ErrorCode.CHAT_ERROR, error);
        }
        return response.getData().getText();
    }
}
