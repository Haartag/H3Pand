package com.valerytimofeev.h3pand.domain.model

data class Dialog <DialogSettings> (val status: DialogStatus, val data: DialogSettings?) {
    companion object {
        fun opened(data: DialogSettings?): Dialog<DialogSettings> {
            return Dialog(DialogStatus.OPENED, data)
        }

        fun closed (data: DialogSettings?): Dialog<DialogSettings> {
            return Dialog(DialogStatus.CLOSED, null)
        }
    }
}

enum class DialogStatus {
    OPENED,
    CLOSED
}