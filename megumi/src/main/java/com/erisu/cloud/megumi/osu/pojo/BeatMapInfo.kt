package com.erisu.cloud.megumi.osu.pojo

import com.alibaba.fastjson.PropertyNamingStrategy
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.annotation.JSONType

@JSONType(naming = PropertyNamingStrategy.SnakeCase)
data class BeatMapInfo(
    var accuracy: Double,
    var ar: Double,
    var beatmapset: Beatmapset,
    var beatmapset_id: Int,
    var bpm: Int,
    var checksum: String,
    var convert: Boolean,
    var count_circles: Int,
    var count_sliders: Int,
    var count_spinners: Int,
    var cs: Int,
    var deleted_at: Any?,
    var difficulty_rating: Double,
    var drain: Int,
    var failtimes: Failtimes,
    var hit_length: Int,
    var id: Int,
    var is_scoreable: Boolean,
    var last_updated: String,
    var max_combo: Int,
    var mode: String,
    var mode_int: Int,
    var passcount: Int,
    var playcount: Int,
    var ranked: Int,
    var status: String,
    var total_length: Int,
    var url: String,
    var user_id: Int,
    var version: String,
) {
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    data class Beatmapset(
        var artist: String,
        var artist_unicode: String,
        var availability: Availability,
        var bpm: Int,
        var can_be_hyped: Boolean,
        var covers: Covers,
        var creator: String,
        var discussion_enabled: Boolean,
        var discussion_locked: Boolean,
        var favourite_count: Int,
        var hype: Any?,
        var id: Int,
        var is_scoreable: Boolean,
        var last_updated: String,
        var legacy_thread_url: String,
        var nominations_summary: NominationsSummary,
        var nsfw: Boolean,
        var offset: Int,
        var play_count: Int,
        var preview_url: String,
        var ranked: Int,
        var ranked_date: String,
        var ratings: List<Int>,
        var source: String,
        var spotlight: Boolean,
        var status: String,
        var storyboard: Boolean,
        var submitted_date: String,
        var tags: String,
        var title: String,
        var title_unicode: String,
        var track_id: Any?,
        var user_id: Int,
        var video: Boolean,
    ) {
        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        data class Availability(
            var download_disabled: Boolean,
            var more_information: Any?,
        )

        data class Covers(
            var card: String,
            @JSONField(name = "card@2x")
            var card2x: String,
            var cover: String,
            @JSONField(name = "cover@2x")
            var cover2x: String,
            var list: String,
            @JSONField(name = "list@2x")
            var list2x: String,
            var slimcover: String,
            @JSONField(name = "slimcover@2x")
            var slimcover2x: String,
        )

        data class NominationsSummary(
            var current: Int,
            var required: Int,
        )
    }

    data class Failtimes(
        var exit: List<Int>,
        var fail: List<Int>,
    )
}

