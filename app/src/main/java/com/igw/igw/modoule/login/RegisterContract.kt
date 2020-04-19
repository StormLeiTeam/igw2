package com.igw.igw.modoule.login

import com.igw.igw.bean.NationalityBean
import com.igw.igw.bean.login.CityListBean
import com.igw.igw.bean.login.RegisterBean
import com.igw.igw.bean.login.RegisterSuccessBean
import com.igw.igw.mvp.model.IBaseModel
import com.igw.igw.mvp.view.IBaseView
import com.igw.igw.network.NetObserver

/**
 *
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 *
 * @Describe 注册页面
 */
interface RegisterContract {


    interface Model : IBaseModel {

        //获取国籍
        fun getNationalityData(observer: NetObserver<NationalityBean.DataBean>)

        fun getCityData(countryId : Int, observer: NetObserver<CityListBean.DataBean>)

        fun registerUser(registerBean: RegisterBean, observer: NetObserver<RegisterSuccessBean.DataBean>)

    }

    interface View : IBaseView {


       fun  registerSuccess()

    }
}