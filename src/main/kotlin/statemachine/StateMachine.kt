package statemachine

interface StateMachine<I : Input, SD : SharedData> {
    fun getNextState(input: I): State<SD>
    fun getCurrentState(): State<SD>
    fun isInFinalState(): Boolean
    fun getSharedData(): SD

    class DefaultStateMachine<I : Input, SD : SharedData>(
        private val transistor: Transistor<I, SD>,
        private val sharedData: SD,
        private val withErrorStateAsDefault: Boolean = true
    ) : StateMachine<I, SD> {
        override fun getNextState(input: I) = transistor.getNextState(input, withErrorStateAsDefault)
        override fun getCurrentState() = transistor.getCurrentState()
        override fun isInFinalState() = transistor.isInFinalState()
        override fun getSharedData() = sharedData
    }
}