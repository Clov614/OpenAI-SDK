package cn.iaimi.openaisdk;

import cn.iaimi.openaisdk.aisender.Exchanger;
import cn.iaimi.openaisdk.aisender.Sender;
import cn.iaimi.openaisdk.api.OpenAiApi;
import cn.iaimi.openaisdk.model.dto.ai.Message;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Tag("excludeThisTest")
class OpenAiSdkConfigTests {

    @Test
    void contextLoads() {
    }

}
