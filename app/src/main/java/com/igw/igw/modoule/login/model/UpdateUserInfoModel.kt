package com.igw.igw.modoule.login.model

import com.igw.igw.bean.NationalityBean
import com.igw.igw.bean.login.*
import com.igw.igw.modoule.login.UpdateInfoContract
import com.igw.igw.network.NetObserver
import com.shengshijingu.yashiji.common.util.ControllerUtils
import java.io.File

/**
 *
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 *
 * @Describe
 */
class UpdateUserInfoModel : UpdateInfoContract.Model {


    override fun getNationalityData(language: Int, observer: NetObserver<NationalityBean.DataBean>) {
        ControllerUtils.getLoginControllerInstance().getNationality(language, observer)


    }

    override fun getCityData(countryId: Int, language: Int, observer: NetObserver<CityListBean.DataBean>) {

        ControllerUtils.getLoginControllerInstance().getCityListData(countryId, language, observer)


    }

    override fun updateUserInfo(registerBean: RegisterBean, observer: NetObserver<UserInfoBean.DataBean>) {


        ControllerUtils.getLoginControllerInstance()
                .updateUserInfo(registerBean.countryId!!, registerBean.cityId!!, registerBean.lastName, registerBean.firstName,
                        registerBean.sex!!, registerBean.birthday, registerBean.nickname, registerBean.agencyName, registerBean.userDesc,
                        registerBean.email, registerBean.mobilePhone, registerBean.inviteCode, registerBean.headImage, observer)


    }

    override fun unloadImageFile(file: File, observer: NetObserver<HeadImageBean.DataBean>) {

        ControllerUtils.getLoginControllerInstance()
                .uploadImage(file, observer)


    }


}