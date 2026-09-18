package com.smart.neteasemusic.business.ext

/**
 * @author david.long 2026/5/7 16:24
 */
/**
 * https://sf.163.com/help/documents/66982522786074624
 * 指定宽高
 */
fun String?.thumbnail(width: Int, height: Int) = this + "?imageView&thumbnail=${width}y${height}"

/**
 * 指定尺寸图片缩略图
 */
fun String?.thumbnail(size: Int) = this + "?imageView&thumbnail=${size}y${size}"

/**
 * GridLayoutManager列表页面统一的尺寸缩放
 */
fun String?.thumbnail() = if (this?.endsWith("thumbnail=300y300") == true) this else "$this?imageView&thumbnail=300y300"

/**
 * LinearLayoutManager列表页面统一的尺寸缩放
 */
fun String?.smallThumbnail() = if (this?.endsWith("thumbnail=100y100") == true) this else "$this?imageView&thumbnail=100y100"