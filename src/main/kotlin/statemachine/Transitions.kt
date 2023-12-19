package statemachine


class Transitions<I : Input, SD : SharedData>(private val transitions: Set<Transition<I, SD>>) {
    internal fun nextState(input: I, sharedData: SD, withErrorStateAsDefault: Boolean = true): State<SD> {
        return if (transitions.count { it.shouldChangeState(input, sharedData) } == 0) {
            if (withErrorStateAsDefault) {
                ErrorState.getTypeCasted()
            } else throw IllegalStateException("${ErrorState.getName()} as a default is not allowed, but no other state can be reached")
        } else transitions.first { it.shouldChangeState(input, sharedData) }.getNextState()
    }
}