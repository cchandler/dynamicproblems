package me.squanderingti.dynamicproblems.api;

import java.util.Map;

/**
 * While a problem is being worked on we only return a very limited
 * view of the information.
 */
public class ProblemDescriptionResponse {
    public Integer inputCount;
    public Integer outputCount;
    public Map<String, Double[]> outputValues;
}
