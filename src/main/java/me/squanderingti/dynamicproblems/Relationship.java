package me.squanderingti.dynamicproblems;

import org.springframework.data.annotation.Reference;

import javax.validation.constraints.NotNull;

public class Relationship {
    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    Operation operation;
    Double operand;
    @Reference Node target;

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
                ", target=" + target +
                '}';
    }
}
