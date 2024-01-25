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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * openAi 核心请求类
 *
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

}
