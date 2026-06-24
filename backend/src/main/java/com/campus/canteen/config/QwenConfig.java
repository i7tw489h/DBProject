package com.campus.canteen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Qwen AI配置
 */
@Configuration
@ConfigurationProperties(prefix = "qwen")
public class QwenConfig {
    
    /**
     * 是否启用Qwen AI预测
     */
    private boolean enabled = false;
    
    /**
     * Qwen API密钥
     */
    private String apiKey;
    
    /**
     * Qwen API地址
     */
    private String apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    /**
     * 模型名称
     */
    private String model = "qwen-turbo";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
