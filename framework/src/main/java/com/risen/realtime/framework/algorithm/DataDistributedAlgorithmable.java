package com.risen.realtime.framework.algorithm;

import com.risen.helper.util.LimitLinkedList;
import com.risen.realtime.framework.dto.DataAroundDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/5/1 17:06
 * @description 数据分布简单算法
 */
public class DataDistributedAlgorithmable {


    /**
     * 判断是递增还是递减
     *
     * @param lst
     * @return
     */
    public static Map<Boolean, Boolean> ascDescAgain(List<Float> lst) {
        Map<Boolean, Boolean> result = new HashMap<>();
        int upCount = 0;
        int downCount = 0;
        int index = 0;
        for (Float item : lst) {
            if (index != 0 && item > lst.get(index - 1)) {
                ++upCount;
            }
            if (index != 0 && item < lst.get(index - 1)) {
                ++downCount;
            }
            ++index;
        }
        if (upCount == lst.size() - 1) {
            result.put(true, true);
            return result;
        }
        if (downCount == lst.size() - 1) {
            result.put(true, false);
            return result;
        }
        result.put(false, false);
        return result;
    }

    /**
     * 判断连续N个点分布在周围
     *
     * @param lst
     * @return
     */
    public static boolean dataAround(List<DataAroundDTO> lst) {
        LimitLinkedList<Boolean> limitLinkedList = new LimitLinkedList(2);
        boolean isExists = true;
        for (DataAroundDTO item : lst) {
            if (limitLinkedList.size() == 2) {
                //如果同号则不满足条件
                if (limitLinkedList.get(0).equals(limitLinkedList.get(1))) {
                    isExists = false;
                    return isExists;
                }
            }
            if (item.getSource() > item.getTarget()) {
                limitLinkedList.addRemoveHead(true);
            }
            if (item.getSource() < item.getTarget()) {
                limitLinkedList.addRemoveHead(false);
            }
        }
        return isExists;
    }


}
