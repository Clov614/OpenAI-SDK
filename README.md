# OpenAI-SDK

OpenAI-SDK 封装了对 ChatGPT 和阿里巴巴通义千问 API 的常用调用。

## 目录
- [快速开始](#快速开始)
  - [第一种使用方式： 工厂模式](#第一种使用方式)
    - [ChatGPT调用示例](#chatgpt调用示例)
    - [通义千问调用示例](#通义千问调用示例)
  - [第二种使用方式： SpringBoot-Starter](#第二种使用方式)
    - [ChatGPT配置&&调用示例](#chatgpt配置调用示例)
    - [通义千问配置&&调用示例](#通义千问配置调用示例)
      - [ChatGPT测试类](#chatgpt测试类)
      - [通义千问测试类](#通义千问测试类)


## 快速开始

### 第一种使用方式

通过工厂模式构建出请求实例进行请求

<details>
  <summary>ChatGPT调用示例</summary>

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
    Message chat = sender.chat("你好，这是一条测试消息");
    System.out.println(chat);

    Message chatPresets = sender.chatPresets("你好，请告诉我你是谁", "你的名字叫小智，是一名无所不知的智者");
    System.out.println(chatPresets);

    Message res = exchanger.chat("请你记住 task = 123");
    System.out.println(res);

    res = exchanger.chat("task 的值 是多少，回答我");
    System.out.println(res);
    long startTime = System.currentTimeMillis();
    exchanger.setPreSetMsg("你现在是一位绘图专家，你最擅长的事情就是绘画");
    Message talk = exchanger.chat("告诉我，你最擅长的事情");
    System.out.println(talk);
    long endTime = System.currentTimeMillis();
    long elapsedTime = endTime - startTime;
    System.out.println("Elapsed Time: " + elapsedTime / 1000 + " seconds");

    talk = exchanger.chat("介绍一下你自己");
    System.out.println(talk);

    List<Message> msgs = exchanger.getMsgs();
    System.out.println("msgs: " + msgs);

    Message lastAnswer = exchanger.getLastAnswer();
    System.out.println("lastAnswer: " + lastAnswer);

    exchanger.clearMsg();

    Message talk1 = exchanger.chat("你好，介绍一下你自己");
    System.out.println(talk1);

    List<Message> msgs1 = exchanger.getMsgs();
    System.out.println(msgs1);
    System.out.println(msgs1.size());
    exchanger.clearPreSet();
  } catch (BusinessException be) {
    throw new RuntimeException(be);
  }

}
```
</details>

<details>
  <summary>通义千问调用示例</summary>

</details>

### 第二种使用方式

通过 maven 引入

`pom.xml` 中引入该 maven 依赖
```xml
<dependency>
    <groupId>cn.iaimi</groupId>
    <artifactId>OpenAI-SDK</artifactId>
    <version>0.0.3</version>
</dependency>
```

#### ChatGPT配置&&调用示例

<details>
  <summary>ChatGPT配置&&调用示例</summary>

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

</details>

#### 通义千问配置&&调用示例

<details>
  <summary>通义千问配置&&调用示例</summary>

`application.yml` 中配置如下条目
```yaml
chatai:
  open-ai:
  alibaba:
    # 最大历史消息数 (可选)
    msg-max-size: 20
    # 百炼平台中获取
    api-key: sk-xxxxxxxx
    use-model: qwen-max
```

通过依赖注入即可使用
```java
@Resource
private ChatClient chatClient;

@Resource
private ChatRecordClient chatRecordClient;
```

</details>




#### 测试类示例如下

##### ChatGPT测试类

<details>
  <summary>ChatGPT测试类</summary>

```java
/**
 * @author clov614
 * {@code @date} 2024/1/25 16:28
 */
@SpringBootTest
public class OpenAITest {

  @Resource
  private Sender sender;

  @Resource
  private Exchanger exchanger;

  /*单次对话测试*/
  @Test
  void talkSingleTest() {
    Message chat = sender.chat("你好，这是一条测试消息");
    System.out.println(chat);

    Message chatPresets = sender.chatPresets("你好，请告诉我你是谁", "你的名字叫小智，是一名无所不知的智者");
    System.out.println(chatPresets);
  }

  /*连续对话测试*/
  @Test
  void talkContinueTest() {
    Message res = exchanger.chat("请你记住 task = 123");
    System.out.println(res);

    res = exchanger.chat("task 的值 是多少，回答我");
    System.out.println(res);
    long startTime = System.currentTimeMillis();
    exchanger.setPreSetMsg("你现在是一位绘图专家，你最擅长的事情就是绘画");
    Message talk = exchanger.chat("告诉我，你最擅长的事情");
    System.out.println(talk);
    long endTime = System.currentTimeMillis();
    long elapsedTime = endTime - startTime;
    System.out.println("Elapsed Time: " + elapsedTime / 1000 + " seconds");

    talk = exchanger.chat("介绍一下你自己");
    System.out.println(talk);

    List<Message> msgs = exchanger.getMsgs();
    System.out.println("msgs: " + msgs);

    Message lastAnswer = exchanger.getLastAnswer();
    System.out.println("lastAnswer: " + lastAnswer);

    exchanger.clearMsg();

    Message talk1 = exchanger.chat("你好，介绍一下你自己");
    System.out.println(talk1);

    List<Message> msgs1 = exchanger.getMsgs();
    System.out.println(msgs1);
    System.out.println(msgs1.size());
    exchanger.clearPreSet();
  }

}
```

</details>

##### 通义千问测试类
<details>
  <summary>通义千问测试类</summary>
  
```java
/**
 * @author clov614
 * {@code @date} 2024/1/26 18:46
 */
@SpringBootTest
public class AliAITest {

  @Resource
  private ChatClient chatClient;

  @Resource
  private ChatRecordClient chatRecordClient;

  @Test
  public void test01() {
    // 单次对话
    ChatClient client = chatClient.createClient(false);
    Message msg = client.chat("你好");
    String content = msg.getContent();
    System.out.println(content);

    // 连续对话
    ChatRecordClient client1 = chatRecordClient.createClient(true);
    client1.setPresets("你是一个编程问题小帮手，你的名字叫小艺");
    Message res = client1.chat("你好，请问该怎么称呼你");
    System.out.println(res);
    Message res2 = client1.chat("问一个问题，Java中Queue有哪些常用方法");
    System.out.println(res2);
    List<Message> msgs = client1.getMsgs();
    System.out.println(msgs);
    Message freshMsg = client1.getLast();
    System.out.println(freshMsg);
    Message res3 = client1.chat("刚刚我的问题是啥来着");
    System.out.println(res3);
    client1.clearMsg();
    System.out.println(client1.getMsgs());
  }
}
```

</details>




