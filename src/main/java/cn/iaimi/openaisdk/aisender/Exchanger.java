package cn.iaimi.openaisdk.aisender;

import cn.iaimi.openaisdk.model.dto.ai.Message;

import java.util.List;

/**
 * @author clov614
 * {@code @date} 2024/1/25 10:47
 */
public interface Exchanger {

    Message talk(String message);

    List<Message> getMsgs();

    Message getLastAnswer();

    boolean removeLastMsgs(int itemNums);

    boolean removeFirstMsgs(int itemNums);

    void clearMsg();

    void setPreSetMsg(String preSetMsg);

    void clearPreSet();

    void setMaxMsgSize(int maxMsgSize);

}
