package me.squanderingti.dynamicproblems;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ProblemTest {
    @Test
    public void testBasicProblem() throws Exception {
        Problem p = new Problem();
        p.name = "test test";

        InputNode input = new InputNode();
        input.initialValue = Double.valueOf(1.0);
        input.valuesAtTime.add(Double.valueOf(1.0));

        OutputNode output = new OutputNode();
        output.initialValue = Double.valueOf(0.0);
        output.valuesAtTime.add(Double.valueOf(0.0));

        Relationship r = new Relationship();
        r.operation = Relationship.Operation.ADD;
        r.operand = 2.0;
        r.target = output;

        input.relationships.add(r);

        Relationship r1 = new Relationship();
        r1.operation = Relationship.Operation.MULTIPLY;
        r1.operand = 1.5;
        r1.target = output;
        output.relationships.add(r1);

        p.nodeList.add(input);
        p.nodeList.add(output);

        p.calculateNextStep();
        p.calculateNextStep();
        p.calculateNextStep();

        List<Node> outs = p.getOuputNodes();
        Assert.assertEquals(outs.size(), 1);

        Double expectedIns[] = {1.0, 1.0, 1.0, 1.0};
        Assert.assertArrayEquals(expectedIns, input.valuesAtTime.toArray());

        Double expectedOuts[] = {0.0, 3.0, 7.5, 14.25};
        Assert.assertArrayEquals(expectedOuts, outs.get(0).valuesAtTime.toArray());
    }
}
