package cn.iaimi.openaisdk.aisender.alibaba;

import com.alibaba.dashscope.common.Message;

import java.util.List;

/**
 * @author clov614
 * {@code @date} 2024/1/26 18:14
 */
public interface ChatRecordClient extends ChatClient {

    ChatRecordClient createClient(boolean isHistory);

    /**
     * 获取所有对话历史
     *
     * @return messageList
     */
    List<Message> getMsgs();

    /**
     * 获取最后一条回复
     *
     * @return
     */
    Message getLast();

    /**
     * 获取最开头的一条消息
     * @return
     */
    Message getTopMsg();

    /**
     * 从后往前删除 多条消息
     * @param itemNums 删除的数量
     * @return boolean 是否删除成功
     */
    boolean removeLastMsgs(int itemNums);

    /**
     * 从前往后删除 多条消息
     * @param itemNums 删除的数量
     * @return boolean 是否删除成功
     */
    boolean removeFirstMsgs(int itemNums);

    /**
     * 清空消息记录
     */
    void clearMsg();
}
