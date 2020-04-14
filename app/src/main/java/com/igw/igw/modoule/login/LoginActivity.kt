package com.igw.igw.modoule.login

import android.content.Intent
import android.support.v4.content.ContextCompat
import com.bumptech.glide.load.data.LocalUriFetcher
import com.igw.igw.R
import com.igw.igw.activity.BaseActivity
import com.igw.igw.mvp.presenter.BasePresenter
import com.igw.igw.mvp.view.IBaseView
import com.igw.igw.utils.LocaleUtils
import com.igw.igw.utils.StatusBarUtils
import com.igw.igw.utils.statusbarutils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.status_bar_view.*
import java.util.*


/**
 * 登陆页面
 */
class LoginActivity : BaseActivity<BasePresenter<IBaseView>>()  {


    companion object{

        val TAG = "LoginActivity"
    }
    override fun initView() {


        StatusBarUtils.setColor(this, ContextCompat.getColor(this, R.color.colorF33))
        StatusBarUtils.setDarkMode(this)


        setUpListener()

    }

    private fun setUpListener() {






        btn_register.setOnClickListener {
            // 跳转到注册模块



        }

        // select lanuage  mode
        tv_language_select.setOnClickListener{

            if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_ENGLISH)) {
                LocaleUtils.saveUserLocale(this,LocaleUtils.LOCALE_ENGLISH)
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH)

            }else{
                LocaleUtils.saveUserLocale(this,LocaleUtils.LOCALE_CHINESE)
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE)
            }

            resertAct()


        }

    }

    private fun resertAct() {

        finish()

        var intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        overridePendingTransition(0, 0);


    }

    override fun initPresenter() {
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun setTitle(): String {

        return ""
    }

    override fun setRightButton(): String {

        return  ""

    }

    override fun setStatusBarColor(): Boolean {
        return true
    }


}
