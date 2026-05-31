package com.campus.canteen.entity;

import lombok.Data;
import java.util.List;

@Data
public class HealthEvaluation {
    private int score;
    private String advice;
    private String status;
    private List<String> issues;
    private List<String> positives;
}
