package cn.iaimi.openaisdk.model.dto.ai;

import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/11/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @Alias("role")
    private String role;

    @Alias("content")
    private String content;
}
