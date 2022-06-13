/**
 * @author: Zhang
 * @description: EventBus 使用 Step.1 —— 事件对象
 * @link: https://github.com/greenrobot/EventBus
 */

package com.example.lib_base.event

class MessageEvent(val type: Int) {

    var stringValue: String = ""
    var intValue: Int = 0
    var booleanValue: Boolean = false
}