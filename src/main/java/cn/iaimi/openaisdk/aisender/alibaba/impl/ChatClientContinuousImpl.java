package cn.iaimi.openaisdk.aisender.alibaba.impl;

import cn.iaimi.openaisdk.aisender.alibaba.ChatRecordClient;
import cn.iaimi.openaisdk.common.BaseResData;
import com.alibaba.dashscope.aigc.generation.GenerationUsage;
import com.alibaba.dashscope.common.Message;
import lombok.NoArgsConstructor;

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
    public BaseResData<Message, GenerationUsage> chat(String message) {
        return doChat(message, null, true);
    }

    @Override
    public List<Message> getMsgs() {
        return msgManager.get();
    }

    @Override
    public Message getLast() {
        return msgManager.getLast();
    }

    @Override
    public Message getTopMsg() {
        return msgManager.getTop();
    }

    @Override
    @Deprecated
    public boolean removeLastMsgs(int itemNums) {
        boolean flag = false;
        // todo 尚未实现
        return flag;
    }

    @Override
    @Deprecated
    public boolean removeFirstMsgs(int itemNums) {
        boolean flag = false;
        // todo 待实现
        return flag;
    }

    @Override
    public void clearMsg() {
        msgManager.clearMsg();
    }
}
