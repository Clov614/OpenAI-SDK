package cn.iaimi.openaisdk.helper;

import cn.iaimi.openaisdk.model.dto.ai.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SerializationHelper {

    private static final Gson gson = new Gson();

    // 使用 GSON 序列化 List<Message> 为 JSON 字符串
    public static<D> String serializeMessageList(List<D> messageList) {
        return gson.toJson(messageList);
    }

    // 使用 GSON 反序列化 JSON 字符串为 List<Message>
    public static<D> List<D> deserializeMessageList(String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<List<D>>(){}.getType());
    }

    // 保证类型安全
    public static <T> List<T> deserializeMessageList(String jsonString, Type typeOfT) {
        return gson.fromJson(jsonString, typeOfT);
    }

    // 示例用法
    public static void main(String[] args) {
        // 假设 messageList 是已经填充的 List<Message>
        List<Message> messageList = new ArrayList<>();
        Message message = new Message();
        message.setContent("hello world");
        message.setRole("user");
        messageList.add(message);

        // 序列化
        String serializedData = serializeMessageList(messageList);
        System.out.println("Serialized Message List: " + serializedData);

        // 反序列化
        List<Message> deserializedList = deserializeMessageList(serializedData);
        System.out.println("Deserialized List: " + deserializedList);
    }
}
