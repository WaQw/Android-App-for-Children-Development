package com.viadroid.app.growingtree.util;

public class Constants {

    public static final String SERVER_HOST = "http://120.27.92.189:8080/v1/";

    //用户接口
    public static final String LOGIN_URL = SERVER_HOST + "user/login";
    public static final String LOGOUT_URL = SERVER_HOST + "user/logout";
    public static final String REGISTER_URL = SERVER_HOST + "user/register";
    public static final String MODIFY_PASS_URL = SERVER_HOST + "user/modifyPass";
    public static final String MODIFY_USERNAME_URL = SERVER_HOST + "user/modifyUsername";

    //宝宝接口
    public static final String GET_BABY_URL = SERVER_HOST + "baby/";
    public static final String ADD_BABY_URL = GET_BABY_URL + "add";

    //记录接口
    public static final String GET_RECORD_URL = SERVER_HOST + "record/";
    public static final String ADD_RECORD_URL = GET_RECORD_URL + "add";
    public static final String GET_RECORD_LIST_URL = GET_RECORD_URL + "listAll";

    //里程碑接口
    public static final String GET_MILESTONE_URL = SERVER_HOST + "milestone/";
    public static final String ADD_MILESTONE_URL = GET_MILESTONE_URL + "add";
    public static final String GET_MILESTONE_LIST_URL = GET_MILESTONE_URL + "list";

    //上传接口
    public static final String IMAGE_UPLOAD_URL = SERVER_HOST + "storage/upload";
}
