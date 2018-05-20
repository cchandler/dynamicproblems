package me.squanderingti.dynamicproblems.problems;

import org.junit.Assert;
import org.junit.Test;

public class ProblemBuilderTest {
    @Test
    public void testProblemBuilder() throws Exception {
        ProblemBuilder pb = new ProblemBuilder();
        pb.inputNodes = 2;
        pb.outputNodes = 2;
        pb.hiddenNodes = 0;
        pb.relationships = 4;

        Problem p = pb.buildProblem();

        Assert.assertEquals(2, p.getInputNodes().size());
        Assert.assertEquals(2, p.getOuputNodes().size());
    }
}
