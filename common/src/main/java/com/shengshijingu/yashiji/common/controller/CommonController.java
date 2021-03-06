package com.shengshijingu.yashiji.common.controller;

import com.shengshijingu.yashiji.common.net.NetApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;

/**
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 * @Describe
 */
public class CommonController extends Controller{


    /**
     * 获取帮助列表
     * @param pageNum
     * @param pageSize
     */
    public void  getHelpList(int pageNum, int  pageSize, Observer observer){


        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);

        ApiSubscribe(NetApi.getApiService().getHelpList(getRequestBody(params)), observer);

    }


    /**
     * 提交反馈
     * @param content
     * @param observer
     */
    public void saveFeedback(String content,int language, int cityId, Observer observer){


        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("language",language);
        params.put("cityId",cityId);

        ApiSubscribe(NetApi.getApiService().saveFeedback(getRequestBody(params)),observer);
    }


    /**
     * 提交详情
     * @param id
     * @param observer
     */
    public void helpDetail(int id , Observer observer){


        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        ApiSubscribe(NetApi.getApiService().helpDetail(getRequestBody(params)),observer);

    }


    public void userInfo(Observer observer) {

        HashMap<String, Object> param = new HashMap<>();

        param.put("", "");

        ApiSubscribe(NetApi.getApiService().userInfo(getRequestBody(param)), observer);
    }


    /**
     * 版本检测
     * @param observer
     */
    public void updateVersion(Observer observer){
        HashMap<String, Object> param = new HashMap<>();

        param.put("", "");

        ApiSubscribe(NetApi.getApiService().updateVersion(getRequestBody(param)),observer);

    }


    /**
     * 开屏页面的回调
     * @param platform
     * @param observer
     */
    public void splashNet(int platform, Observer observer){
        HashMap<String, Object> param = new HashMap<>();

        param.put("platform", platform);

        ApiSubscribe(NetApi.getApiService().splashNet(getRequestBody(param)),observer);

    }
}
