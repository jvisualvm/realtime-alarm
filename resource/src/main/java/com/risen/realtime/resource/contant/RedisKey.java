package com.risen.realtime.resource.contant;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/1 21:21
 */
public class RedisKey {

    /********************************业务定时任务******************************/
    //SPC第一个任务的任务执行位置,其余任务以此类推
    public static final String STR_SPC_CZ_302_DP_TASK_KEY = "str_spc_cz_302_dp_task_key";
    public static final String STR_SPC_CZ_302_ZR_TASK_KEY = "str_spc_cz_302_zr_task_key";
    public static final String STR_SPC_CZ_302_KS_TASK_KEY = "str_spc_cz_302_ks_task_key";
    public static final String STR_SPC_CZ_302_ZM_TASK_KEY = "str_spc_cz_302_zm_task_key";
    public static final String STR_SPC_CZ_302_BM_TASK_KEY = "str_spc_cz_302_bm_task_key";
    public static final String STR_SPC_CZ_302_BDH_TASK_KEY = "str_spc_cz_302_bdh_task_key";
    public static final String STR_SPC_CZ_302_SW_TASK_KEY = "str_spc_cz_302_sw_task_key";


    /********************************背膜数据聚合同步任务******************************/
    public static final String STR_SPC_CZ_302_BM_SYNC_TASK_KEY = "str_spc_cz_302_bm_sync_task_key";


    /********************************数据监听器******************************/
    public static final String DEVICE_ALARM_SUB_TASK_KEY = "device_alarm_sub_task_key";


}
