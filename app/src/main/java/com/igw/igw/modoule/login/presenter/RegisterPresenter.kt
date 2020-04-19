package com.igw.igw.modoule.login.presenter

import com.igw.igw.bean.NationalityBean
import com.igw.igw.bean.login.CityListBean
import com.igw.igw.bean.login.RegisterBean
import com.igw.igw.bean.login.RegisterSuccessBean
import com.igw.igw.modoule.login.RegisterContract
import com.igw.igw.modoule.login.view.RegisterActivity
import com.igw.igw.mvp.presenter.BasePresenter
import com.igw.igw.network.NetObserver
import com.igw.igw.utils.LogUtils
import kotlin.math.log

/**
 *
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 *
 * @Describe 注册
 */
class RegisterPresenter(model: RegisterContract.Model)
    : BasePresenter<RegisterContract.View, RegisterContract.Model>(model) {




    companion object{

        val TAG = "RegisterPresenter"
    }


    /**
     * 获取 国籍信息
     */
    fun getNationalityData(){


        mModel.getNationalityData(object: NetObserver<NationalityBean.DataBean>(NationalityBean.DataBean::class.java){
            override fun onSuccess(m: NationalityBean.DataBean?) {

//                val toString = m?.countrys.toString()


//                LogUtils.d(TAG,"--->  $${m?.countrys?.get(0)?.countryCnName}")


                m?.let {
                    (rootView as RegisterActivity).showNationality(it.countrys)
                }



            }

            override fun onFail(code: String?, msg: String?) {
                LogUtils.d(TAG,"onFail --> ${code}")

            }

            override fun onError(msg: String?) {
                LogUtils.d(TAG,"onError --> ${msg}")


            }

        })
    }


    /**
     * 根据国籍id 获取城市列表
     */
    fun getCityListData(countryId : Int){

        mModel.getCityData(countryId, object: NetObserver<CityListBean.DataBean>(CityListBean.DataBean::class.java){
            override fun onSuccess(m: CityListBean.DataBean?) {


                m?.let {

                    (rootView as RegisterActivity).showCityListData(it.citys)
                }
            }

            override fun onFail(code: String?, msg: String?) {
            }

            override fun onError(msg: String?) {
            }

        })
    }


    override fun requestData() {


    }

    fun registerUser(registerBean: RegisterBean) {

        mModel.registerUser(registerBean,object :NetObserver<RegisterSuccessBean.DataBean>(RegisterSuccessBean.DataBean::class.java){
            override fun onSuccess(m: RegisterSuccessBean.DataBean?) {


                LogUtils.d(TAG,"注册成功 ")
                rootView.registerSuccess()
            }

            override fun onFail(code: String?, msg: String?) {
                LogUtils.d(TAG,"注册失败")

            }

            override fun onError(msg: String?) {

                LogUtils.d(TAG,"出现错误")
            }


        })
    }
}