package me.squanderingti.dynamicproblems.problems;

import org.junit.Assert;
import org.junit.Test;

public class ProblemDrawerTest {
    @Test
    public void drawDOTTest() {
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

        input.relationships.add(r);

        Relationship r1 = new Relationship();
        r1.operation = Relationship.Operation.MULTIPLY;
        r1.operand = 1.5;
        r1.target = output;
        output.relationships.add(r1);

        p.nodeList.add(input);
        p.nodeList.add(output);

        ProblemDrawer pd = new ProblemDrawer(p);
        String result = pd.drawDOT();
        String expected = "digraph {\n" +
                "\"input[1.0]\" -> \"output[0.0]\" [ label=\" ADD 2.0\" ];\n" +
                "\"output[0.0]\" -> \"output[0.0]\" [ label=\" MULTIPLY 1.5\" ];\n" +
                "}";
        Assert.assertEquals(expected, result);
    }
}
