# Actual dynamic problem solving

This is an implementation of a DYNAMIS-like approach to dynamic problem solving. It creates an instance of a `Problem` which is a linear structural equation. A problem has some number of input nodes, some number of output nodes, and a set of relationships between them. Given control of the inputs, and the values of the outputs, can you determine the relationships between them?

## Problem parameters
When creating a new problem the following can be set to tune the difficulty.
* `inputNotes` The number of user controlled inputs
* `outputNodes` The number of outputs that will be exposed.
* `hiddenNodes` The number of hidden variables in the network. Beware: these are insidious.
* `relationships` How many relationships should exist in the network. Input nodes never have ingress relationships.
* `allowSelfReference` Default = true. Can a node have a relationship with itself?

## Pastables + Examples

Creating a new problem with 2 inputs, 2 outputs, and 4 relationship edges. Relationships may have the same node for source and target. No hidden nodes.
```
curl -vX POST http://localhost:8080/problems -d @request.json --header "Content-Type: application/json"

request.json
{
  "inputNodes" : 2,
  "outputNodes" : 2,
  "relationships" : 4,
  "allowSelfReference" : true
}
```

Get the graphviz DOT file for a given problem (essentially the solution).
```
curl http://localhost:8080/problems/{id}/dot
```

Get the current data points for a problem.
```
curl http://localhost:8080/problems/{id}
```

Advance the current problem one step without modifying inputs.
```
curl -vX POST http://localhost:8080/problems/{id}/next --header "Content-Type: application/json"
```

Advance the current problem with specified inputs. Inputs are all named `Input_` with zero-indexed names. Any unnamed inputs will be left at their previous value. If a variable isn't found it will raise an exception.
```
curl -vX POST http://localhost:8080/problems/{id}/next -d @values.json --header "Content-Type: application/json"

values.json
{
  "Input_0": 0.0
}
```