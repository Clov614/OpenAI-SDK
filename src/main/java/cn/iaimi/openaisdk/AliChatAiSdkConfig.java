package cn.iaimi.openaisdk;

import cn.iaimi.openaisdk.aisender.alibaba.ChatClient;
import cn.iaimi.openaisdk.aisender.alibaba.ChatRecordClient;
import cn.iaimi.openaisdk.aisender.alibaba.impl.ChatClientContinuousImpl;
import cn.iaimi.openaisdk.aisender.alibaba.impl.ChatClientImpl;
import com.aliyun.broadscope.bailian.sdk.AccessTokenClient;
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
     * AK (必填)
     */
    private String accessKeyId;

    /**
     * SK (必填)
     */
    private String accessKeySecret;


    /**
     * agentKey (必填) 模型应用中获取
     */
    private String agentKey;

    /**
     * appID (必填) 模型应用中获取
     */
    private String appId;

    /**
     * 历史消息最大保存条数 (选填)
     */
    private int msgMaxSize = 20;

    private AccessTokenClient accessTokenClient;


    @Bean
    public AccessTokenClient accessTokenClient() {
        return new AccessTokenClient(accessKeyId, accessKeySecret, agentKey);
    }

    @Bean
    public ChatClient chatClient() {
        return new ChatClientImpl();
    }

    @Bean
    public ChatRecordClient chatRecordClient() {
        return new ChatClientContinuousImpl();
    }


}
