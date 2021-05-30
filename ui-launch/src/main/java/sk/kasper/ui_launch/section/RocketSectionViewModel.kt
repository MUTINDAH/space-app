package sk.kasper.ui_launch.section

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.Rocket
import sk.kasper.domain.usecase.launchdetail.GetRocketForLaunch
import sk.kasper.ui_launch.R

data class RocketSectionState(
    val rocketName: String = "",
    val height: String = "",
    val diameter: String = "",
    val mass: String = "",
    val payloadLeo: String = "",
    val payloadGto: String = "",
    val thrust: String = "",
    val stages: String = "",
    val title: Int = R.string.section_rocket,
    val visible: Boolean = false
)


class RocketSectionViewModel @AssistedInject constructor(
    @Assisted private val launchId: String,
    private val getRocketForLaunch: GetRocketForLaunch
) : LoaderViewModel<RocketSectionState, Rocket>(RocketSectionState()) {

    init {
        submitAction(Action.Init)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): RocketSectionViewModel
    }

    override suspend fun load(): Response<Rocket> {
        return getRocketForLaunch.getRocket(launchId)
    }

    override fun mapLoadToState(load: Rocket, oldState: RocketSectionState): RocketSectionState {
        return RocketSectionState(
            rocketName = load.rocketName,
            height = "${load.height} m",
            diameter = "${load.diameter} m",
            mass = "${load.mass} kg",
            payloadLeo = "${load.payloadLeo} kg",
            payloadGto = "${load.payloadGto} kg",
            thrust = "${load.thrust} kN",
            stages = "${load.stages}",
            visible = true
        )
    }

    override fun mapErrorToState(
        message: String?,
        oldState: RocketSectionState
    ): RocketSectionState {
        return oldState.copy(visible = false)
    }

}