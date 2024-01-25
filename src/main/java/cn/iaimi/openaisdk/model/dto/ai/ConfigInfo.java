package cn.iaimi.openaisdk.model.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author clov614
 * {@code @date} 2024/1/25 13:04
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfigInfo {

    private String openAiApiKey;
    private String url;
    private String proxyHost;
    private Integer proxyPort;
    private String aiModel;
}
