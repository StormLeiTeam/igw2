package com.igw.igw.modoule.login.view


import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.Gravity.BOTTOM
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.jpush.android.webview.bridge.HostJsScope.startMainActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.igw.igw.MainActivity
import com.igw.igw.R
import com.igw.igw.activity.BaseActivity
import com.igw.igw.bean.NationalityBean
import com.igw.igw.bean.login.*
import com.igw.igw.modoule.login.RegisterContract
import com.igw.igw.modoule.login.loginstate.LoginManager
import com.igw.igw.modoule.login.model.RegisterModel
import com.igw.igw.modoule.login.presenter.RegisterPresenter
import com.igw.igw.utils.*
import com.igw.igw.widget.ChoicePopWindow
import com.igw.igw.widget.storm.StatusBarView
import com.igw.igw.widget.storm.TextClickPrivacy
import com.igw.igw.widget.storm.popwindowselect.popselectview.WheelViewPopupwindow
import com.shengshijingu.yashiji.common.Constants
import com.shengshijingu.yashiji.common.util.ToastUtil
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btn_submit
import kotlinx.android.synthetic.main.activity_register.et_agencyname
import kotlinx.android.synthetic.main.activity_register.et_description
import kotlinx.android.synthetic.main.activity_register.et_email
import kotlinx.android.synthetic.main.activity_register.et_explain
import kotlinx.android.synthetic.main.activity_register.et_inviteCode
import kotlinx.android.synthetic.main.activity_register.et_lastname_explain
import kotlinx.android.synthetic.main.activity_register.et_nickname
import kotlinx.android.synthetic.main.activity_register.et_password
import kotlinx.android.synthetic.main.activity_register.et_password_again
import kotlinx.android.synthetic.main.activity_register.et_phonenumber
import kotlinx.android.synthetic.main.activity_register.iv_head_img
import kotlinx.android.synthetic.main.activity_register.root_main
import kotlinx.android.synthetic.main.activity_register.tv_select_city
import kotlinx.android.synthetic.main.activity_register.tv_select_gender
import kotlinx.android.synthetic.main.activity_register.tv_select_nationality
import kotlinx.android.synthetic.main.activity_register.tv_select_ymd
import kotlinx.android.synthetic.main.activity_update_user_info.*
import kotlinx.android.synthetic.main.common_status_bar.*
import org.devio.takephoto.app.TakePhoto
import org.devio.takephoto.app.TakePhotoImpl
import org.devio.takephoto.compress.CompressConfig
import org.devio.takephoto.model.CropOptions
import org.devio.takephoto.model.InvokeParam
import org.devio.takephoto.model.TContextWrap
import org.devio.takephoto.model.TResult
import org.devio.takephoto.permission.InvokeListener
import org.devio.takephoto.permission.PermissionManager
import org.devio.takephoto.permission.TakePhotoInvocationHandler
import java.io.File
import java.util.*


/**
 * 注册模块
 */

class RegisterActivity : BaseActivity<RegisterPresenter>(), RegisterContract.View, TakePhoto.TakeResultListener, InvokeListener {

    companion object {


        val TAG = "RegisterActivity"

        val GENDER_JSON = "[{\"chName\":\"男\",\"enName\":\"man\",\"id\":1,\"isEnglish\":false},{\"chName\":\"女\",\"enName\":\"women\",\"id\":2,\"isEnglish\":false}]"


        public fun startSelf(activity: BaseActivity<*>) {

            var intent = Intent(activity, RegisterActivity::class.java)

            activity.startActivity(intent)

        }


    }


    // 国籍选择器
    private var nationalityPopWindow: WheelViewPopupwindow<NationalityBean.DataBean.CountrysBean>? = null

    // 城市
    private var cityPopwindow: WheelViewPopupwindow<CityListBean.DataBean.CitysBean>? = null

    // 性别
    private var genderPopWindow: WheelViewPopupwindow<GenderBean>? = null


    // 生日选择
//    private var birthSelectDialog: DatePickDialog? = null

    //国籍
    private var mCountrys: List<NationalityBean.DataBean.CountrysBean>? = null

    // 城市
    private var mCitys: List<CityListBean.DataBean.CitysBean>? = null

    // 性别
    private var genders: List<GenderBean>? = null

    // head view popWindow
    private var mChiocePopwindow: ChoicePopWindow? = null

    private var isCityClick: Boolean = false

    private var takePhoto: TakePhoto? = null
    private var invokeParam: InvokeParam? = null
    private var cropOptions: CropOptions? = null
    private var compressConfig: CompressConfig? = null
    private var imageUri: Uri? = null


    // 注册提交参数
    private var mNationality: NationalityBean.DataBean.CountrysBean? = null  // 国籍 必填
    private var mCity: CityListBean.DataBean.CitysBean? = null // 城市 必填
    private var mExplain: String = "" //性 必填
    private var mLastNameExplain: String = "" // 名 必填
    private var mGender: GenderBean? = null // 性别 必填
    private var mBirthday: String = "" // 出生日期 必填
    private var mNickName: String = "" //  昵称 选填
    private var mAgencyName: String = "" // 机构/学校 选填
    private var mDescription: String = ""  // 自我介绍
    private var mEmail: String = "" // 自我介绍 // 选填
    private var mMobilePhone: String = "" // 手机号码 必填
    private var mPassword: String = ""  // 密码 必填
    private var mInviteCode: String = "" // 邀请码
    private var mHeadImage: File? = null
    private var language: Int = 1  // 默认问中文  1 中文  2英文


    override fun onCreate(savedInstanceState: Bundle?) {
        getTakePhoto().onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        getTakePhoto().onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)

    }

    private fun getTakePhoto(): TakePhoto {
        if (takePhoto == null) {
            takePhoto = (TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this))) as TakePhoto
        }

        return takePhoto!!
    }

    override fun initView() {


        StatusBarUtils.setDarkMode(this)
        status_bar_main.setTitle(resources.getString(R.string.register_title))
        status_bar_main.setTitleTextSize(16F)
        status_bar_main.setConfirmVisible(View.VISIBLE)
        status_bar_main.setConfirmText("中/En")
        status_bar_main.setConfirmTextSize(15F)

//        getSystemNationality()

        language = if (LocaleUtils.isLocaleEn(this)) 2 else 1
        setUpImagePicker()

        setUpPop()
        initData()
        requestData()


        setUserAgreement()
        setUpListener()

    }

    private fun setUpImagePicker() {

        takePhoto = getTakePhoto()
        cropOptions = CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create()
        compressConfig = CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create()
        takePhoto?.onEnableCompress(compressConfig, true)

    }

    private fun initData() {

        mChiocePopwindow = ChoicePopWindow(this)


        //


//        val list = GsonUtils.getInstance().fromJson<java.util.ArrayList<GenderBean>>(GENDER_JSON, java.util.ArrayList::class.java)
//
//        LogUtils.d(TAG, "list --> ${list[0].getChName()}")


//        var genders = ArrayList<GenderBean>()
//        var man = GenderBean()
//        man.setId(1)
//        man.setChName("男")
//        man.setEnName("女")
//
//        var women = GenderBean()
//        women.setId(2)
//        women.setChName("女")
//        women.setEnName("women")
//
//        genders.add(man)
//        genders.add(women)
//
//
//        var gender = Gender()
//        gender.genders = genders
//
//        var gson = Gson()
//        val toJson = gson.toJson(gender)
//
//
//        val fromJson = GsonUtils.getInstance().fromJson<Gender>(toJson, Gender::class.java)
//
//
//        LogUtils.d(TAG, "${toJson}")
//
//        LogUtils.d(TAG,"${fromJson.genders[0].getChName()}")


        genders = GsonUtils.instance.fromJsonString2list<GenderBean>(GENDER_JSON, GenderBean::class.java)

        genderPopWindow?.let {

            it.setData(genders)


            when (LocaleUtils.getUserLocale(this)) {

                LocaleUtils.LOCALE_ENGLISH -> {

//                    mCountrys.in

                    for (item: GenderBean in genders!!) {


                        item.isEnglish = true
                    }
                }

                LocaleUtils.LOCALE_CHINESE -> {

                    for (item: GenderBean in genders!!) {


                        item.isEnglish = false
                    }
                }


            }

            it.setSelectItemPosition(0)
        }


    }


    /**
     * 获取系统的手机语言状太
     */
    private fun getSystemNationality() {


        var locale = LocaleUtils.getUserLocale(this)

        if (null == locale) {
            return
        }


        if (locale.equals(Locale.ENGLISH)) {
            LogUtils.d(TAG, "当前用户保存的是english")
            LocaleUtils.changeLocale(this, LocaleUtils.LOCALE_ENGLISH)

        } else {
            LogUtils.d(TAG, "当前用户保存的是 中文")

            LocaleUtils.changeLocale(this, LocaleUtils.LOCALE_CHINESE)

        }

        //重新启动

    }

    private fun setUpListener() {


        status_bar_main.setOnConfirmClickListener(object : StatusBarView.OnConfirmClickListener {

            override fun onClick() {

                changeLocale()


            }


        })


        // 注册信息提交
        btn_submit.setOnClickListener {


//            registerSubmitForNet()

            if (mHeadImage != null && mHeadImage!!.exists()) {
                // 如果有头像文件

                mPresenter.uploadImaga(mHeadImage!!)

            } else {

                registerSubmitForNet()
            }


        }


        // 从相册选择图片
        mChiocePopwindow?.setOnButtonChoiceOneListener(object : ChoicePopWindow.OnButtonChoiceOneListener {
            override fun onClick() {

//                var file = File(Environment.getExternalStorageDirectory(), "/temp/${System.currentTimeMillis()}.jpg")
//                if (!file.parentFile.exists()) file.parentFile.mkdirs()
////                return Uri.fromFile(file)
//
////                var contentUri: Uri = FileProvider.getUriForFile(this@RegisterActivity, BuildConfig.APPLICATION_ID.toString() + ".fileprovider", file)
                val imageRri = getImageCropUri()
//
//
                takePhoto?.onPickFromGalleryWithCrop(imageRri, cropOptions)
//                ImageUploadUtil.getInstance().startPictureActivity(this@RegisterActivity)

            }

        })

        mChiocePopwindow?.setOnButtonChoiceTwoListener(object : ChoicePopWindow.OnButtonChoiceTwoListener {
            override fun onClick() {

                val url = getImageCropUri()
                takePhoto?.onPickFromCaptureWithCrop(url, cropOptions)
//                ImageUploadUtil.getInstance().checkPermission(this@RegisterActivity)

            }

        })


        iv_head_img.setOnClickListener {

            // 获取头像图片
            selectHeadImg()


        }


        tv_select_ymd.setOnClickListener {

            // 时间
//            showDateOfbrith()
            showBirthSelect()
        }





        genderPopWindow?.onCommitlistener { data, position ->

            mGender = data

            if (LocaleUtils.isLocaleEn(this)) {
                tv_select_gender.text = mGender!!.getEnName()
            } else {
                tv_select_gender.text = mGender!!.getChName()
            }

        }

        /**
         * 城市确认
         */
        cityPopwindow?.onCommitlistener { data, position ->


            this.mCity = data

            if (LocaleUtils.isLocaleEn(this)) {
                tv_select_city.text = mCity!!.cityEn

            } else {
                tv_select_city.text = mCity!!.cityCn

            }


        }


        /**
         * 国籍选中监听
         */
        nationalityPopWindow?.onCommitlistener { data, position ->


            this.mNationality = data

            if (LocaleUtils.isLocaleEn(this)) {
                tv_select_nationality.text = mNationality!!.countryEnName

            } else {
                tv_select_nationality.text = mNationality!!.countryCnName

            }

            mCitys = null
            mPresenter.getCityListData(mNationality!!.id,language)
            isCityClick = false


        }



        tv_select_gender.setOnClickListener {


            if (genderPopWindow != null && !genderPopWindow!!.isShowing) {
                genderPopWindow!!.animationStyle = R.style.pop_anim
                genderPopWindow!!.showAtLocation(root_main, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, UIUtils.getNavigationHeight(this))
            }
        }



        tv_select_city.setOnClickListener {

            SoftInputUtils.hideSoftInput(this, tv_select_city)

            if (mCitys != null) {

                if (cityPopwindow != null && !cityPopwindow!!.isShowing) {
                    cityPopwindow!!.animationStyle = R.style.pop_anim
                    cityPopwindow!!.showAtLocation(root_main, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, UIUtils.getNavigationHeight(this))
                }

            } else {

//
                if (mNationality == null) {

                    ToastUtil.showCenterToast(this, "请先选择国籍")
                    return@setOnClickListener

                } else {

                    mPresenter.getCityListData(mNationality!!.id, language)

                    isCityClick = true
                }

//                }
            }
//
//
//


        }


        tv_select_nationality.setOnClickListener {
            SoftInputUtils.hideSoftInput(this, tv_select_nationality)
            if (nationalityPopWindow != null && !nationalityPopWindow!!.isShowing) {
                nationalityPopWindow!!.animationStyle = R.style.pop_anim
                nationalityPopWindow!!.showAtLocation(root_main, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            }
        }


    }


    private fun changeLocale() {

//        //
//        if (LocaleUtils.isLocaleEn(this)){
//            LocaleUtils.updateLocale(this,LocaleUtils.LOCALE_CHINESE)
//        }else{
//
//            LocaleUtils.updateLocale(this,LocaleUtils.LOCALE_ENGLISH)
//
//        }

//        LocaleUtils.changeLocale(this)
        //重新启动

        LocaleUtils.changeLocale(this)

    }


    /**
     * 提交注册
     */
    private fun registerSubmitForNet() {


        if (mNationality == null) {
            ToastUtil.showCenterToast(this, R.string.warning_nationality)
            return
        }

        if (mCity == null) {
            ToastUtil.showCenterToast(this, R.string.warning_city)
            return
        }

        mExplain = et_explain.text.toString().trim()
        if (mExplain.isEmpty()) {
            ToastUtil.showCenterToast(this, R.string.warning_explain)
            return

        }

        mLastNameExplain = et_lastname_explain.text.toString().trim()
        if (mLastNameExplain.isEmpty()) {

            ToastUtil.showCenterToast(this, R.string.warning_lastname_explain)
            return
        }


        if (!cb_check.isChecked) {

            // 请先勾选
            ToastUtil.showCenterToast(this, R.string.check_user_agreement)
            return
        }


        if (mGender == null) {

            return
        }

        mBirthday = tv_select_ymd.text.toString().trim()
        LogUtils.d(TAG, "获取的生日  -->  $mBirthday")

        if (mBirthday.isEmpty()) {

            return
        }




        mNickName = et_nickname.text.toString().trim()
        mAgencyName = et_agencyname.text.toString().trim()
        mDescription = et_description.text.toString().trim()
        mEmail = et_email.text.toString().trim()
        mMobilePhone = et_phonenumber.text.toString().trim()

        if (mMobilePhone.isEmpty() && mMobilePhone.length != 11) {
            // 手机号码不能为空且 11位

            return
        }


        if (et_password.text.toString().trim().isEmpty()) {

            ToastUtil.showCenterToast(this, R.string.warning_not_input_password)

            // 为空
            return
        }

        if (et_password.text.toString().trim().isEmpty() && !(et_password.text.toString().trim()).equals(et_password_again.text.toString().trim())) {
            ToastUtil.showCenterToast(this, R.string.warning_two_password_not_same)


            // 密码不一直
            return


        }

        mPassword = et_password.text.toString().trim()


        mInviteCode = et_inviteCode.text.toString().trim()


        var registerBean = RegisterBean()

        registerBean.countryId = mNationality!!.id
        registerBean.cityId = mCity!!.id
        registerBean.lastName = mExplain
        registerBean.firstName = mLastNameExplain
        registerBean.sex = mGender!!.id
        registerBean.birthday = mBirthday
        registerBean.nickname = mNickName
        registerBean.agencyName = mAgencyName
        registerBean.userDesc = mDescription
        registerBean.email = mEmail
        registerBean.mobilePhone = mMobilePhone
        registerBean.password = mPassword
        registerBean.inviteCode = mInviteCode


        if (mImagePath.isNotEmpty()) {
            registerBean.headImage = mImagePath
        }
//        mHeadImage?.let {
//            registerBean.headImage = it
//
//        }


        LogUtils.d(TAG, registerBean.toString())


        mPresenter.registerUser(registerBean)

        showLoadingText("注册中...")


    }

    private fun getImageCropUri(): Uri {

        var file = File(Environment.getExternalStorageDirectory(), "/temp/${System.currentTimeMillis()}.jpg")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        return Uri.fromFile(file)


    }


    /**
     * 获取头像图片
     */
    private fun selectHeadImg() {
        // 判断

        if (mChiocePopwindow == null) {

            mChiocePopwindow = ChoicePopWindow(this)
        }

        mChiocePopwindow?.let {

//            if (LocaleUtils.isLocaleEn(this)) {
//
//                it.setCancelText("cancle")
//                it.setChoiceOneText("select ")
//                it.setChoiceTwoText("take photo")
//            } else {
//
//                it.setCancelText("取消")
//                it.setChoiceOneText("从相册选取图片")
//                it.setChoiceTwoText("拍照")
//            }

            if (!it.isShowing) {
                it.showAtLocation(
                        root_main,
                        BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)

            }
        }


    }


    private var pvTime: TimePickerView? = null
    private var pickerBuilder: TimePickerBuilder? = null

    /**
     *
     */
    private fun showBirthSelect() {


        val selectedDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        //startDate.set(2013,1,1);
        //startDate.set(2013,1,1);
        val endDate = Calendar.getInstance()
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(startDate.get(Calendar.YEAR) - 100, 0, 1)
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH) + 1, endDate.get(Calendar.DAY_OF_MONTH))

        if (pickerBuilder == null) {

            pickerBuilder = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                //选中事件回调


                // 选中

                var time: String = TimesUtils.data2String(date)
                LogUtils.d(TAG, "获取的生日 --> $time")
                tv_select_ymd.text = time
                mBirthday = time

            })
                    .setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
//                    .setCancelText("Cancel") //取消按钮文字
//                    .setSubmitText("Sure") //确认按钮文字
                    .setContentTextSize(18)
                    .setTitleSize(20) //标题文字大小
                    .setTitleText("") //标题文字
                    .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
                    .isCyclic(true) //是否循环滚动
                    .setTitleColor(Color.BLACK) //标题文字颜色
                    .setSubmitColor(Color.BLACK) //确定按钮文字颜色
                    .setCancelColor(Color.BLACK) //取消按钮文字颜色
                    .isDialog(true)
                    .setTitleBgColor(Color.WHITE)
                    .setBgColor(Color.WHITE) //滚轮背景颜色 Night mode
                    .setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
                    .setRangDate(startDate, endDate) //起始终止年月日设定
                    .setLabel("", "", "", "时", "分", "秒") //默认设置为年月日时分秒
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(true) //是否显示为对话框样式


        }



        pvTime = pickerBuilder!!.setCancelText(resources.getString(R.string.cancel))
                .setSubmitText(resources.getString(R.string.data_sure)).build()


        var dialog = pvTime!!.dialog

        if (dialog != null) {

            var params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)

            params.leftMargin = 0
            params.rightMargin = 0
            pvTime!!.dialogContainerLayout.layoutParams = params

            var dialogWindow = dialog.window

            dialogWindow?.let {

                it.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
                dialogWindow.setGravity(Gravity.BOTTOM)

            }
        }

        pvTime!!.show()

    }

    private fun showDateOfbrith() {
//
//        if (birthSelectDialog == null) {
//            birthSelectDialog = DatePickDialog(this)
//            birthSelectDialog!!.setYearLimt(200)
//            birthSelectDialog!!.setType(DateType.TYPE_YMD)
//            birthSelectDialog!!.setMessageFormat("yyyy-MM-dd")
//            birthSelectDialog!!.setOnChangeLisener(null)
//            birthSelectDialog!!.setOnSureLisener { date ->
//                date?.let {
//
//                    var instance = Calendar.getInstance()
//                    instance.time = date
//
//                    var year = instance.get(Calendar.YEAR)
//                    var month = instance.get(Calendar.MONTH)
//                    var day = instance.get(Calendar.DAY_OF_MONTH)
//
//
////                    String str = TimesUtils.timeToString(year, month, day);
////                    regTime = Long.valueOf(str);
////                    regTime = str;
////                    LogUtils.d(TAG, "获取的时间 --> " + regTime);
////                    tvSelectCarRequestDate.setText(year + "年" + (month + 1) + "月" + day + "日");
//                    val time = year.toString() + "年" + (month + 1) + "月" + day + "日"
//                    val s = TimesUtils.time2StringTime(time, "yyyy年MM月dd日", "yyyy-MM-dd")
//                    tv_select_ymd.text = s
//                }
//            }
//
//
//        }
//
//
//        birthSelectDialog?.show()

    }

    /**
     *  网络请求国籍
     */
    private fun requestData() {


        mPresenter.getNationalityData(language)

    }


    /**
     * 创建pop
     */
    private fun setUpPop() {

        // 国籍选择
        nationalityPopWindow = WheelViewPopupwindow<NationalityBean.DataBean.CountrysBean>(this)

        // 城市
        cityPopwindow = WheelViewPopupwindow<CityListBean.DataBean.CitysBean>(this)

        // 性别
        genderPopWindow = WheelViewPopupwindow<GenderBean>(this)


    }


    override fun initPresenter() {

        mPresenter = RegisterPresenter(RegisterModel())
        mPresenter.attachView(this)


    }

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun setTitle(): String {
        return ""
    }

    override fun setRightButton(): String {
        return ""
    }

    override fun setStatusBarColor(): Boolean {

        return true
    }


    fun setUserAgreement() {

        val isEn = LocaleUtils.isLocaleEn(this)

        var contentCn = "我同意《i-gw用户协议》"
        var contentEn = "I agree <i-gw user agreement>"

        var spannable = SpannableStringBuilder(contentCn)

        tv_user_agreement.movementMethod = LinkMovementMethod.getInstance()

        if (isEn) {
            spannable = SpannableStringBuilder(contentEn)
            spannable.setSpan(TextClickPrivacy(this), 7, contentEn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        } else {
            spannable = SpannableStringBuilder(contentCn)

            spannable.setSpan(TextClickPrivacy(this), 3, contentCn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }


        tv_user_agreement.text = spannable

    }

    override fun registerSuccess(data:LoginBean.DataBean) {


        hideLoadingText()

        LoginManager.instance.updateRongUserInfo("$data.id", data.nickName, data.headImage)

        LoginManager.instance.loginSuccess(GsonUtils.instance.toJson(data))

        startMainActivity()




        finish()
    }


    private fun startMainActivity() {


        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun registerFail(code: Int, msg: String) {
        hideLoadingText()

        ToastUtil.showCenterToast(this, msg)
    }


    private var mImagePath: String = ""

    override fun loadHeadImageSuccessful(data: HeadImageBean.DataBean) {
        this.mImagePath = data.attachmentUrl

        GlideUtils.loadImage(this, Constants.BASE_URL + mImagePath, iv_head_img)
        registerSubmitForNet()


    }

    override fun loadHeadImageFail(code: Int, msg: String) {


        ToastUtil.showCenterToast(this, msg)
    }


    override fun fail(o: Any?) {
    }

    override fun success(o: Any?) {
    }

    /**
     *  国籍数据
     */
    fun showNationality(countrys: List<NationalityBean.DataBean.CountrysBean>) {

        this.mCountrys = countrys

        nationalityPopWindow?.let {


            it.setData(mCountrys)
            when (LocaleUtils.getUserLocale(this)) {

                LocaleUtils.LOCALE_ENGLISH -> {

//                    mCountrys.in

                    for (item: NationalityBean.DataBean.CountrysBean in mCountrys!!) {


                        item.isEnglish = true
                    }
                }

                LocaleUtils.LOCALE_CHINESE -> {

                    for (item: NationalityBean.DataBean.CountrysBean in mCountrys!!) {


                        item.isEnglish = false
                    }
                }
            }

            it.setSelectItemPosition(0)
        }


    }

    fun showCityListData(citys: List<CityListBean.DataBean.CitysBean>) {


        this.mCitys = citys;

        cityPopwindow?.let {

            it.setData(mCitys)

            when (LocaleUtils.getUserLocale(this)) {

                LocaleUtils.LOCALE_ENGLISH -> {

//                    mCountrys.in

                    for (item: CityListBean.DataBean.CitysBean in mCitys!!) {


                        item.isEnglish = true
                    }
                }

                LocaleUtils.LOCALE_CHINESE -> {

                    for (item: CityListBean.DataBean.CitysBean in mCitys!!) {


                        item.isEnglish = false
                    }
                }


            }

            it.setSelectItemPosition(0)

        }



        if (cityPopwindow != null && !cityPopwindow!!.isShowing && isCityClick) {
            cityPopwindow!!.animationStyle = R.style.pop_anim
            cityPopwindow!!.showAtLocation(root_main, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, UIUtils.getNavigationHeight(this))
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)

        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)

    }

    override fun takeSuccess(result: TResult?) {
        LogUtils.d(TAG, "takePhoto --> takeSuccess -> ${result?.image?.originalPath}")

        // 返回文件 路径
        result?.let {
            mHeadImage = File(it.image?.originalPath)

//            mPresenter.uploadImaga(mHeadImage!!)
            LogUtils.d(
                    TAG, "获取的文件是否存在 -> ${mHeadImage!!.exists()}"


            )

            if (mHeadImage!!.exists()) {

                GlideUtils.loadLocalImage(this, it.image.originalPath, iv_head_img)

            } else {

                ToastUtil.showCenterToast(this, R.string.select_headimage)
            }

//            loadHeadImageSuccessful
        }

    }

    override fun takeCancel() {
        LogUtils.d(TAG, "takePhoto --> takeCancel")

    }

    override fun takeFail(result: TResult?, msg: String?) {
        LogUtils.d(TAG, "takePhoto --> takeFail -> ${msg}")

    }

    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {

        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam!!.method)
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam
        }

        return type

    }


}
