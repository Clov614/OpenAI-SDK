# OpenAI-SDK

封装了常用的对 ChatGPT 的 API 的调用

## 目录
- [快速开始](#快速开始)
    - [第一种使用方式： 工厂模式](#第一种使用方式)
    - [第二种使用方式： SpringBoot-Starter](#第二种使用方式)


## 快速开始

### 第一种使用方式

通过工厂模式构建出请求实例进行请求

```java
/* 测试类 */
public static void main(String[] args) {
    String openAiApiKey = "sk-xxxxxxxx"; // 你的 secretKey
    OpenAiApi openAiApi = new OpenAiApi();

    AiFactory aiFactory = new AiFactory(openAiApiKey, openAiApi)
            .withUrl("https://api.openai.com/v1/chat/completions")
            .withProxy("127.0.0.1", 7890) // 代理地址
            .withModel("gpt-3.5-turbo")                     // 模型类型设置
            .withMsgMaxSize(30);                            // 最大历史消息数

    Sender sender = aiFactory.createSender();
    Exchanger exchanger = aiFactory.createExchanger();

    try {
        // region 单次对话示例
        Message chat = sender.toChat("你好，这是一条测试消息");
        System.out.println(chat);

        Message chatPresets = sender.toChatPresets("你好，请告诉我你是谁", "你的名字叫小智，是一名无所不知的智者");
        System.out.println(chatPresets);
        // endregion
        
        // region 连续对话示例
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
        // endregion
    } catch (BusinessException be) {
        throw new RuntimeException(be);
    }

}
```

### 第二种使用方式

通过 maven 引入

`pom.xml` 中引入该 maven 依赖
```xml
<dependency>
    <groupId>cn.iaimi</groupId>
    <artifactId>OpenAI-SDK</artifactId>
    <version>0.0.1</version>
</dependency>
```

`application.yml` 中配置如下条目
```yaml
chatai:
  open-ai:
    # 你的 secretKey
    open-ai-api-key: sk-xxxxxxxxx
    # 代理地址和端口
    proxy-host: 127.0.0.1
    proxy-port: 7890
    # 最大历史消息数 (可选)
    msg-max-size: 30
```

通过依赖注入即可使用
```java
@Resource
private Sender sender;

@Resource
private Exchanger exchanger;
```

#### 测试类示例如下



```java
/**
 * @author clov614
 * {@code @date} 2024/1/25 16:28
 */
@SpringBootTest
@ActiveProfiles({"work"})
public class OpenAITest {

    @Resource
    private Sender sender;

    @Resource
    private Exchanger exchanger;

    /*单次对话测试*/
    @Test
    void talkSingleTest() {
        Message chat = sender.toChat("你好，这是一条测试消息");
        System.out.println(chat);

        Message chatPresets = sender.toChatPresets("你好，请告诉我你是谁", "你的名字叫小智，是一名无所不知的智者");
        System.out.println(chatPresets);
    }

    /*连续对话测试*/
    @Test
    void talkContinueTest() {
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
    }

}
```


