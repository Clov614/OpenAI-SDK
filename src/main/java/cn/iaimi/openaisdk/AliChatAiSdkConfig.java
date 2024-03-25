package cn.iaimi.openaisdk;

import cn.iaimi.openaisdk.aisender.alibaba.ChatClient;
import cn.iaimi.openaisdk.aisender.alibaba.ChatRecordClient;
import cn.iaimi.openaisdk.aisender.alibaba.impl.ChatClientContinuousImpl;
import cn.iaimi.openaisdk.aisender.alibaba.impl.ChatClientImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("chatai.alibaba")
@Data
@ComponentScan
public class AliChatAiSdkConfig {

    /**
     * 百炼开放平台 apiKey
     */
    private String apiKey;

    /**
     * 使用的模型
     */
    private String useModel;

    /**
     * 历史消息最大保存条数 (选填)
     */
    private int msgMaxSize = 10;


    @Bean
    public ChatClient chatClient() {
        return new ChatClientImpl();
    }

    @Bean
    public ChatRecordClient chatRecordClient() {
        return new ChatClientContinuousImpl();
    }


}
