package cn.iaimi.openaisdk.aisender;

import cn.iaimi.openaisdk.model.dto.ai.Message;

import java.util.List;

/**
 * 连续交互对话类
 * @author clov614
 * {@code @date} 2024/1/25 10:47
 */
public interface Exchanger {

    /**
     * 发送对话
     * @param message 发送给 AI 的消息
     * @return
     */
    Message talk(String message);

    /**
     * 获取所有对话历史
     * @return messageList
     */
    List<Message> getMsgs();

    /**
     * 获取最后一条回复
     * @return
     */
    Message getLastAnswer();

    /**
     * 从后往前删除 多条消息
     * @param itemNums 删除的数量
     * @return 是否删除成功
     */
    boolean removeLastMsgs(int itemNums);

    /**
     * 从前往后删除 多条消息
     * @param itemNums 删除的数量
     * @return 是否删除成功
     */
    boolean removeFirstMsgs(int itemNums);

    /**
     * 清空消息记录
     */
    void clearMsg();

    /**
     * 设置 预制系统消息
     *
     * @param preSetMsg 背景预设 例如：你的名字叫小智，你是一名全知全能的问答助手
     */
    void setPreSetMsg(String preSetMsg);

    /**
     * 清除 预设
     */
    void clearPreSet();

    /**
     * 设置最大历史消息条目
     * @param maxMsgSize 最多容纳消息
     */
    void setMaxMsgSize(int maxMsgSize);

}
