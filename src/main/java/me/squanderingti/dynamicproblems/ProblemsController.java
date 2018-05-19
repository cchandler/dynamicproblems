package me.squanderingti.dynamicproblems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProblemsController {

    @Autowired ProblemRepository problemRepository;

    @GetMapping
    public String index() {
        for(Problem p : problemRepository.findAll()) {
            System.out.println(p);
        }
        return "Problems";
    }

    @PostMapping
    public String createProblem() {
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

        problemRepository.save(p);

        return "Created";
    }
}
