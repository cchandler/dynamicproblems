package me.squanderingti.dynamicproblems.problems;

import java.util.List;

/**
 * Draws an instance of a problem using graphviz's DOT language.
 */
public class ProblemDrawer {
    Problem problem;

    public ProblemDrawer(Problem problem) {
        this.problem = problem;
    }

    public String drawDOT() {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph {\n");
        for(Node n : problem.nodeList) {
            List<Relationship> relationships = n.relationships;
            for(Relationship r : relationships) {
                builder.append(n.graphName() + " -> " + r.target.graphName() + " [ label=\" " + r.operation + " " + r.operand + "\" ];\n");
            }
        }
        builder.append("}");

        return builder.toString();
    }
}
