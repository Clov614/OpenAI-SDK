package cn.iaimi.openaisdk.aisender.alibaba;

import cn.iaimi.openaisdk.common.BaseResData;
import com.alibaba.dashscope.aigc.generation.GenerationUsage;
import com.alibaba.dashscope.common.Message;

/**
 * @author clov614
 * {@code @date} 2024/1/26 17:50
 */
public interface ChatClient {

    ChatClient createClient(boolean isHistory);

    ChatClient setPresets(String systemMsg);

    void add(String userMsg);

    BaseResData<Message, GenerationUsage> chat(String message);

    /**
     * 单词对话  携带 系统预设消息
     *
     * @param message    用户消息
     * @param systemSets 系统预设 prompt
     * @return
     */
    BaseResData<Message, GenerationUsage> chatPresets(String message, String systemSets);

}
