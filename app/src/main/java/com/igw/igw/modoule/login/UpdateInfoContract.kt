package com.igw.igw.modoule.login

import com.igw.igw.bean.NationalityBean
import com.igw.igw.bean.login.*
import com.igw.igw.mvp.model.IBaseModel
import com.igw.igw.mvp.presenter.IBasePresenter
import com.igw.igw.mvp.view.IBaseView
import com.igw.igw.network.NetObserver
import java.io.File

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

        fun loadHeadImageSuccessful(data: HeadImageBean.DataBean)

        fun loadHeadImageFail(code: Int, msg: String)

        fun updateUserInfoSuccessful(data:UserInfoBean.DataBean)

        fun updateUserInfoFail(code: Int,msg:String)
    }

    interface Model : IBaseModel {
        fun getNationalityData(language: Int,observer: NetObserver<NationalityBean.DataBean>)

        fun getCityData(countryId: Int,language: Int, observer: NetObserver<CityListBean.DataBean>)

        fun updateUserInfo(registerBean: RegisterBean, observer: NetObserver<UserInfoBean.DataBean>)

        fun unloadImageFile(file: File,observer: NetObserver<HeadImageBean.DataBean>)

    }

    interface Presenter : IBasePresenter {


        fun getNationalityData(language: Int)

        fun getCityData(countryId: Int,language: Int,isLocal: Boolean)

        fun updateUserInfo(registerBean: RegisterBean)

        fun uploadImaga(file:File)

    }
}
