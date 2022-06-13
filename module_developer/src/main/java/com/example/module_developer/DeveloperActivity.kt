/**
 * @author: Zhang
 * @description: 开发者模式 用于测试功能
 */
package com.example.module_developer

import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTS
import com.example.module_developer.data.DeveloperListData
import kotlinx.android.synthetic.main.activity_developer.*

@Route(path = ARouterHelper.PATH_DEVELOPER)
class DeveloperActivity : BaseActivity() {

    private val mTypeTitle = 0 // 标题
    private val mTypeContent = 1 // 内容

    private val mList = ArrayList<DeveloperListData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_developer
    }

    override fun getTitleText(): String {
        return getString(com.example.lib_base.R.string.app_title_developer)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        initData()
        initListView()
    }

    // 初始化列表
    private fun initListView() {
        // layout管理器
        rvDeveloperView.layoutManager = LinearLayoutManager(this)
        // 分割线
        rvDeveloperView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        // Adapter
        rvDeveloperView.adapter =
            CommonAdapter(mList, object : CommonAdapter.OnMoreBindDataListener<DeveloperListData> {
                override fun onBindViewHolder(
                    model: DeveloperListData,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    when (model.type) {
                        mTypeTitle -> {
                            viewHolder.setText(R.id.mTvDeveloperTitle, model.text)
                        }
                        mTypeContent -> {
                            viewHolder.setText(
                                R.id.mTvDeveloperContent,
                                "${position}.${model.text}"
                            )
                            viewHolder.itemView.setOnClickListener {
                                itemClick(position)
                            }
                        }
                    }
                }

                override fun getLayoutId(type: Int): Int {
                    if (type == mTypeTitle) {
                        return R.layout.developer_title
                    } else {
                        return R.layout.developer_content
                    }
                }

                override fun getItemType(position: Int): Int {
                    return mList[position].type
                }

            })

    }

    // 初始化数据
    private fun initData() {
        val dataArray =
            resources.getStringArray(com.example.lib_base.R.array.DeveloperListArray) // DeveloperListArray中拿数据
        dataArray.forEach {
            // 若含[则为标题
            if (it.contains("[")) {
                addItemData(mTypeTitle, it.replace("[", "").replace("]", "")) // 去掉"[]"
            } else {
                addItemData(mTypeContent, it)
            }
        }
    }

    // 添加数据
    private fun addItemData(type: Int, text: String) {
        val data = DeveloperListData(type, text)
        mList.add(data)
    }

    // 点击事件
    private fun itemClick(position: Int) {
        when (position) {
            1 -> ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
            2 -> ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
            3 -> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
            4 -> ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
            5 -> ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
            6 -> ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
            7 -> ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)

            14 -> VoiceManager.startWakeUp()
            15-> VoiceManager.stopWakeUp()

            20 -> VoiceManager.ttsStart("这是一段简短的文字内容，用于测试语音")
            21 -> VoiceManager.ttsPause()
            22 -> VoiceManager.ttsResume()
            23 -> VoiceManager.ttsStop()
            24 -> VoiceManager.ttsRelease()
        }
    }

}