package me.squanderingti.dynamicproblems.problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProblemBuilder {
    String name;

    public Integer inputNodes;
    public Integer outputNodes;
    public Integer hiddenNodes;
    public Integer relationships;

    Boolean allowSelfReference;

    protected Random random;

    public ProblemBuilder() {
        random = new Random();
        inputNodes = 0;
        outputNodes = 0;
        hiddenNodes = 0;
        relationships = 0;
    }

    protected Double initialValue() {
        return (double) random.nextInt(6) + 1;
    }

    public Problem buildProblem() {
        Problem p = new Problem();
        p.name = name;

        List<Node> inputNodeList = new ArrayList<>(inputNodes);
        for(int i  = 0; i < inputNodes; i++) {
            Node n = new InputNode();
            n.name = "Input_" + i;
            n.initialValue = initialValue();
            n.valuesAtTime.add(n.initialValue);
            inputNodeList.add(n);
        }

        List<Node> outputNodeList = new ArrayList<>(outputNodes);
        for(int i = 0; i < outputNodes; i++) {
            Node n = new OutputNode();
            n.name = "Output_" + i;
            n.initialValue = initialValue();
            n.valuesAtTime.add(n.initialValue);
            outputNodeList.add(n);
        }

        List<Node> hiddenNodeList = new ArrayList<>(hiddenNodes);
        for(int i = 0; i < hiddenNodes; i++) {
            Node n = new HiddenNode();
            n.name = "Hidden_" + i;
            n.initialValue = initialValue();
            n.valuesAtTime.add(n.initialValue);
            hiddenNodeList.add(n);
        }

        List<Node> combinedPossibleInputs = new ArrayList<>();
        combinedPossibleInputs.addAll(inputNodeList);
        combinedPossibleInputs.addAll(hiddenNodeList);
        combinedPossibleInputs.addAll(outputNodeList);

        List<Node> combinedPossibleOutputs = new ArrayList<>();
        combinedPossibleOutputs.addAll(hiddenNodeList);
        combinedPossibleOutputs.addAll(outputNodeList);
        for(int i = 0; i < relationships; i++) {
            // Choose a random feasible input
            int idxInput = random.nextInt(combinedPossibleInputs.size());
            int idxOutput = random.nextInt(combinedPossibleOutputs.size());

            Relationship r = new Relationship();
            Node inputNode = combinedPossibleInputs.get(idxInput);
            Node outputNode = combinedPossibleOutputs.get(idxOutput);
            r.target = outputNode;
            r.targetName = outputNode.name;
            r.operand = (double) (random.nextInt(5) + 1);
            switch(random.nextInt(4)) {
                case 0:
                    r.operation = Relationship.Operation.ADD;
                    break;
                case 1:
                    r.operation = Relationship.Operation.SUBTRACT;
                    break;
                case 2:
                    r.operation = Relationship.Operation.MULTIPLY;
                    break;
                case 3:
                    r.operation = Relationship.Operation.DIVIDE;
                    break;
                default:
                    throw new IllegalStateException("Invalid random");
            }
            inputNode.relationships.add(r);
        }

        p.nodeList.addAll(inputNodeList);
        p.nodeList.addAll(outputNodeList);
        p.nodeList.addAll(hiddenNodeList);

        return p;
    }
}
