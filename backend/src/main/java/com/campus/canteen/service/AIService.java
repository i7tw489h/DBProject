package com.campus.canteen.service;

import com.campus.canteen.config.AIConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);

    @Autowired
    private AIConfig aiConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 调用阿里云百炼通义千问（OpenAI 兼容接口）
     */
    
    public String chat(String prompt) {
        if (!aiConfig.isEnabled()) {
            log.warn("[AIService] AI 未启用(ai.enabled=false)，使用模拟数据");
            return generateMockResponse(prompt);
        }

        if (!aiConfig.isApiKeyConfigured()) {
            log.warn("[AIService] 百炼 API Key 未配置，请设置 ai.alibaba.api-key 或环境变量 DASHSCOPE_API_KEY");
            return generateMockResponse(prompt);
        }

        String url = aiConfig.getAlibaba().getApiUrl();
        String apiKey = aiConfig.getAlibaba().getApiKey().trim();
        String model = aiConfig.getAlibaba().getModel();

        log.info("[AIService] 调用百炼 API, model={}, url={}", model, url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", Arrays.asList(
                    createMessage("user", prompt)
            ));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            String response = restTemplate.postForObject(url, request, String.class);

            String content = parseResponse(response);
            log.info("[AIService] 百炼 API 调用成功, 响应长度={}", content.length());
            return content;
        } catch (HttpStatusCodeException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("[AIService] 百炼 API HTTP 错误 status={}, body={}", e.getStatusCode(), errorBody);
            return handleFailure("HTTP " + e.getStatusCode() + ": " + errorBody, prompt, e);
        } catch (Exception e) {
            log.error("[AIService] 百炼 API 调用异常: {}", e.getMessage(), e);
            return handleFailure(e.getMessage(), prompt, e);
        }
    }

    public Map<String, Object> testConnection() {
        Map<String, Object> result = new HashMap<>();
        result.put("enabled", aiConfig.isEnabled());
        result.put("apiKeyConfigured", aiConfig.isApiKeyConfigured());
        result.put("model", aiConfig.getAlibaba().getModel());
        result.put("apiUrl", aiConfig.getAlibaba().getApiUrl());

        if (!aiConfig.isEnabled()) {
            result.put("success", false);
            result.put("message", "ai.enabled 为 false");
            return result;
        }
        if (!aiConfig.isApiKeyConfigured()) {
            result.put("success", false);
            result.put("message", "未配置 API Key");
            return result;
        }

        try {
            String reply = chat("请只回复：连接成功");
            result.put("success", true);
            result.put("reply", reply);
            result.put("message", "百炼 API 连接正常");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    private Map<String, String> createMessage(String role, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private String handleFailure(String detail, String prompt, Exception e) {
        if (aiConfig.getAlibaba().isFallbackOnError()) {
            log.warn("[AIService] fallback-on-error=true，回退模拟数据");
            return generateMockResponse(prompt);
        }
        throw new RuntimeException("百炼 API 调用失败: " + detail, e);
    }

    private String parseResponse(String response) throws Exception {
        if (response == null || response.trim().isEmpty()) {
            throw new RuntimeException("百炼 API 返回空响应");
        }

        JsonNode root = objectMapper.readTree(response);

        JsonNode error = root.get("error");
        if (error != null && !error.isNull()) {
            String message = error.has("message") ? error.get("message").asText() : error.toString();
            throw new RuntimeException("百炼 API 业务错误: " + message);
        }

        // OpenAI 兼容模式: choices[0].message.content
        JsonNode choices = root.get("choices");
        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode message = choices.get(0).get("message");
            if (message != null && message.has("content") && !message.get("content").isNull()) {
                return message.get("content").asText();
            }
        }

        // 百炼原生格式: output.text
        JsonNode output = root.get("output");
        if (output != null && !output.isNull()) {
            JsonNode text = output.get("text");
            if (text != null && !text.isNull()) {
                return text.asText();
            }
            JsonNode outputChoices = output.get("choices");
            if (outputChoices != null && outputChoices.isArray() && outputChoices.size() > 0) {
                JsonNode message = outputChoices.get(0).get("message");
                if (message != null && message.has("content")) {
                    return message.get("content").asText();
                }
            }
        }

        throw new RuntimeException("无法解析百炼 API 响应: " + response);
    }

    private String generateMockResponse(String prompt) {
        if (prompt.contains("营养") || prompt.contains("健康") || prompt.contains("热量") || prompt.contains("蛋白质")) {
            return "评分：75\n" +
                    "状态：良好\n" +
                    "优点：蛋白质摄入充足；碳水比例合理\n" +
                    "问题：脂肪摄入稍高\n" +
                    "建议：根据您的营养摄入分析，整体饮食较为均衡。建议减少油炸食品摄入，增加蔬菜比例。";
        }
        return "这是一个模拟的AI回复。请配置百炼 API Key 后获得真实建议。";
    }

    public String generateMealRecommendation(String type) {
        String prompt = String.format("请为我推荐一份%s，包含主食、蛋白质、蔬菜，每份菜品请给出热量和营养特点。",
                getMealTypeName(type));
        return chat(prompt);
    }

    private String getMealTypeName(String type) {
        switch (type.toLowerCase()) {
            case "low-calorie":
            case "减脂":
                return "减脂餐";
            case "high-protein":
            case "增肌":
                return "增肌餐";
            case "养胃":
                return "养胃餐";
            case "低糖":
                return "低糖餐";
            case "清淡":
                return "清淡餐";
            default:
                return type;
        }
    }
}
