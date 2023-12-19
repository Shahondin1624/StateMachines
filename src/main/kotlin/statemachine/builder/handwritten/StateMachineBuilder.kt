package statemachine.builder.handwritten

import mu.KotlinLogging
import statemachine.*

class StateMachineBuilder<I : Input, SD : SharedData> {
    private val logger = KotlinLogging.logger { }
    private var initialState: State<SD>? = null
    private val stateMapping: MutableMap<State<SD>, MutableSet<Transition<I, SD>>> = mutableMapOf()

    fun addFirstState(
        name: String,
        entryFunction: (SD) -> Unit = {},
        exitFunction: (SD) -> Unit = {},
    ): TransitionBuilder<I, SD> =
        addFirstState(DefaultState(name, entryFunction = entryFunction, exitFunction = exitFunction))

    fun addFirstState(state: State<SD>): TransitionBuilder<I, SD> {
        if (initialState == null) {
            initialState = state
            stateMapping[state] = mutableSetOf()
        } else {
            throw RuntimeException("Can not set first state twice ->${state.getName()}")
        }
        return TransitionBuilder(state, this)
    }

    fun getState(name: String): State<SD>? = stateMapping.keys.find { it.getName() == name }

    fun addState(
        name: String,
        entryFunction: (SD) -> Unit = {},
        exitFunction: (SD) -> Unit = {},
    ): TransitionBuilder<I, SD> =
        addState(DefaultState(name, entryFunction = entryFunction, exitFunction = exitFunction))

    fun addState(state: State<SD>): TransitionBuilder<I, SD> {
        stateMapping[state] = mutableSetOf()
        return TransitionBuilder(state, this)
    }

    internal fun getMapping(): MutableMap<State<SD>, MutableSet<Transition<I, SD>>> = stateMapping

    fun finalState(): State<SD> = FinalState.getTypeCasted()

    fun build(sharedData: SD, withErrorStateAsDefault: Boolean = true): StateMachine<I, SD> {
        if (initialState == null) throw IllegalStateException("No initial state set!")
        if (stateMapping.containsKey(finalState())) throw IllegalStateException(
            "${FinalState.getName()} can only be set as target of a transition, not an independent state"
        )
        if (stateMapping.values.count { containsTransitionToFinalState(it) } == 0) {
            logger.warn { "At least one transition should lead to the final state!" }
        }
        val transistorMappings = stateMapping.mapValues { Transitions(it.value) }
        val transistor = Transistor(transistorMappings, initialState!!, sharedData)
        return StateMachine.DefaultStateMachine(
            transistor,
            transistor.getSharedData(),
            withErrorStateAsDefault
        )
    }

    private fun containsTransitionToFinalState(set: Set<Transition<I, SD>>): Boolean =
        set.any { it.getNextState() is FinalState }
}