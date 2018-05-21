package me.squanderingti.dynamicproblems.api;

import me.squanderingti.dynamicproblems.problems.Problem;
import me.squanderingti.dynamicproblems.problems.ProblemBuilder;
import me.squanderingti.dynamicproblems.problems.ProblemDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/problems")
public class ProblemsController {

    @Autowired
    ProblemRepository problemRepository;

    @GetMapping
    public String index() {
        for(Problem p : problemRepository.findAll()) {
            System.out.println(p);
        }
        return "Problems";
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ProblemDescriptionResponse> getProblem(@PathVariable(value = "id") String problemId) {
        Optional<Problem> p = problemRepository.findById(problemId);
        if(p.isPresent()) {
            ProblemDescriptionResponse pdr = new ProblemDescriptionResponse();
            pdr.inputCount = p.get().getInputNodes().size();
            pdr.outputCount = p.get().getOuputNodes().size();
            pdr.outputValues = p.get().getAllNormallyVisibleValues();
            return new ResponseEntity<>(pdr, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/dot", produces = MediaType.TEXT_PLAIN)
    public String getDOT(@PathVariable(value = "id") String problemId) {
        Optional<Problem> p = problemRepository.findById(problemId);
        if(p.isPresent()) {
            Problem problem = p.get();
            problem.reconnectRelationships();
            ProblemDrawer pd = new ProblemDrawer(problem);
            return pd.drawDOT();
        }

        return "";
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON)
    public Map<String, String> createProblem(@RequestBody ProblemBuilder problemBuilder) {
        Problem p = problemBuilder.buildProblem();
        // Calculate the first three steps
        p.calculateNextStep();
        p.calculateNextStep();
        p.calculateNextStep();
        problemRepository.save(p);
        return Collections.singletonMap("Created", p.id);
    }

    @PostMapping(value = "/{id}/next", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<ProblemDescriptionResponse> nextStepForProblem(@PathVariable(value="id") String problemId, @RequestBody(required = false) Map<String, Double> nextValues) {
        if(nextValues == null) {
            nextValues = Collections.emptyMap();
        }
        Optional<Problem> p = problemRepository.findById(problemId);
        if(p.isPresent()) {
            Problem problem = p.get();
            problem.reconnectRelationships();
            problem.applyInputs(nextValues);

            problemRepository.save(problem);

            ProblemDescriptionResponse pdr = new ProblemDescriptionResponse();
            pdr.inputCount = problem.getInputNodes().size();
            pdr.outputCount = problem.getOuputNodes().size();
            pdr.outputValues = problem.getAllNormallyVisibleValues();
            return new ResponseEntity<>(pdr, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
