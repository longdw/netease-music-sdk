package com.smart.neteasemusic.business.base

import androidx.lifecycle.ViewModel

/**
 * @author david.long 2026/2/24 17:19
 */
open class BaseViewModel : ViewModel() {
    var loadMore = false
    protected var offset = 0
}