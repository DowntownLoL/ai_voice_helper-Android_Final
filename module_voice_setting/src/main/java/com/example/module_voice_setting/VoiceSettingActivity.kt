/**
 * @author: Zhang
 * @description: 语音设置
 */
package com.example.module_voice_setting

import android.os.Bundle
import android.widget.SeekBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.lib_base.helper.ARouterHelper
import com.example.lib_voice.manager.VoiceManager
import com.example.lib_voice.tts.VoiceTTS
import kotlinx.android.synthetic.main.activity_voice_setting.*

@Route(path = ARouterHelper.PATH_VOICE_SETTING)
class VoiceSettingActivity : BaseActivity() {

    private val mList: ArrayList<String> = ArrayList()
    private var mTTSPeopleIndex: Array<String>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_voice_setting
    }

    override fun getTitleText(): String {
        return getString(com.example.lib_base.R.string.app_title_voice_setting)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        // 默认
        bar_voice_speed.progress = 6
        bar_voice_volume.progress = 8

        // 设置最大值
        bar_voice_speed.max = 15
        bar_voice_volume.max = 15

        initData() // 初始化数据
        initListener() // 初始化监听器
        initPeopleView() // 初始化发音人
        btn_test.setOnClickListener {
            VoiceManager.ttsStart("您好，我是您的智能语音助手，我能帮您做什么吗？")
        }
    }

    // 初始化数据
    private fun initData() {
        val mTTSPeople = resources.getStringArray(R.array.TTSPeople)

        mTTSPeopleIndex = resources.getStringArray(R.array.TTSPeopleIndex)

        mTTSPeople.forEach {
            mList.add(it)
        }
    }

    // 初始化发音人列表
    private fun initPeopleView() {
        rv_voice_people.layoutManager = LinearLayoutManager(this)
        rv_voice_people.addItemDecoration(  // 添加分割线
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL  // 分割线方向
            )
        )
        rv_voice_people.adapter =
            CommonAdapter(mList, object : CommonAdapter.OnBindDataListener<String> {
                override fun onBindViewHolder(
                    model: String,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    viewHolder.setText(R.id.mTvPeopleContent, model)
                    viewHolder.itemView.setOnClickListener {
                        mTTSPeopleIndex?.let {
                            VoiceManager.setPeople(it[position])
                        }
                    }
                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.tts_people_list
                }

            })
    }

    private fun initListener() {
        // 监听用户拖动语速
        bar_voice_speed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bar_voice_speed.progress = progress
                VoiceManager.setSpeed(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        // 监听用户拖动音量
        bar_voice_volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bar_voice_volume.progress = progress
                VoiceManager.setVoiceVolume(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

}