package com.tj007.divbucketmvp.model;

public final class NETWORK_FETCHER_ERROR {
    private NETWORK_FETCHER_ERROR(){}
    public static String NO_NETWORK="[NETWORKERROR_NO_NETWORK]";//没有连接网络
    public static String NO_DATA="[NETWORKERROR_NO_DATA]";//这条路径没有数据
    public static String G_ERROR="[UNKNOWN_ERROR]";//一般错误
    public static String TIMEOUT="[NETWORKERROR_TIMEOUT]";//无法确认网络是否畅通，但是访问已经超时
    public static String DONE="[DONE]";//不是错误，标识一系列的工作已经完成
}
