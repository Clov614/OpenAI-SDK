package cn.iaimi.openaisdk.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import cn.iaimi.openaisdk.common.BaseResponse;
import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.common.ResultUtils;
import cn.iaimi.openaisdk.common.ThrowUtils;
import cn.iaimi.openaisdk.model.dto.ai.ConfigInfo;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionRequest;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionResponse;
import cn.iaimi.openaisdk.model.dto.ai.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/11/13
 */
@Component
public class OpenAiApi {

    public BaseResponse<CreateChatCompletionResponse> createChatCompletion(CreateChatCompletionRequest request,
                                                                           ConfigInfo configInfo) {
        String openAiApiKey = configInfo.getOpenAiApiKey();
        String url = configInfo.getUrl();
        String proxyHost = configInfo.getProxyHost();
        Integer proxyPort = configInfo.getProxyPort();
        String model = configInfo.getAiModel();

        request.setModel(model);

        ThrowUtils.throwIf(StringUtils.isAnyBlank(openAiApiKey, url), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(proxyPort == null, ErrorCode.PARAMS_ERROR);

        CreateChatCompletionResponse response = getResponse(request, url, proxyHost, proxyPort, openAiApiKey);
        // 错误处理 (每分钟速率限制3条)
        if (response.getError() != null) {
            return ResultUtils.error(ErrorCode.CHAT_ERROR, response.getError().getCode());
        }
        return ResultUtils.success(response);
    }

    private static CreateChatCompletionResponse getResponse(CreateChatCompletionRequest request, String url, String proxyHost, Integer proxyPort, String openAiApiKey) {
        String result = null;
        String json = JSONUtil.toJsonStr(request);
        if (proxyHost == null || proxyPort == null) {
            result = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .body(json)
                    .execute()
                    .body();
        } else {
            result = HttpRequest.post(url).setHttpProxy(proxyHost, proxyPort)
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .body(json)
                    .execute()
                    .body();
        }
        return JSONUtil.toBean(result, CreateChatCompletionResponse.class);
    }


    public static void main(String[] args) {

        String url = "https://api.openai.com/v1/chat/completions";
        String proxyHost = "127.0.0.1";
        Integer proxyPort = 7890;
        String model = "gpt-3.5-turbo";

        OpenAiApi openAiApi = new OpenAiApi();
        String openAiKey = "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";
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setMessages(Arrays.asList(new Message("user", "你好 这是一条测试消息")));

        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setOpenAiApiKey(openAiKey);
        configInfo.setUrl(url);
        configInfo.setProxyHost(proxyHost);
        configInfo.setProxyPort(proxyPort);
        configInfo.setAiModel(model);

        BaseResponse<CreateChatCompletionResponse> response = openAiApi.createChatCompletion(request, configInfo);
        System.out.println(response.getData().getChoices());
        System.out.println(response.getData().toString());
    }
}
