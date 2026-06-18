package com.campus.canteen.service;

import com.campus.canteen.config.QwenConfig;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.SalesPrediction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Qwen AI预测服务
 */
@Service
public class QwenPredictionService {

    @Autowired
    private QwenConfig qwenConfig;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 使用Qwen AI预测菜品销量
     * @param dishes 菜品列表
     * @param targetDate 目标日期（星期几）
     * @param historicalData 历史数据摘要
     * @return 每个菜品的预测销量
     */
    public Map<Long, Integer> predictWithQwen(List<Dish> dishes, String targetDate, String historicalData) {
        Map<Long, Integer> predictions = new HashMap<>();

        // 如果Qwen未启用或无API密钥，返回空MAP（将使用规则方法）
        if (!qwenConfig.isEnabled() || qwenConfig.getApiKey() == null || 
            qwenConfig.getApiKey().trim().isEmpty()) {
            System.out.println("Qwen AI未启用或未配置API密钥，将使用规则预测方法");
            return predictions;
        }

        try {
            // 构建提示词
            String prompt = buildPredictionPrompt(dishes, targetDate, historicalData);
            
            // 调用Qwen API
            String response = callQwenApi(prompt);
            
            // 解析响应
            predictions = parseQwenResponse(response, dishes);
            
            System.out.println("Qwen AI预测成功，生成了 " + predictions.size() + " 条预测");
        } catch (Exception e) {
            System.err.println("Qwen AI预测失败: " + e.getMessage());
            e.printStackTrace();
        }

        return predictions;
    }

    /**
     * 使用Qwen AI生成备餐建议
     * @param predictions 预测结果列表
     * @return 备餐建议（菜品ID -> 建议备餐量）
     */
    public Map<Long, PreparationAdvice> generateSuggestionsWithQwen(List<SalesPrediction> predictions) {
        Map<Long, PreparationAdvice> suggestions = new HashMap<>();

        // 如果Qwen未启用或无API密钥，返回空MAP
        if (!qwenConfig.isEnabled() || qwenConfig.getApiKey() == null || 
            qwenConfig.getApiKey().trim().isEmpty()) {
            System.out.println("Qwen AI未启用或未配置API密钥，将使用规则生成备餐建议");
            return suggestions;
        }

        try {
            // 构建提示词
            String prompt = buildSuggestionPrompt(predictions);
            
            // 调用Qwen API
            String response = callQwenApi(prompt);
            
            // 解析响应
            suggestions = parseSuggestionResponse(response, predictions);
            
            System.out.println("Qwen AI备餐建议生成成功，生成了 " + suggestions.size() + " 条建议");
        } catch (Exception e) {
            System.err.println("Qwen AI备餐建议生成失败: " + e.getMessage());
            e.printStackTrace();
        }

        return suggestions;
    }

    /**
     * 构建预测提示词
     */
    private String buildPredictionPrompt(List<Dish> dishes, String targetDate, String historicalData) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("你是校园食堂的智能预测助手。请根据以下信息预测明天各菜品的销量。\n\n");
        prompt.append("目标日期: ").append(targetDate).append("\n\n");
        
        prompt.append("菜品列表:\n");
        for (Dish dish : dishes) {
            prompt.append("- ").append(dish.getName())
                  .append(" (价格: ¥").append(dish.getPrice()).append(")\n");
        }
        
        if (historicalData != null && !historicalData.isEmpty()) {
            prompt.append("\n历史销售数据摘要:\n").append(historicalData).append("\n");
        }
        
        prompt.append("\n请根据食堂运营规律和历史数据，预测每个菜品的明天销量。");
        prompt.append("\n返回格式为JSON，格式如下：\n");
        prompt.append("{\n");
        prompt.append("  \"predictions\": [\n");
        prompt.append("    {\"dishName\": \"菜品名称\", \"predictedSales\": 预测销量, \"confidence\": 0.0-1.0, \"reason\": \"预测理由\"},\n");
        prompt.append("    ...\n");
        prompt.append("  ]\n");
        prompt.append("}\n");
        prompt.append("注意：预测销量应该是整数，基于菜品类型、定价、季节性等因素综合判断。");
        
        return prompt.toString();
    }

    /**
     * 构建备餐建议提示词
     */
    private String buildSuggestionPrompt(List<SalesPrediction> predictions) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("你是校园食堂的智能备餐顾问。请根据以下预测结果生成备餐建议。\n\n");
        prompt.append("预测结果:\n");
        for (SalesPrediction pred : predictions) {
            prompt.append("- ").append(pred.getDishName())
                  .append(": 预测销量=").append(pred.getPredictedSales())
                  .append(", 置信度=").append(pred.getConfidence())
                  .append(", 价格=¥").append(pred.getPrice()).append("\n");
        }
        
        prompt.append("\n请根据预测销量和置信度，为每个菜品生成备餐建议。");
        prompt.append("\n考虑因素：");
        prompt.append("\n1. 预测销量越高，备餐量应越多");
        prompt.append("\n2. 置信度越低，应适当增加保险量");
        prompt.append("\n3. 需要给出最小、建议、最大备餐量");
        prompt.append("\n4. 根据销量给出优先级（高/中/低）");
        prompt.append("\n\n返回格式为JSON，格式如下：\n");
        prompt.append("{\n");
        prompt.append("  \"suggestions\": [\n");
        prompt.append("    {\"dishName\": \"菜品名称\", \"minQuantity\": 最小备餐量, \"recommendedQuantity\": 建议备餐量, \"maxQuantity\": 最大备餐量, \"priority\": \"high|medium|low\", \"reason\": \"建议理由\"},\n");
        prompt.append("    ...\n");
        prompt.append("  ]\n");
        prompt.append("}\n");
        
        return prompt.toString();
    }

    /**
     * 调用Qwen API
     */
    private String callQwenApi(String prompt) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + qwenConfig.getApiKey());
        headers.set("Accept", "application/json");
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", qwenConfig.getModel());
        
        Map<String, Object> input = new HashMap<>();
        input.put("prompt", prompt);
        requestBody.put("input", input);
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 0.7);
        parameters.put("max_tokens", 2000);
        requestBody.put("parameters", parameters);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            qwenConfig.getApiUrl(),
            HttpMethod.POST,
            entity,
            String.class
        );
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new Exception("Qwen API调用失败: " + response.getStatusCode());
        }
    }

    /**
     * 解析Qwen预测响应
     */
    private Map<Long, Integer> parseQwenResponse(String response, List<Dish> dishes) {
        Map<Long, Integer> predictions = new HashMap<>();
        
        try {
            // 解析Qwen API响应
            JsonNode root = objectMapper.readTree(response);
            
            // 不同API版本的响应结构可能不同，尝试多种路径
            JsonNode outputNode = root.has("output") ? root.get("output") : root;
            JsonNode choicesNode = outputNode.has("choices") ? outputNode.get("choices") : null;
            
            String content = null;
            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                content = firstChoice.has("text") ? firstChoice.get("text").asText() : 
                          firstChoice.has("message") && firstChoice.get("message").has("content") 
                              ? firstChoice.get("message").get("content").asText() : null;
            } else if (outputNode.has("text")) {
                content = outputNode.get("text").asText();
            } else if (outputNode.has("result")) {
                content = outputNode.get("result").asText();
            }
            
            if (content != null && !content.isEmpty()) {
                // 清理可能的markdown格式
                content = content.replace("```json", "").replace("```", "").trim();
                
                // 解析预测JSON
                JsonNode resultRoot = objectMapper.readTree(content);
                JsonNode predictionsArray = resultRoot.get("predictions");
                
                if (predictionsArray != null && predictionsArray.isArray()) {
                    // 构建菜品名称到ID的映射
                    Map<String, Long> dishNameToId = new HashMap<>();
                    for (Dish dish : dishes) {
                        dishNameToId.put(dish.getName(), dish.getDishId());
                    }
                    
                    // 解析每个预测
                    for (JsonNode predictionNode : predictionsArray) {
                        String dishName = predictionNode.has("dishName") ? 
                            predictionNode.get("dishName").asText() : null;
                        int predictedSales = predictionNode.has("predictedSales") ? 
                            predictionNode.get("predictedSales").asInt() : 0;
                        
                        if (dishName != null && dishNameToId.containsKey(dishName) && predictedSales > 0) {
                            Long dishId = dishNameToId.get(dishName);
                            predictions.put(dishId, predictedSales);
                            System.out.println("Qwen AI预测: " + dishName + " -> " + predictedSales + "份");
                        }
                    }
                }
            }
            
            // 如果AI返回了部分结果，返回这些结果
            if (!predictions.isEmpty()) {
                return predictions;
            }
            
        } catch (Exception e) {
            System.err.println("解析Qwen响应失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 如果解析失败或没有AI结果，返回空MAP，调用方会使用规则方法
        return predictions;
    }

    /**
     * 解析Qwen备餐建议响应
     */
    private Map<Long, PreparationAdvice> parseSuggestionResponse(String response, List<SalesPrediction> predictions) {
        Map<Long, PreparationAdvice> suggestions = new HashMap<>();
        
        try {
            // 解析Qwen API响应
            JsonNode root = objectMapper.readTree(response);
            
            JsonNode outputNode = root.has("output") ? root.get("output") : root;
            JsonNode choicesNode = outputNode.has("choices") ? outputNode.get("choices") : null;
            
            String content = null;
            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                content = firstChoice.has("text") ? firstChoice.get("text").asText() : 
                          firstChoice.has("message") && firstChoice.get("message").has("content") 
                              ? firstChoice.get("message").get("content").asText() : null;
            } else if (outputNode.has("text")) {
                content = outputNode.get("text").asText();
            } else if (outputNode.has("result")) {
                content = outputNode.get("result").asText();
            }
            
            if (content != null && !content.isEmpty()) {
                content = content.replace("```json", "").replace("```", "").trim();
                
                JsonNode resultRoot = objectMapper.readTree(content);
                JsonNode suggestionsArray = resultRoot.get("suggestions");
                
                if (suggestionsArray != null && suggestionsArray.isArray()) {
                    Map<String, Long> dishNameToId = new HashMap<>();
                    for (SalesPrediction pred : predictions) {
                        dishNameToId.put(pred.getDishName(), pred.getDishId());
                    }
                    
                    for (JsonNode suggestionNode : suggestionsArray) {
                        String dishName = suggestionNode.has("dishName") ? 
                            suggestionNode.get("dishName").asText() : null;
                        
                        if (dishName != null && dishNameToId.containsKey(dishName)) {
                            Long dishId = dishNameToId.get(dishName);
                            PreparationAdvice advice = new PreparationAdvice();
                            
                            advice.setMinQuantity(suggestionNode.has("minQuantity") ? 
                                suggestionNode.get("minQuantity").asInt() : 5);
                            advice.setRecommendedQuantity(suggestionNode.has("recommendedQuantity") ? 
                                suggestionNode.get("recommendedQuantity").asInt() : 10);
                            advice.setMaxQuantity(suggestionNode.has("maxQuantity") ? 
                                suggestionNode.get("maxQuantity").asInt() : 15);
                            advice.setPriority(suggestionNode.has("priority") ? 
                                suggestionNode.get("priority").asText() : "medium");
                            
                            suggestions.put(dishId, advice);
                            System.out.println("Qwen AI备餐建议: " + dishName + " -> " + advice.getRecommendedQuantity() + "份");
                        }
                    }
                }
            }
            
            if (!suggestions.isEmpty()) {
                return suggestions;
            }
            
        } catch (Exception e) {
            System.err.println("解析Qwen备餐建议响应失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return suggestions;
    }

    /**
     * 生成历史数据摘要
     */
    public String generateHistoricalSummary(Map<Long, Integer> dishSales, Map<Integer, Integer> weeklyPattern) {
        if (dishSales == null || dishSales.isEmpty()) {
            return "暂无历史数据";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("各菜品总销量:\n");
        dishSales.forEach((dishId, sales) -> 
            summary.append("- 菜品ID ").append(dishId).append(": ").append(sales).append("份\n"));
        
        if (weeklyPattern != null && !weeklyPattern.isEmpty()) {
            summary.append("\n星期销量模式:\n");
            String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
            for (int i = 1; i <= 7; i++) {
                if (weeklyPattern.containsKey(i)) {
                    summary.append("- ").append(days[i-1]).append(": ")
                          .append(weeklyPattern.get(i)).append("份\n");
                }
            }
        }
        
        return summary.toString();
    }

    /**
     * 备餐建议内部类
     */
    public static class PreparationAdvice {
        private Integer minQuantity;
        private Integer recommendedQuantity;
        private Integer maxQuantity;
        private String priority;

        public Integer getMinQuantity() { return minQuantity; }
        public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }
        public Integer getRecommendedQuantity() { return recommendedQuantity; }
        public void setRecommendedQuantity(Integer recommendedQuantity) { this.recommendedQuantity = recommendedQuantity; }
        public Integer getMaxQuantity() { return maxQuantity; }
        public void setMaxQuantity(Integer maxQuantity) { this.maxQuantity = maxQuantity; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }
}
