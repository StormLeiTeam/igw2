package com.igw.igw.modoule.login

import com.igw.igw.bean.login.LoginBean
import com.igw.igw.bean.login.VerifyBean
import com.igw.igw.mvp.model.IBaseModel
import com.igw.igw.mvp.presenter.IBasePresenter
import com.igw.igw.mvp.view.IBaseView
import com.igw.igw.network.NetObserver

/**
 *
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 *
 * @Describe
 */
interface LoginContract {


    interface Model : IBaseModel {

        //登陆 账号
        fun loginByAccent(accent: String, password: String, observer: NetObserver<LoginBean.DataBean>)

        // 登陆 email
        fun loginByEmail(email: String, verifyCode: String, observer: NetObserver<LoginBean.DataBean>)

        // 发送验证码
        fun sendEmailVerifyCode(email: String, type: Int,observer: NetObserver<VerifyBean.DataBean>)




    }


    interface View : IBaseView {

        fun loginSuccess(loginBean : LoginBean.DataBean)
        fun loginFail(code:String ,msg: String)

        fun sendVerifyCodeSuccess()
        fun sendVerifyCodeFail(code: String, msg: String)



    }

    interface Presenter : IBasePresenter {

        //账号登陆
        fun loginByAccent(accent: String, password: String)

        //邮箱登陆
        fun loginByEmail(email: String, verifyCode: String)

        // 发送验证码
        fun sendEmailVerifyCode(email: String, type: Int)


    }
}