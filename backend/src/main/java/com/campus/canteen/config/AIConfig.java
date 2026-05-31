package com.campus.canteen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ai")
public class AIConfig {
    private AlibabaConfig alibaba = new AlibabaConfig();
    private boolean enabled = true;

    @Data
    public static class AlibabaConfig {
        /** 百炼 API Key（sk- 开头），支持 yaml 直填或环境变量 DASHSCOPE_API_KEY / ALIYUN_API_KEY */
        private String apiKey;
        /** OpenAI 兼容模式完整地址，需包含 /chat/completions */
        private String apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
        private String model = "qwen-plus-2025-07-28";
        /** API 调用失败时是否回退模拟数据；配置了 Key 时建议 false */
        private boolean fallbackOnError = false;
    }

    public boolean isApiKeyConfigured() {
        return alibaba != null
                && alibaba.getApiKey() != null
                && !alibaba.getApiKey().trim().isEmpty();
    }
}
