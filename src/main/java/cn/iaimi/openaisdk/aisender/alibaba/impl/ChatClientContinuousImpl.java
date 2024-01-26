package cn.iaimi.openaisdk.aisender.alibaba.impl;

import cn.iaimi.openaisdk.aisender.alibaba.ChatClient;
import cn.iaimi.openaisdk.aisender.alibaba.ChatRecordClient;
import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.exception.BusinessException;
import com.aliyun.broadscope.bailian.sdk.ApplicationClient;
import com.aliyun.broadscope.bailian.sdk.models.CompletionsRequest;
import com.aliyun.broadscope.bailian.sdk.models.CompletionsResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author clov614
 * {@code @date} 2024/1/26 18:17
 */
@NoArgsConstructor
public class ChatClientContinuousImpl extends ChatClientImpl implements ChatRecordClient {

    @Override
    public ChatRecordClient createClient(boolean isHistory) {
        return (ChatRecordClient) super.createClient(isHistory);
    }

    @Override
    public String chat(String message) {
        request = request.setPrompt(message).setHistory(new ArrayList<>()); // 设置历史消息
        CompletionsResponse response = client.completions(request);
        if (!response.isSuccess()) {
            String error = "completions Error requestId: " + response.getRequestId() + " code: " + response.getCode()
                    + " msg: " + response.getMessage();
            throw new BusinessException(ErrorCode.CHAT_ERROR, error);
        }
        String botAnswer = response.getData().getText();
        history.addLast(new CompletionsRequest.ChatQaPair(message, botAnswer)); // 添加历史消息
        // 维护大小
        while (history.size() > maxMsgSize) {
            history.removeFirst();
        }

        return botAnswer;
    }

    @Override
    public List<CompletionsRequest.ChatQaPair> getMsgs() {
        return new ArrayList<>(history);
    }

    @Override
    public CompletionsRequest.ChatQaPair getLastAnswer() {
        return history.getLast();
    }

    @Override
    public boolean removeLastMsgs(int itemNums) {
        boolean flag = true;

        while (itemNums-- > 0) {
            if (history.isEmpty()) {
                flag = false;
                break;
            }
            history.removeLast();
        }
        return flag;
    }

    @Override
    public boolean removeFirstMsgs(int itemNums) {
        boolean flag = true;

        while (itemNums-- > 0) {
            if (history.isEmpty()) {
                flag = false;
                break;
            }
            history.removeFirst();
        }
        return flag;
    }

    @Override
    public void clearMsg() {
        history.clear();
    }
}
