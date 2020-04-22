package com.igw.igw.modoule.login

import com.igw.igw.bean.NationalityBean
import com.igw.igw.bean.login.CityListBean
import com.igw.igw.bean.login.RegisterBean
import com.igw.igw.bean.login.RegisterSuccessBean
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
class UpdateInfoContract {


    interface View : IBaseView {

        fun onSuccessNationality(countrys: List<NationalityBean.DataBean.CountrysBean>)

        fun onFailNationality()


        fun onSuccessCitys(citys : List<CityListBean.DataBean.CitysBean>,isLocal:Boolean)

        fun onFailCitys()
    }

    interface Model : IBaseModel {
        fun getNationalityData(observer: NetObserver<NationalityBean.DataBean>)

        fun getCityData(countryId: Int, observer: NetObserver<CityListBean.DataBean>)

        fun updateUserInfo(registerBean: RegisterBean, observer: NetObserver<RegisterSuccessBean.DataBean>)

    }

    interface Presenter : IBasePresenter {


        fun getNationalityData()

        fun getCityData(countryId: Int,isLocal: Boolean)

        fun updateUserInfo(registerBean: RegisterBean)

    }
}