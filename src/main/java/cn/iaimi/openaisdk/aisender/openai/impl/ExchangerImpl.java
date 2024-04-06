package cn.iaimi.openaisdk.aisender.openai.impl;

import cn.iaimi.openaisdk.aisender.openai.Exchanger;
import cn.iaimi.openaisdk.api.OpenAiApi;
import cn.iaimi.openaisdk.common.BaseResData;
import cn.iaimi.openaisdk.common.BaseResponse;
import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.exception.BusinessException;
import cn.iaimi.openaisdk.model.dto.ai.ConfigInfo;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionRequest;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionResponse;
import cn.iaimi.openaisdk.model.dto.ai.Message;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clov614
 * {@code @date} 2024/1/25 13:00
 */
@NoArgsConstructor
public class ExchangerImpl implements Exchanger {

    /*记录&&维护所有对话*/
    private Deque<Message> msgDeque;
    /*消息队列的最大长度*/
    private int maxMsgSize;
    /*配置信息*/
    private ConfigInfo configInfo;

    /**
     * 预设消息
     * {"role": "system","content": "You are a helpful assistant."},
     */
    private Message preSetMsg;

    @Resource
    private OpenAiApi openAiApi;

    public ExchangerImpl(int maxMsgSize, ConfigInfo configInfo) {
        msgDeque = new LinkedList<>();
        this.maxMsgSize = maxMsgSize;
        this.configInfo = configInfo;
    }

    public ExchangerImpl(int maxMsgSize, ConfigInfo configInfo, OpenAiApi openAiApi) {
        msgDeque = new LinkedList<>();
        this.maxMsgSize = maxMsgSize;
        this.configInfo = configInfo;
        this.openAiApi = openAiApi;
    }

    @Override
    public BaseResData<Message, CreateChatCompletionResponse.UsageBean> chat(String message) {

        if (preSetMsg != null) { // 假如存在预设消息，拼入消息开头
            msgDeque.addFirst(preSetMsg);
        }
        // 发送消息，并记录对话
        msgDeque.addLast(new Message("user", message));
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setMessages(new ArrayList<>(msgDeque));
        BaseResponse<CreateChatCompletionResponse> response = openAiApi.createChatCompletion(request, configInfo);
        // 错误处理
        if (response.getCode() != 0) {
            throw new BusinessException(ErrorCode.CHAT_ERROR, response.getMessage());
        }

        List<CreateChatCompletionResponse.ChoicesBean> choices = response.getData().getChoices();

        Message replyMsg = choices.get(choices.size() - 1).getMessage();
        // 记录对话
        msgDeque.addLast(replyMsg);
        // 移除预设消息
        if (preSetMsg != null) {
            msgDeque.removeFirst();
        }
        // 维护 消息队列的长度
        while (msgDeque.size() > maxMsgSize) {
            removeFirstMsgs(2);
        }


        return new BaseResData<>(replyMsg, response.getData().getUsage());
    }

    @Override
    public List<Message> getMsgs() {

        return new ArrayList<>(msgDeque);
    }

    @Override
    public Message getLastAnswer() {
        return msgDeque.isEmpty() ? null : msgDeque.getLast();
    }

    @Override
    public boolean removeLastMsgs(int itemNums) {
        boolean flag = true;

        while (itemNums-- > 0) {
            if (msgDeque.isEmpty()) {
                flag = false;
                break;
            }
            msgDeque.removeLast();
        }
        return flag;
    }

    @Override
    public boolean removeFirstMsgs(int itemNums) {
        boolean flag = true;

        while (itemNums-- > 0) {
            if (msgDeque.isEmpty()) {
                flag = false;
                break;
            }
            msgDeque.removeFirst();
        }
        return flag;
    }

    @Override
    public void clearMsg() {
        msgDeque.clear();
    }

    /**
     * 设置 预制系统消息
     *
     * @param preSetMsg role: system
     */
    @Override
    public void setPreSetMsg(String preSetMsg) {
        this.preSetMsg = new Message("system", preSetMsg);
    }

    @Override
    public void clearPreSet() {
        this.preSetMsg = null;
    }

    @Override
    public void setMaxMsgSize(int maxMsgSize) {
        this.maxMsgSize = maxMsgSize;
    }
}
