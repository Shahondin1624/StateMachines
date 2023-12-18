package statemachine

interface Transition<I : Input, SD : SharedData> {
    fun getActivationCondition(): (I, SD) -> Boolean
    fun shouldChangeState(input: I, sharedData: SD) = getActivationCondition().invoke(input, sharedData)
    fun getNextState(): State<SD>
}

class DefaultTransition<I : Input, SD : SharedData>(
    private val nextState: State<SD>,
    private val activationCondition: (I, SD) -> Boolean
) : Transition<I, SD> {
    override fun getActivationCondition(): (I, SD) -> Boolean = activationCondition

    override fun getNextState(): State<SD> = nextState

}