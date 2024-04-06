package cn.iaimi.openaisdk.aisender.openai;

import cn.iaimi.openaisdk.common.BaseResData;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionResponse;
import cn.iaimi.openaisdk.model.dto.ai.Message;

/**
 * 单词对话请求
 * 如需连续对话(历史记录) 请使用 Exchanger 接口
 *
 * @author clov614
 * {@code @date} 2024/1/25 10:41
 */
public interface Sender {

    /**
     * 单次对话 不记录对话历史
     *
     * @param message 用户消息
     * @return Message 消息对象
     */
    BaseResData<Message, CreateChatCompletionResponse.UsageBean> chat(String message);

    /**
     * 单词对话  携带 系统预设消息
     *
     * @param message    用户消息
     * @param systemSets 系统预设 prompt
     * @return Message 消息对象
     */
    BaseResData<Message, CreateChatCompletionResponse.UsageBean> chatPresets(String message, String systemSets);
}
