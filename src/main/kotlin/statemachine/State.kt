package statemachine

interface State<SD: SharedData> {
    fun getName(): String
    fun entry(sharedData: SD)
    fun exit(sharedData: SD)
}

data class InitialState<SD: SharedData>(val nextState: State<SD>) : State<SD> {
    override fun getName(): String = javaClass.simpleName

    override fun entry(sharedData: SD) {}

    override fun exit(sharedData: SD) {}
}

object FinalState : State<SharedData> {
    override fun getName(): String = javaClass.simpleName

    override fun entry(sharedData: SharedData) {}

    override fun exit(sharedData: SharedData) {}
}

class DefaultState<SD: SharedData>(
    private val name: String,
    private val entryFunction: (SD) -> Unit,
    private val exitFunction: (SD) -> Unit
) : State<SD> {
    override fun getName() = name

    override fun entry(sharedData: SD) = entryFunction(sharedData)

    override fun exit(sharedData: SD) = exitFunction(sharedData)
}