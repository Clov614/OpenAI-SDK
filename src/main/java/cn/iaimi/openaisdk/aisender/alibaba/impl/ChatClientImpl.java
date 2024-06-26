package cn.iaimi.openaisdk.aisender.alibaba.impl;

import cn.iaimi.openaisdk.AliChatAiSdkConfig;
import cn.iaimi.openaisdk.aisender.alibaba.ChatClient;
import cn.iaimi.openaisdk.common.BaseResData;
import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.exception.BusinessException;
import cn.iaimi.openaisdk.manager.MsgManager;
import cn.iaimi.openaisdk.model.enums.ModelType;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.GenerationUsage;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author clov614
 * {@code @date} 2024/1/26 17:51
 */
@NoArgsConstructor
public class ChatClientImpl implements ChatClient {

    protected Generation gen;

    protected MsgManager msgManager;

    protected static Map<String, ModelType> modelMap = new HashMap<>();

    static {
        modelMap.put("qwen-v1", ModelType.QWEN_V1);
        modelMap.put("qwen-turbo", ModelType.QWEN_TURBO);
        modelMap.put("bailian-v1", ModelType.BAILIAN_V1);
        modelMap.put("dolly-12b-v2", ModelType.DOLLY_12B_V2);
        modelMap.put("qwen-plus", ModelType.QWEN_PLUS);
        modelMap.put("qwen-max", ModelType.QWEN_MAX);
    }

    /**
     * 最大历史消息数
     */
    protected int maxMsgSize;

//    protected EvictingQueue<Message> history;

    @Resource
    private AliChatAiSdkConfig aliChatAiSdkConfig;


    @Override
    public ChatClient setPresets(String systemMsg) {
        if (null != systemMsg) {
            Message msg =
                    Message.builder().role(Role.SYSTEM.getValue()).content(systemMsg).build();
            msgManager.add(msg);
        }
        return this;
    }

    @Override
    public void add(String userMsg) {
        if (null != userMsg) {
            Message msg =
                    Message.builder().role(Role.USER.getValue()).content(userMsg).build();
            msgManager.add(msg);
        }
    }


    @Override
    public ChatClient createClient(boolean isHistory) {
        // 获取配置信息 设置 apiKey
        Constants.apiKey = aliChatAiSdkConfig.getApiKey();
        // 构建 gen 和 msgManager
        this.gen = new Generation();

        if (isHistory) { // tag 开启历史消息模式

        }
        this.msgManager = new MsgManager(aliChatAiSdkConfig.getMsgMaxSize());
        return this;
    }

    @Override
    public BaseResData<Message, GenerationUsage> chat(String message) {
        return doChat(message, null, false);
    }

    @Override
    public BaseResData<List<Message>, GenerationUsage> chat(List<Message> messages) {
        return doChat(messages);
    }

    @Override
    public BaseResData<Message, GenerationUsage> chatPresets(String message, String systemSets) {
        return doChat(message, systemSets, false);
    }

    @Override
    public List<Message> addPresets(List<Message> messages, String systemSet) {
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content(systemSet).build();
        messages.add(systemMsg);
        return messages;
    }

    @Override
    public List<Message> addUser(List<Message> messages, String userMsg) {
        Message msg = Message.builder().role(Role.USER.getValue()).content(userMsg).build();
        messages.add(msg);
        return messages;
    }

    public BaseResData<Message, GenerationUsage> doChat(String message, String systemSets, boolean isHistory) {
        if (null != systemSets) {
            Message systemMsg =
                    Message.builder().role(Role.SYSTEM.getValue()).content(systemSets).build();
            msgManager.add(systemMsg);
        }
        Message msg = Message.builder().role(Role.USER.getValue()).content(message).build();
        msgManager.add(msg);

        // TODO 细化 参数
        QwenParam param = QwenParam.builder()
                .model(modelMap.getOrDefault(aliChatAiSdkConfig.getUseModel(), ModelType.QWEN_MAX).getValue())
                .messages(msgManager.get())
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(aliChatAiSdkConfig.getTopP()) // 取值越大，生成的随机性越高(0, 1.0)
                .enableSearch(aliChatAiSdkConfig.getEnableSearch()) // 是否启用搜索？
                .build();
        try {
            GenerationResult result = gen.call(param);
            msgManager.add(result); // 保存回复
            Message msgByRes = msgManager.getMsgByRes(result);

            return new BaseResData<>(msgByRes, result.getUsage());
        } catch (NoApiKeyException e) {
            throw new BusinessException(ErrorCode.NO_API_KEY, e.getMessage());
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        } finally {
            if (!isHistory) { // 不保存历史消息 // TODO 单次询问借助队列特性，改为弹出
                msgManager.clearMsg();
            }
        }
    }

    private BaseResData<List<Message>, GenerationUsage> doChat(List<Message> messages) {
        // TODO 细化 参数
        QwenParam param = QwenParam.builder()
                .model(modelMap.getOrDefault(aliChatAiSdkConfig.getUseModel(), ModelType.QWEN_MAX).getValue())
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(aliChatAiSdkConfig.getTopP()) // 取值越大，生成的随机性越高(0, 1.0)
                .enableSearch(aliChatAiSdkConfig.getEnableSearch()) // 是否启用搜索？
                .build();
        try {
            GenerationResult result = gen.call(param);
            msgManager.add(result); // 保存回复
            Message msgByRes = msgManager.getMsgByRes(result);
            messages.add(msgByRes);
            return new BaseResData<>(messages, result.getUsage());
        } catch (NoApiKeyException e) {
            throw new BusinessException(ErrorCode.NO_API_KEY, e.getMessage());
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }


}
