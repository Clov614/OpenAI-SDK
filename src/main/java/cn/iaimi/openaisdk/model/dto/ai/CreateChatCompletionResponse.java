package cn.iaimi.openaisdk.model.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2023/11/14
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CreateChatCompletionResponse {

    /**
     * id : chatcmpl-8KnNAZQ43y8teJnxyDv2QB37vzAQs
     * object : chat.completion
     * created : 1699967040
     * model : gpt-3.5-turbo-0613
     * choices : [{"index":0,"message":{"role":"assistant","content":"你好！感谢您发来的测试消息。如果您有任何问题或需要帮助，请随时告诉我。"},"finish_reason":"stop"}]
     * usage : {"prompt_tokens":16,"completion_tokens":34,"total_tokens":50}
     * error : {"message":"Rate limit reached for gpt-3.5-turbo in xx: Limit 3, Used 3, Requested 1. Please try again in 20s. .."}
     */

    private String id;
    private String object;
    private Integer created;
    private String model;
    private UsageBean usage;
    private List<ChoicesBean> choices;
    private ErrorBean error;


    @Override
    public String toString() {
        return "CreateChatCompletionResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", usage=" + usage +
                ", choices=" + choices +
                ", error=" + error +
                '}';
    }

    @NoArgsConstructor
    @Data
    public static class UsageBean {
        /**
         * prompt_tokens : 16
         * completion_tokens : 34
         * total_tokens : 50
         */

        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
    }

    @NoArgsConstructor
    @Data
    public static class ChoicesBean {
        /**
         * index : 0
         * message : {"role":"assistant","content":"你好！感谢您发来的测试消息。如果您有任何问题或需要帮助，请随时告诉我。"}
         * finish_reason : stop
         */

        private Integer index;
        private MessageBean message;
        private String finish_reason;

        @EqualsAndHashCode(callSuper = true) // 继承父类的 equals和hashcode方法
        @NoArgsConstructor
        @Data
        public static class MessageBean extends Message {
            /**
             * role : assistant
             * content : 你好！感谢您发来的测试消息。如果您有任何问题或需要帮助，请随时告诉我。
             */

            private String role;
            private String content;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ErrorBean {
        /**
         * message : Rate limit reached for gpt-3.5-turbo in organization org-TqBpmK8YhGr4NGBw..
         * type : requests
         * param : null
         * code : rate_limit_exceeded
         */

        private String message;
        private String type;
        private Object param;
        private String code;
    }
}
