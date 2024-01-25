package cn.iaimi.openaisdk;

import cn.iaimi.openaisdk.aisender.Exchanger;
import cn.iaimi.openaisdk.aisender.Sender;
import cn.iaimi.openaisdk.aisender.impl.ExchangerImpl;
import cn.iaimi.openaisdk.aisender.impl.SenderImpl;
import cn.iaimi.openaisdk.api.OpenAiApi;
import cn.iaimi.openaisdk.model.dto.ai.ConfigInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConfigurationProperties("chatai.open-ai")
@Data
@ComponentScan
public class OpenAiSdkConfig {

    /**
     * secretKey (必填)
     */
    private String openAiApiKey;

    /**
     * 接口地址
     * 默认= https://api.openai.com/v1/chat/completions
     */
    private String url = "https://api.openai.com/v1/chat/completions";

    // region 代理配置

    /**
     * 代理地址 (选填)
     */
    private String proxyHost;

    /**
     * 代理端口 (选填)
     */
    private Integer proxyPort;

    // endregion

    /**
     * 请求的ai模型 (选填)
     * 默认:
     */
    private String model = "gpt-3.5-turbo";

    /**
     * 历史消息最大保存条数
     */
    private int msgMaxSize = 20;


    @Resource
    private OpenAiApi openAiApi;

    @Bean
    public Sender openAiSender() {
        return new SenderImpl(getConfigInfo(), openAiApi);
    }

    @Bean
    public Exchanger openAiExchanger() {
        return new ExchangerImpl(msgMaxSize, getConfigInfo());
    }


    private ConfigInfo getConfigInfo() {
        return new ConfigInfo(openAiApiKey, url, proxyHost, proxyPort, model);
    }

}
