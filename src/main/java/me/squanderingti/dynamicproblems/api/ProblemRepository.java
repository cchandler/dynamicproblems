package me.squanderingti.dynamicproblems.api;

import me.squanderingti.dynamicproblems.problems.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemRepository extends CrudRepository<Problem, String> {
}
