package com.tc.tools;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符操作类
 */
public class TcTextUtils {
    /**
     * 清除文本中script,style等标签，只保留文本
     * @param inputString 输入的字符串
     * @return 纯文本
     */
    public static String htmlToText(String inputString) {
        if (StringUtils.isBlank(inputString)) {
            return null;
        }
        String htmlStr = inputString;
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        Pattern p_html1;
        Matcher m_html1;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); //过滤html标签

            htmlStr = htmlStr.replaceAll("&nbsp;", "");
            htmlStr = StringEscapeUtils.unescapeHtml4(htmlStr);
            if (StringUtils.isNoneEmpty(htmlStr)) {
                htmlStr = htmlStr.trim();
            }
            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("html2Text: " + e.getMessage());
        }

        return textStr;//返回文本字符串
    }
}
