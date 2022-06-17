/**
 * @author: Zhang
 * @description: 对话列表适配器
 */
package com.example.aivoicehelper.adapter

import com.example.aivoicehelper.R
import com.example.aivoicehelper.data.ChatList
import com.example.aivoicehelper.entity.AppConstants
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.module_weather.tools.WeatherIconTools


class ChatListAdapter(
    mList: List<ChatList>,
    ) : CommonAdapter<ChatList>(mList, object : OnMoreBindDataListener<ChatList> {

    override fun onBindViewHolder(
        model: ChatList,
        viewHolder: CommonViewHolder,
        type: Int,
        position: Int
    ) {
        when(type){
            AppConstants.TYPE_MINE_TEXT -> viewHolder.setText(R.id.tv_mine_text,model.text)
            AppConstants.TYPE_AI_TEXT -> viewHolder.setText(R.id.tv_ai_text, model.text)
            AppConstants.TYPE_AI_WEATHER -> {
                viewHolder.setText(R.id.tv_city, model.city)
                viewHolder.setText(R.id.tv_info, model.info)
                viewHolder.setText(R.id.tv_temperature, model.temperature)
                viewHolder.setImageResource(R.id.iv_icon, WeatherIconTools.getIcon(model.wid))
            }
        }
    }

    override fun getLayoutId(type: Int): Int {
        return when (type) {
            AppConstants.TYPE_MINE_TEXT -> R.layout.layout_mine_text
            AppConstants.TYPE_AI_TEXT -> R.layout.layout_ai_text
            AppConstants.TYPE_AI_WEATHER -> R.layout.layout_ai_weather
            else -> 0
        }
    }

    override fun getItemType(position: Int): Int {
        return mList[position].type
    }

})