package statemachine.builder.handwritten

import statemachine.*

class TransitionBuilder<I : Input, SD : SharedData>(
    private val forState: State<SD>,
    private val builder: StateMachineBuilder<I, SD>,
) {

    fun addTransitionTo(state: State<SD>, activationCondition: (I, SD) -> Boolean): TransitionBuilder<I, SD> {
        val stateMapping = builder.getMapping()
        val transitions = stateMapping.getOrPut(forState) { mutableSetOf() }
        transitions.add(DefaultTransition(state, activationCondition))
        return this
    }

    fun stateBuilder(): StateMachineBuilder<I, SD> = builder
}