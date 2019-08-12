package com.xmbl.util.XmlParseUtil;

import com.xmbl.util.RegexUtil;
import com.xmbl.util.file.FileContentUtil;
import com.xmbl.util.pathUtil.ProjectPathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 小米xml解析工具类
 *
 * @author sunbenbao
 */
public class XmlParseUtil {

    /**
     * 根据标签名查看是否含有某个标签的标签体
     *
     * @param labelName 标签名
     * @return
     */
    private static boolean isContainLabName(String labelStr, String labelName) {
        if (labelStr == null) {
            return false;
        }
        labelStr = labelStr.trim();
        int length = labelStr.length();
        if (length <= 0) {
            return false;
        }
        int startIndex = labelStr.indexOf("<" + labelName + " ");
        int endIndex = labelStr.indexOf("</" + labelName + ">");
        int endLabelIndex = labelStr.indexOf("/>");
        if (startIndex >= 0 && ((endLabelIndex <= length && endLabelIndex > 0) || (endIndex > 0 && endIndex <= length))) {
            return true;
        }
        return false;
    }

    /**
     * 从xml字符串中根据标签名 获取标签字符串
     *
     * @param xmlStr
     * @param labelName
     * @return
     */
    private static List<String> getLabelStrLst(String xmlStr, String labelName) {
        String[] xmlStrArr = xmlStr.split("\n");
        List<String> xmlStrLst = Arrays.asList(xmlStrArr);
        List<String> labelStrLst = new ArrayList<String>();
        for (String labelStr : xmlStrLst) {
            if (isContainLabName(labelStr, labelName)) {
                labelStrLst.add(labelStr.trim());
            }
        }
        return labelStrLst;
    }

    /**
     * 根据xml片段获取 某个标签的所有属性及其对应字符串
     *
     * @param xmlStr    xml片段字符串 或者xml字符串
     * @param labelName 标签名
     * @return
     */
    private static List<Map<String, String>> getKeyValMapLstByXmlStrAndLabelName(String xmlStr, String labelName) {
        List<Map<String, String>> keyValMapLst = new ArrayList<Map<String, String>>();
        List<String> labelStrLst = getLabelStrLst(xmlStr, labelName);
        Map<String, String> keyValMap = null;
        for (String labelStr : labelStrLst) {
            keyValMap = RegexUtil.getKeyValOfXmlStr(labelStr);
            keyValMapLst.add(keyValMap);
        }
        return keyValMapLst;
    }

    /**
     * 根据文件名 和 标签名 在xml文件中获取所有标签内的键值对字符串
     *
     * @param fileName  文件名
     * @param labelName 标签名
     * @return
     * @throws Exception
     */
    private static List<Map<String, String>> getKeyValMapLstByFileNameAndLabelName(String fileName, String labelName) throws Exception {
        String xmlStr = FileContentUtil.readFile(fileName);
        List<Map<String, String>> mapLst = getKeyValMapLstByXmlStrAndLabelName(xmlStr, labelName);
        return mapLst;
    }

    /**
     * 根据文件路径 和 标签名 在xml文件中获取所有标签内的键值对字符串
     *
     * @param filePath  文件路径
     * @param labelName 文件名
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getKeyValMapLstByFilePathAndLabelName(String filePath, String labelName) throws Exception {
        String fileName = ProjectPathUtil
                .getProjectPath(filePath);
        List<Map<String, String>> mapLst = getKeyValMapLstByFileNameAndLabelName(fileName, labelName);
        return mapLst;
    }

    public static void main(String[] args) {
        try {
            List<Map<String, String>> mapLst = getKeyValMapLstByFilePathAndLabelName("xml/dev/main.commodityConfig.xml", "commodityConfig");
            System.out.println(mapLst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
