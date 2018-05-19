package me.squanderingti.dynamicproblems;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.LinkedList;
import java.util.List;

@RedisHash("Problem")
public class Problem {
    @Id public String id;
    public String name;
    public Integer currentTime;

    List<Node> nodeList;

    public Problem() {
        nodeList = new LinkedList<>();
        currentTime = 0;
    }

    public List<Node> getOuputNodes() {
        List<Node> outputs = new LinkedList<>();
        for(Node n : nodeList) {
            if(n instanceof OutputNode) {
                outputs.add(n);
            }
        }
        return outputs;
    }

    public void calculateNextStep() {
        // For every node, apply relationships
        for(Node n : nodeList) {
            n.applyRelationshipsForTime(currentTime + 1);
        }

        // Have every node calculate it's inbox and append.
        for(Node n : nodeList) {
            n.processMailbox(currentTime + 1);
        }

        ++currentTime;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nodeList=" + nodeList +
                '}';
    }
}
