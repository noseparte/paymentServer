package com.xmbl.util.xiaomiUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小米商品信息工具类
 *
 * @author sunbenbao
 */
public class XiaomiGoodsInfoUtil {

    /**
     * 根据mapLst 和 键值对 匹配 获取 list 中的map对象
     *
     * @param mapLst
     * @param key
     * @param value
     * @return
     */
    public static Map<String, String> getOneGoodsMapByKeyVal(List<Map<String, String>> mapLst, String key, String value) {
        if (mapLst == null || mapLst.size() <= 0 || StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return new HashMap<String, String>();
        }
        String valTemp = "";
        for (Map<String, String> tempMap : mapLst) {
            valTemp = tempMap.get(key);
            if (value.equals(valTemp)) {
                return tempMap;
            }
        }
        // 没找到返回null
        return new HashMap<String, String>();
    }

    /**
     * 根据maplist key value 获取指定的值oneKey对应的 value 字符串
     *
     * @param mapLst
     * @param key
     * @return
     */
    @SuppressWarnings("unused")
    private static String getOneGoodsValByKey(List<Map<String, String>> mapLst, String key, String value, String oneKey) {
        Map<String, String> map = getOneGoodsMapByKeyVal(mapLst, key, value);
        if (map != null) {
            return map.get(oneKey);
        }
        return null;
    }

    /**
     * 从map中获取onekey 对应的value
     *
     * @param map
     * @param oneKey
     * @return
     */
    public static String getOneGoddsValByMapAndKey(Map<String, String> map, String oneKey) {
        if (map != null) {
            return map.get(oneKey);
        }
        return null;
    }
}
