package com.aiyangniu.common.utils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 分词工具类
 *
 * 分词方法：
 * 1> 基于字符串匹配：把中文句子按照一定的策略将待分析的汉字串与已知且足够大的中文词典库进行比对，从而达到分词效果
 * 2> 基于理解：让计算机模拟人对句子的理解进行分词
 * 3> 基于统计：找出上下文中出现较多的汉字组合，将这些组合视为词汇，代入到原文中进行分词
 *
 * 基于字符串匹配，常用的分词策略：
 * 正向最大匹配法：匹配的开始为句首
 * 逆向最大匹配法：匹配的开始为句尾
 * 最少切分法
 *
 * @author lzq
 * @date 2023/09/26
 */
public class MyIkAnalyzerUtil {

    /** 切分后的结果串 **/
    private String splitResult = "";
    /** 游标指针 **/
    private int markIndex;
    /** 最大取词长 **/
    private int maxLength;
    /** 总最大取词长 **/
    private int totalMaxLength;
    /** 分词字典 **/
    private Set<String> dictionary;

    public MyIkAnalyzerUtil(int maxLen){
        this.maxLength = maxLen;
        this.totalMaxLength = maxLen;
        try {
            this.dictionary = loadFile();
        } catch (IOException ex) {
            Logger.getLogger(MyIkAnalyzerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MyIkAnalyzerUtil(){
        this.maxLength = 3;
        this.totalMaxLength = 3;
        try {
            this.dictionary = loadFile();
        } catch (IOException ex) {
            Logger.getLogger(MyIkAnalyzerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Set<String> loadFile() throws IOException{
        // 读取字典
        Set<String> dictionary = new HashSet<String>();
        String filePath = "D:\\workspace-learning5\\mall-learning-cloud\\mall-common\\src\\main\\resources\\dict.txt";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String tmp;
        while((tmp = reader.readLine()) != null){
            String[] split = tmp.split(",");
            String word = split[0];
            dictionary.add(word);
        }
        return dictionary;
    }

    public String segment(String source){
        int len= totalMaxLength;
        this.markIndex = source.length();
        int frompos = this.markIndex;
        rmm(source, maxLength, markIndex);
        // 将结果按顺序输出
        String[] token = splitResult.split("/");
        String result = "";
        for(int i = token.length-1; i > 0 ; i--){
            result += token[i] + "/ ";
        }
        return result;
    }

    public String getSubString(String source, int markIndex, int len){
        int startIndex = markIndex - len;
        // 判断越界条件
        while(startIndex < 0){
            startIndex += 1;
        }
        return source.substring(startIndex, markIndex);
    }

    public void rmm(String source, int len, int frompos){
        if(markIndex < 0) {
            return;
        }
        String sub = getSubString(source, markIndex, len);
        if(dictionary.contains(sub)){
            // 匹配成功
            splitResult += "/" + sub ;
            markIndex = markIndex - maxLength;
            maxLength = totalMaxLength;
        } else {
            // 不匹配
            if(maxLength > 1){
                maxLength = maxLength - 1;
            } else{
                splitResult += "/" + sub ;
                markIndex -= 1;
                maxLength = totalMaxLength;
            }
        }
        rmm(source, maxLength, markIndex);
    }

    public static void main(String[] args) {

        MyIkAnalyzerUtil util = new MyIkAnalyzerUtil();
        String source = "爱迪生发明了电灯泡";
        String result = util.segment(source);
        System.out.println(result);

    }
}
