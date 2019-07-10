package com.bitwiserain.remindme.dummy

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {
    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: List<DummyItem> = listOf(
        DummyItem("Respond to that important email", "2m37"),
        DummyItem("Transfer tuition fee", "5h"),
        DummyItem("Ask X about a possible extension", "1d2h")
    )

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val title: String, val time: String) {
        override fun toString(): String = "$title in $time"
    }
}
