package cn.iaimi.openaisdk.aisender.alibaba;

import cn.iaimi.openaisdk.common.BaseResData;
import com.alibaba.dashscope.aigc.generation.GenerationUsage;
import com.alibaba.dashscope.common.Message;

import java.util.List;

/**
 * @author clov614
 * {@code @date} 2024/1/26 17:50
 */
public interface ChatClient {

    ChatClient createClient(boolean isHistory);

    ChatClient setPresets(String systemMsg);

    void add(String userMsg);

    BaseResData<Message, GenerationUsage> chat(String message);

    BaseResData<List<Message>, GenerationUsage> chat(List<Message> messages);

    /**
     * 单词对话  携带 系统预设消息
     *
     * @param message    用户消息
     * @param systemSets 系统预设 prompt
     * @return
     */
    BaseResData<Message, GenerationUsage> chatPresets(String message, String systemSets);

    /**
     * 添加预设 消息
     * @param messages
     * @param systemSet
     * @return
     */
    List<Message> addPresets(List<Message> messages, String systemSet);

    /**
     * 添加用户 消息
     * @param messages
     * @param userMsg
     * @return
     */
    List<Message> addUser(List<Message> messages, String userMsg);

}
