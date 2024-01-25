package cn.iaimi.openaisdk.model.dto.ai;

import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/11/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatCompletionRequest {

    @Alias("model")
    private String model;

    @Alias("messages")
    private List<Message> messages;
}
