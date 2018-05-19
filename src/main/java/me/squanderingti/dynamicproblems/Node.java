package me.squanderingti.dynamicproblems;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.LinkedList;
import java.util.List;

/**
 * A node in a dynamic problem.
 */
@RedisHash("Node")
public abstract class Node {
    @Id
    public String id;

    List<Double> valuesAtTime;
    Double initialValue;

    List<Double> mailbox;
    List<Relationship> relationships;

    public Node() {
        valuesAtTime = new LinkedList<>();
        mailbox = new LinkedList<>();
        relationships = new LinkedList<>();
    }

    public Double getLastValue() {
        return valuesAtTime.get(valuesAtTime.size() - 1);
    }

    public void applyRelationshipsForTime(Integer timeStep) {
        for(Relationship r : relationships) {
            r.apply(getLastValue());
        }
    }

    public void processMailbox(Integer timeStep) {
        if(mailbox.isEmpty()) {
            // Copy the last value since the mailbox was empty.
            Double lastValue = valuesAtTime.get(valuesAtTime.size() - 1);
            valuesAtTime.add(lastValue);
        } else {
            Double result = 0.0;
            for(Double d : mailbox) {
                result += d;
            }
            valuesAtTime.add(result);
            mailbox.clear();
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", valuesAtTime=" + valuesAtTime +
                ", initialValue=" + initialValue +
                ", mailbox=" + mailbox +
                ", relationships=" + relationships +
                "}\n";
    }
}
