package cn.iaimi.openaisdk.manager;

import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.DashScopeResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.google.common.collect.EvictingQueue;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2024/3/24
 */
public class MsgManager {
    private static final int DEFAULT_MAXIMUM_MESSAGES = 10;
    private final EvictingQueue<Message> messages;

    public MsgManager() {
        this.messages = EvictingQueue.create(10);
    }

    public MsgManager(int maxMessages) {
        this.messages = EvictingQueue.create(maxMessages);
    }

    public void add(ConversationParam param) {
    }

    public void add(DashScopeResult result) {
        JsonObject obj = (JsonObject)result.getOutput();
        if (obj.has("text")) {
            this.messages.add(Message.builder().role(Role.ASSISTANT.getValue()).content(obj.get("text").getAsString()).build());
        }

    }

    public void add(ConversationResult result) {
        this.addWithOutput(result.getOutput());
    }

    public void add(Message msg) {
        this.messages.add(msg);
    }

    public List<Message> get() {
        return Arrays.asList(this.messages.toArray(new Message[0]));
    }

    public List<Message> get(int start, int end) {
        return Arrays.asList(this.messages.toArray(new Message[0])).subList(start, end);
    }

    public Message getTop() {
        return messages.peek();
    }

    public Message getLast() {
        LinkedList<Message> linkedList = new LinkedList<>(messages);
        return linkedList.getLast();
    }


    public Message getMsgByRes(GenerationResult result) {
        GenerationOutput output = result.getOutput();
        if (output.getChoices() != null && !output.getChoices().isEmpty()) {
            GenerationOutput.Choice choice = (GenerationOutput.Choice)output.getChoices().get(0);
            return choice.getMessage();
        }

        if (output.getText() != null) {
            return Message.builder().role(Role.ASSISTANT.getValue()).content(output.getText()).build();
        }
        return null;
    }

    public boolean clearMsg() {
        messages.clear();
        return true;
    }

    public void add(GenerationResult result) {
        this.addWithOutput(result.getOutput());
    }

    private void addWithOutput(GenerationOutput output) {
        if (output.getChoices() != null && !output.getChoices().isEmpty()) {
            GenerationOutput.Choice choice = (GenerationOutput.Choice)output.getChoices().get(0);
            this.messages.add(choice.getMessage());
        }

        if (output.getText() != null) {
            this.messages.add(Message.builder().role(Role.ASSISTANT.getValue()).content(output.getText()).build());
        }

    }
}
