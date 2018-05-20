package me.squanderingti.dynamicproblems.problems;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemTest {
    protected Problem basicProblemSetup() {
        Problem p = new Problem();
        p.name = "test test";

        InputNode input = new InputNode();
        input.name = "input";
        input.initialValue = Double.valueOf(1.0);
        input.valuesAtTime.add(Double.valueOf(1.0));

        OutputNode output = new OutputNode();
        output.name = "output";
        output.initialValue = Double.valueOf(0.0);
        output.valuesAtTime.add(Double.valueOf(0.0));

        Relationship r = new Relationship();
        r.operation = Relationship.Operation.ADD;
        r.operand = 2.0;
        r.target = output;
        r.targetName = "output";

        input.relationships.add(r);

        Relationship r1 = new Relationship();
        r1.operation = Relationship.Operation.MULTIPLY;
        r1.operand = 1.5;
        r1.target = output;
        output.relationships.add(r1);

        p.nodeList.add(input);
        p.nodeList.add(output);
        return p;
    }

    @Test
    public void testBasicProblem() throws Exception {
        Problem p = basicProblemSetup();

        p.calculateNextStep();
        p.calculateNextStep();
        p.calculateNextStep();

        List<Node> outs = p.getOuputNodes();
        Assert.assertEquals(outs.size(), 1);

        Double expectedIns[] = {1.0, 1.0, 1.0, 1.0};
        Node input = p.getInputNodes().get(0);
        Assert.assertArrayEquals(expectedIns, input.valuesAtTime.toArray());

        Double expectedOuts[] = {0.0, 3.0, 7.5, 14.25};
        Assert.assertArrayEquals(expectedOuts, outs.get(0).valuesAtTime.toArray());
    }

    @Test
    public void specifyNextStep() {
        Problem p = basicProblemSetup();
        Map<String, Double> inputs = new HashMap<>();
        inputs.put("input", 9.0);

        p.applyInputs(inputs); // Auto calls next step

        List<Node> outs = p.getOuputNodes();
        Node input = p.getInputNodes().get(0);

        Double expectedIns[] = {1.0, 9.0};
        Assert.assertArrayEquals(expectedIns, input.valuesAtTime.toArray());

        Double expectedOuts[] = {0.0, 11.0};
        Assert.assertArrayEquals(expectedOuts, outs.get(0).valuesAtTime.toArray());
    }

    @Test
    public void unspecifiedNextStep() {
        Problem p = basicProblemSetup();

        p.calculateNextStep();

        List<Node> outs = p.getOuputNodes();
        Node input = p.getInputNodes().get(0);

        Double expectedIns[] = {1.0, 1.0};
        Assert.assertArrayEquals(expectedIns, input.valuesAtTime.toArray());

        Double expectedOuts[] = {0.0, 3.0};
        Assert.assertArrayEquals(expectedOuts, outs.get(0).valuesAtTime.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyInputsToNonExistingNodes() {
        Problem p = basicProblemSetup();
        Map<String, Double> inputs = new HashMap<>();
        inputs.put("not there", 9.0);

        p.applyInputs(inputs);
    }

    @Test
    public void reconnectRelationships() {
        Problem p = basicProblemSetup();
        p.getInputNodes().get(0).relationships.get(0).target = null;
        p.reconnectRelationships();
        Assert.assertNotNull(p.getInputNodes().get(0).relationships.get(0).target);
    }
}
