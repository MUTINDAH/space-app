package sk.kasper.ui_timeline

import androidx.annotation.DrawableRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import sk.kasper.ui_common.tag.UiTag
import sk.kasper.ui_common.utils.FormattedTimeType
import sk.kasper.ui_timeline.utils.rocketIdToDrawableRes

// todo update
open class LaunchListItemViewModel(val listener: OnListInteractionListener) : BaseObservable() {

    interface OnListInteractionListener {
        fun onItemClick(item: LaunchListItem)
    }

    @get:Bindable
    val tags: ObservableList<UiTag> = ObservableArrayList()

    @get:Bindable
    var tagsVisible: Boolean = true

    @get:Bindable
    var title: String = ""

    @get:Bindable
    var launchDateTime: LocalDateTime = LocalDateTime.MIN

    @get:Bindable
    var prettyTimeVisible: Boolean = false

    @get:Bindable
    var formattedTimeVisible: Boolean = false

    @get:Bindable
    var formattedTimeType: FormattedTimeType = FormattedTimeType.WEEKDAY_TIME

    @get:Bindable
    var dateConfirmed: Boolean = true

    @get:Bindable
    @get:DrawableRes
    var rocketIcon: Int = R.drawable.soyuz

    var launchListItem: LaunchListItem? = null
        set(value) {
            field = value

            launchListItem?.let {
                tags.clear()
                tags.addAll(it.tags)

                rocketIcon = rocketIdToDrawableRes(launchListItem?.rocketId)

                tagsVisible = tags.isNotEmpty()
            }

            title = launchListItem?.launchName?.replace("|", "•") ?: "" // launchName contains both launch and rocket name
            launchDateTime = launchListItem?.launchDateTime ?: LocalDateTime.MIN

            updateTime()

            notifyChange()
        }

    private fun updateTime() {
        dateConfirmed = true
        val currentDateTime = LocalDateTime.now()
        val weekLaterDateTime = currentDateTime.plusDays(7)
        val todayMidnightDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.MIDNIGHT).plusDays(1)

        when {
            launchDateTime.isBefore(todayMidnightDateTime) -> {
                formattedTimeType = FormattedTimeType.TIME
                formattedTimeVisible = true
                prettyTimeVisible = true
            }
            launchDateTime.isBefore(weekLaterDateTime) -> {
                formattedTimeType = FormattedTimeType.WEEKDAY_TIME
                formattedTimeVisible = true
                prettyTimeVisible = true
            }
            else -> {
                formattedTimeType = FormattedTimeType.DATE
                formattedTimeVisible = true
                prettyTimeVisible = false
            }
        }

        launchListItem?.let {
            if (!it.accurateTime) {
                prettyTimeVisible = false
                formattedTimeType = FormattedTimeType.DATE
            }

            dateConfirmed = it.accurateDate
        }
    }

    fun onClick() {
        launchListItem?.let {
            listener.onItemClick(it)
        }
    }

    open fun getCurrentDateTime(): LocalDateTime = LocalDateTime.now()

}

