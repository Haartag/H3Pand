package com.valerytimofeev.h3pand.domain.model

/**
 * State of dialog window
 */
data class DialogState(
    val dialogStatus: DialogStatus,
    val dialogSettings: DialogSettings = DialogSettings.CLOSED,
    val searchState: SearchState = SearchState.CLOSED,
    val searchType: SearchType = SearchType.NONE
) {
    companion object {
        enum class DialogUiPresets(
            val dialogUiState: DialogState
        ) {
            CLOSED(DialogState(DialogStatus.CLOSED)),

            GUARD_CASTLE(DialogState(DialogStatus.OPENED, DialogSettings.GuardStageOne)),
            GUARD_UNIT(DialogState(DialogStatus.OPENED, DialogSettings.GuardStageTwo)),
            GUARD_NUMBER(DialogState(DialogStatus.OPENED, DialogSettings.GuardStageThree)),
            GUARD_SEARCH(DialogState(DialogStatus.OPENED, DialogSettings.GuardSearch, SearchState.OPENED, SearchType.GUARD)),

            ADDVALUE_TYPE(DialogState(DialogStatus.OPENED, DialogSettings.AddValueStageOne)),
            ADDVALUE_DWELLING(DialogState(DialogStatus.OPENED, DialogSettings.DwellingDialog)),
            ADDVALUE_CUSTOMVALUE(DialogState(DialogStatus.OPENED, DialogSettings.CustomValueDialog)),
            ADDVALUE_SUBTYPE(DialogState(DialogStatus.OPENED, DialogSettings.AddValueStageTwo)),
            ADDVALUE_ITEM(DialogState(DialogStatus.OPENED, DialogSettings.AddValueStageThree)),
            ADDVALUE_SEARCH(DialogState(DialogStatus.OPENED, DialogSettings.AddValueSearch, SearchState.OPENED, SearchType.ADDVALUE)),

            ZONE(DialogState(DialogStatus.OPENED, DialogSettings.CastleZoneDialog)),
        }
    }
}

/**
 * State of search window in dialog
 */
enum class SearchState {
    OPENED,
    CLOSED
}
/**
 * State of search type in dialog
 */
enum class SearchType {
    NONE,
    GUARD,
    ADDVALUE
}

/**
 * Status of dialog
 */
enum class DialogStatus {
    OPENED,
    CLOSED
}