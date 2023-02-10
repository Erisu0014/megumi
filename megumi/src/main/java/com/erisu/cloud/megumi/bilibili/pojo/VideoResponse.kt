package com.erisu.cloud.megumi.bilibili.pojo

import com.alibaba.fastjson.annotation.JSONField

data class VideoResponse(

	@JSONField(name="code")
	val code: Int? = null,

	@JSONField(name="data")
	val data: Data? = null,

	@JSONField(name="message")
	val message: String? = null,

	@JSONField(name="ttl")
	val ttl: Int? = null
)

data class UserGarb(

	@JSONField(name="url_image_ani_cut")
	val urlImageAniCut: String? = null
)

data class HonorReply(
	val any: Any? = null
)

data class Data(

	@JSONField(name="bvid")
	val bvid: String? = null,

	@JSONField(name="copyright")
	val copyright: Int? = null,

	@JSONField(name="user_garb")
	val userGarb: UserGarb? = null,

	@JSONField(name="videos")
	val videos: Int? = null,

	@JSONField(name="pic")
	val pic: String? = null,

	@JSONField(name="title")
	val title: String? = null,

	@JSONField(name="tid")
	val tid: Int? = null,

	@JSONField(name="duration")
	val duration: Int? = null,

	@JSONField(name="pages")
	val pages: List<PagesItem?>? = null,

	@JSONField(name="no_cache")
	val noCache: Boolean? = null,

	@JSONField(name="rights")
	val rights: Rights? = null,

	@JSONField(name="ctime")
	val ctime: Int? = null,

	@JSONField(name="dynamic")
	val dynamic: String? = null,

	@JSONField(name="state")
	val state: Int? = null,

	@JSONField(name="dimension")
	val dimension: Dimension? = null,

	@JSONField(name="pubdate")
	val pubdate: Int? = null,

	@JSONField(name="owner")
	val owner: Owner? = null,

	@JSONField(name="desc_v2")
	val descV2: List<DescV2Item?>? = null,

	@JSONField(name="stat")
	val stat: Stat? = null,

	@JSONField(name="tname")
	val tname: String? = null,

	@JSONField(name="is_season_display")
	val isSeasonDisplay: Boolean? = null,

	@JSONField(name="premiere")
	val premiere: Any? = null,

	@JSONField(name="honor_reply")
	val honorReply: HonorReply? = null,

	@JSONField(name="subtitle")
	val subtitle: Subtitle? = null,

	@JSONField(name="aid")
	val aid: Int? = null,

	@JSONField(name="desc")
	val desc: String? = null,

	@JSONField(name="cid")
	val cid: Int? = null
)

data class Dimension(

	@JSONField(name="rotate")
	val rotate: Int? = null,

	@JSONField(name="width")
	val width: Int? = null,

	@JSONField(name="height")
	val height: Int? = null
)

data class Rights(

	@JSONField(name="clean_mode")
	val cleanMode: Int? = null,

	@JSONField(name="movie")
	val movie: Int? = null,

	@JSONField(name="is_cooperation")
	val isCooperation: Int? = null,

	@JSONField(name="ugc_pay")
	val ugcPay: Int? = null,

	@JSONField(name="no_background")
	val noBackground: Int? = null,

	@JSONField(name="pay")
	val pay: Int? = null,

	@JSONField(name="elec")
	val elec: Int? = null,

	@JSONField(name="ugc_pay_preview")
	val ugcPayPreview: Int? = null,

	@JSONField(name="bp")
	val bp: Int? = null,

	@JSONField(name="autoplay")
	val autoplay: Int? = null,

	@JSONField(name="download")
	val download: Int? = null,

	@JSONField(name="no_reprint")
	val noReprint: Int? = null,

	@JSONField(name="is_360")
	val is360: Int? = null,

	@JSONField(name="no_share")
	val noShare: Int? = null,

	@JSONField(name="hd5")
	val hd5: Int? = null,

	@JSONField(name="is_stein_gate")
	val isSteinGate: Int? = null
)

data class Subtitle(

	@JSONField(name="allow_submit")
	val allowSubmit: Boolean? = null,

	@JSONField(name="list")
	val list: List<Any?>? = null
)

data class Stat(

	@JSONField(name="now_rank")
	val nowRank: Int? = null,

	@JSONField(name="like")
	val like: Int? = null,

	@JSONField(name="dislike")
	val dislike: Int? = null,

	@JSONField(name="his_rank")
	val hisRank: Int? = null,

	@JSONField(name="evaluation")
	val evaluation: String? = null,

	@JSONField(name="argue_msg")
	val argueMsg: String? = null,

	@JSONField(name="view")
	val view: Int? = null,

	@JSONField(name="danmaku")
	val danmaku: Int? = null,

	@JSONField(name="share")
	val share: Int? = null,

	@JSONField(name="reply")
	val reply: Int? = null,

	@JSONField(name="aid")
	val aid: Int? = null,

	@JSONField(name="favorite")
	val favorite: Int? = null,

	@JSONField(name="coin")
	val coin: Int? = null
)

data class PagesItem(

	@JSONField(name="duration")
	val duration: Int? = null,

	@JSONField(name="vid")
	val vid: String? = null,

	@JSONField(name="part")
	val part: String? = null,

	@JSONField(name="weblink")
	val weblink: String? = null,

	@JSONField(name="from")
	val from: String? = null,

	@JSONField(name="page")
	val page: Int? = null,

	@JSONField(name="dimension")
	val dimension: Dimension? = null,

	@JSONField(name="cid")
	val cid: Int? = null
)

data class Owner(

	@JSONField(name="face")
	val face: String? = null,

	@JSONField(name="name")
	val name: String? = null,

	@JSONField(name="mid")
	val mid: Number? = null
)

data class DescV2Item(

	@JSONField(name="raw_text")
	val rawText: String? = null,

	@JSONField(name="type")
	val type: Int? = null,

	@JSONField(name="biz_id")
	val bizId: Int? = null
)
