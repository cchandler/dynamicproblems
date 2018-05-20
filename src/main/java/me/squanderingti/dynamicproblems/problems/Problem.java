package me.squanderingti.dynamicproblems.problems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RedisHash("Problem")
public class Problem {
    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final Long ONE_DAY = 86400L;
    public static final Long MAXIMUM_STEPS = 150L;

    @Id public String id;
    public String name;
    public Integer currentTime;

    @TimeToLive  public Long timeToLive;

    public List<Node> nodeList;

    public Problem() {
        nodeList = new LinkedList<>();
        currentTime = 0;
    }

    /**
     * Returns all the input nodes.
     * @return
     */
    public List<Node> getInputNodes() {
        List<Node> inputs = new LinkedList<>();
        for(Node n : nodeList) {
            if(n instanceof InputNode) {
                inputs.add(n);
            }
        }
        return inputs;
    }

    /**
     * Returns all the output nodes.
     * @return
     */
    public List<Node> getOuputNodes() {
        List<Node> outputs = new LinkedList<>();
        for(Node n : nodeList) {
            if(n instanceof OutputNode) {
                outputs.add(n);
            }
        }
        return outputs;
    }

    /**
     * Returns all the values of all the nodes. Includes normally hidden nodes.
     * @return
     */
    public Map<String, Double[]> getAllNodeValues() {
        Map<String, Double[]> outputs = new HashMap<>();
        for(Node n : nodeList) {
            outputs.put(n.name, n.valuesAtTime.toArray(new Double[n.valuesAtTime.size()]));
        }
        return outputs;
    }

    /**
     * Returns all the values of normally visible nodes. This is limited to inputs and outputs. Likely what you want
     * for displaying results to a user actively working.
     * @return
     */
    public Map<String, Double[]> getAllNormallyVisibleValues() {
        Map<String, Double[]> outputs = new HashMap<>();
        for(Node n : nodeList) {
            if(!(n instanceof HiddenNode)) {
                outputs.put(n.name, n.valuesAtTime.toArray(new Double[n.valuesAtTime.size()]));
            }
        }
        return outputs;
    }

    /**
     * Given a Map of inputs (node names to values) append them to the input nodes. Automatically advances
     * the network one time step. Will raise IllegalArgumentException if an unknown node is specified in the inputs.
     * @param inputValues
     */
    public void applyInputs(Map<String, Double> inputValues) {
        List<Node> inputNodes = getInputNodes();
        Map<String, Node> search = new HashMap<String, Node>();
        for(Node n : inputNodes) {
            search.put(n.name, n);
        }

        inputValues.forEach((k,v) -> {
            Node n = search.get(k);
            if(n == null) {
                throw new IllegalArgumentException("Looking for node [" + k + "] but it doesn't exist in this problem.");
            }
            n.valuesAtTime.add(v);
        });

        calculateNextStep();
    }

    /**
     * This is required to be called after loading a problem from Redis. At this time it doesn't
     * appear that redis-data supports graph-like relationships. The result is that we have to mark the
     * relationship on the underlying node as @Transient. This makes everything easier. Is there a callback?
     */
    public void reconnectRelationships() {
        Map<String, Node> nameToNode = new HashMap<String, Node>();
        for(Node n : nodeList) {
            nameToNode.put(n.name, n);
        }
        for(Node n : nodeList) {
            for(Relationship r : n.relationships) {
                r.target = nameToNode.get(r.targetName);
            }
        }
    }

    /**
     * Advance the problem one additional time step.
     */
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
                ", nodeList=\n" + nodeList +
                '}';
    }
}
