package statemachine

import mu.KotlinLogging

class Transistor<I : Input, SD : SharedData>(
    private val stateMapping: Map<State<SD>, Transitions<I, SD>>,
    firstState: State<SD>,
    private var sharedData: SD
) {
    private val logger = KotlinLogging.logger {}
    private val initialState = InitialState(firstState)
    private var currentState: State<SD> = initialState.nextState

    internal fun getNextState(input: I, withErrorStateAsDefault: Boolean = true): State<SD> {
        val transitions = stateMapping[currentState]
        return if (transitions != null) {
            logger.debug { "Calling exit function of ${currentState.getName()}" }
            currentState.exit(sharedData)
            currentState = transitions.nextState(input, sharedData, withErrorStateAsDefault)
            logger.debug { "Calling entry function of ${currentState.getName()}" }
            currentState.entry(sharedData)
            return currentState
        } else currentState
    }

    internal fun getCurrentState(): State<SD> = currentState
    fun isInFinalState(): Boolean = currentState is FinalState
    fun getSharedData(): SD = sharedData
}