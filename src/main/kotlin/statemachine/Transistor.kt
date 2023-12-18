package statemachine

import mu.KotlinLogging

class Transistor<I: Input, SD: SharedData>(private val stateMapping: Map<State<SD>, Set<Transition<I, SD>>>, firstState: State<SD>, private var sharedData: SD) {
    private val logger = KotlinLogging.logger {}
    private val initialState = InitialState(firstState)
    private var currentState: State<SD> = initialState.nextState

    fun getNextState(input: I): State<SD> {
        val transitions = stateMapping[currentState]
        return if (transitions != null) {
            logger.debug { "Calling exit function of ${currentState.getName()}" }
            currentState.exit(sharedData)
            currentState = transitions.first { it.shouldChangeState(input, sharedData) }.getNextState()
            logger.debug { "Calling entry function of ${currentState.getName()}" }
            currentState.entry(sharedData)
            return currentState
        } else currentState
    }

    fun getCurrentState(): State<SD> = currentState
    fun isInFinalState(): Boolean = currentState is FinalState
}