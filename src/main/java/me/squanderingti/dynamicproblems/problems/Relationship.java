package me.squanderingti.dynamicproblems.problems;

import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;

@RedisHash("Relationship")
public class Relationship {
    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    public Operation operation;
    public Double operand;
    public String targetName;
    @Transient public Node target;

    public void apply(@NotNull Double sourceValue) {
        Double result;
        switch(operation) {
            case ADD:
                result = sourceValue + operand;
                break;
            case SUBTRACT:
                result = sourceValue - operand;
                break;
            case MULTIPLY:
                result = sourceValue * operand;
                break;
            case DIVIDE:
                result = sourceValue / operand;
                break;
            default:
                throw new IllegalStateException("Unknown operand.");
        }

        target.mailbox.add(result);
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "operation=" + operation +
                ", operand=" + operand +
                ", target=" + target.name +
                '}';
    }
}
