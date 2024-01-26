package cn.iaimi.openaisdk.aisender.alibaba;

/**
 * @author clov614
 * {@code @date} 2024/1/26 17:50
 */
public interface ChatClient {

    ChatClient createClient(boolean isHistory);

    String chat(String message);


}
