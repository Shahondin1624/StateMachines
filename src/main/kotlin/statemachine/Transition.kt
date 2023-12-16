package statemachine

interface Transition<I: Input, SD: SharedData> {
    fun getActivationCondition(): (I) -> Boolean
    fun shouldChangeState(input: I) = getActivationCondition().invoke(input)
    fun getNextState(): State<SD>
}