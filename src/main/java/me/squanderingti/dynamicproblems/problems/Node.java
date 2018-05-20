package me.squanderingti.dynamicproblems.problems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import java.util.LinkedList;
import java.util.List;

/**
 * A node in a dynamic problem.
 */
@RedisHash("Node")
public abstract class Node {
    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Id
    public String id;

    public String name;

    public List<Double> valuesAtTime;
    public Double initialValue;

    protected List<Double> mailbox;
    public List<Relationship> relationships;

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
        if(this instanceof InputNode) {
            // Input nodes shouldn't have incoming relationships
            if(!mailbox.isEmpty()) {
                throw new IllegalStateException("Input nodes shouldn't have incoming relationships!");
            } else {
                // A step was calculated but an input was unspecified.
                // Repeat the last value.
                if(valuesAtTime.size() <= timeStep) {
                    log.debug("An additional input was unspecified on [{}]. Repeating previous [{}].", name, getLastValue());
                    Double lastValue = getLastValue();
                    valuesAtTime.add(lastValue);
                }
            }
            return;
        }

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

    public String graphName() {
        return "\"" + name + "[" + initialValue + "]\"";
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", valuesAtTime=" + valuesAtTime +
                ", initialValue=" + initialValue +
                ", mailbox=" + mailbox +
                ", relationships=" + relationships +
                "}\n";
    }
}
