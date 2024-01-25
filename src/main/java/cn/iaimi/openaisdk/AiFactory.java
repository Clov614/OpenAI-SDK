package cn.iaimi.openaisdk;

import cn.iaimi.openaisdk.aisender.Exchanger;
import cn.iaimi.openaisdk.aisender.Sender;
import cn.iaimi.openaisdk.aisender.impl.ExchangerImpl;
import cn.iaimi.openaisdk.aisender.impl.SenderImpl;
import cn.iaimi.openaisdk.api.OpenAiApi;
import cn.iaimi.openaisdk.exception.BusinessException;
import cn.iaimi.openaisdk.model.dto.ai.ConfigInfo;
import cn.iaimi.openaisdk.model.dto.ai.Message;

import java.util.List;

public class AiFactory {

    private String openAiApiKey;
    private String url = "https://api.openai.com/v1/chat/completions";
    private String proxyHost;
    private Integer proxyPort;
    private String model = "gpt-3.5-turbo";
    private int msgMaxSize = 20;
    private OpenAiApi openAiApi;

    public AiFactory(String openAiApiKey, OpenAiApi openAiApi) {
        this.openAiApiKey = openAiApiKey;
        this.openAiApi = openAiApi;
    }

    public AiFactory withUrl(String url) {
        this.url = url;
        return this;
    }

    public AiFactory withProxy(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        return this;
    }

    public AiFactory withModel(String model) {
        this.model = model;
        return this;
    }

    public AiFactory withMsgMaxSize(int msgMaxSize) {
        this.msgMaxSize = msgMaxSize;
        return this;
    }

    public Sender createSender() {
        ConfigInfo configInfo = createConfigInfo();
        return new SenderImpl(configInfo, openAiApi);
    }

    public Exchanger createExchanger() {
        ConfigInfo configInfo = createConfigInfo();
        return new ExchangerImpl(msgMaxSize, configInfo, openAiApi);
    }

    private ConfigInfo createConfigInfo() {
        return new ConfigInfo(openAiApiKey, url, proxyHost, proxyPort, model);
    }

    /* 测试类 */
    public static void main(String[] args) {
        String openAiApiKey = "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";
        OpenAiApi openAiApi = new OpenAiApi();

        AiFactory aiFactory = new AiFactory(openAiApiKey, openAiApi)
                .withUrl("https://api.openai.com/v1/chat/completions")
                .withProxy("127.0.0.1", 7890)
                .withModel("gpt-3.5-turbo")
                .withMsgMaxSize(30);

        Sender sender = aiFactory.createSender();
        Exchanger exchanger = aiFactory.createExchanger();

        try {
            Message chat = sender.toChat("你好，这是一条测试消息");
            System.out.println(chat);

            Message chatPresets = sender.toChatPresets("你好，请告诉我你是谁", "你的名字叫小智，是一名无所不知的智者");
            System.out.println(chatPresets);

            Message res = exchanger.talk("请你记住 task = 123");
            System.out.println(res);

            res = exchanger.talk("task 的值 是多少，回答我");
            System.out.println(res);
            long startTime = System.currentTimeMillis();
            exchanger.setPreSetMsg("你现在是一位绘图专家，你最擅长的事情就是绘画");
            Message talk = exchanger.talk("告诉我，你最擅长的事情");
            System.out.println(talk);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Elapsed Time: " + elapsedTime / 1000 + " seconds");

            talk = exchanger.talk("介绍一下你自己");
            System.out.println(talk);

            List<Message> msgs = exchanger.getMsgs();
            System.out.println("msgs: " + msgs);

            Message lastAnswer = exchanger.getLastAnswer();
            System.out.println("lastAnswer: " + lastAnswer);

            exchanger.clearMsg();

            Message talk1 = exchanger.talk("你好，介绍一下你自己");
            System.out.println(talk1);

            List<Message> msgs1 = exchanger.getMsgs();
            System.out.println(msgs1);
            System.out.println(msgs1.size());
            exchanger.clearPreSet();
        } catch (BusinessException be) {
            throw new RuntimeException(be);
        }

    }
}
