package com.aiyangniu.common.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * 敏感词工具类
 *
 * @author lzq
 * @date 2023/09/22
 */
@Component
public class SensitiveWordUtil implements ApplicationRunner {

    /**
     * 敏感词集合
     */
    private static HashMap sensitiveWordMap;

    /**
     * 初始化敏感词库（基于数据库存储）
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        for (String sensitiveWord : sensitiveWordSet) {
            sensitiveWordMap.put(sensitiveWord, sensitiveWord);
        }
    }

    /**
     * 判断文字是否包含敏感字符
     */
    public static boolean contains(String txt) throws Exception {
        List<String> wordList = segment(txt);
        for (String word : wordList) {
            if (sensitiveWordMap.get(word) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文字中的敏感词
     */
    public static Set<String> getSensitiveWord(String txt) throws IOException {
        Set<String> sensitiveWordList = new HashSet<>();

        List<String> wordList = segment(txt);
        for (String word : wordList) {
            if (sensitiveWordMap.get(word) != null) {
                sensitiveWordList.add(word);
            }
        }
        return sensitiveWordList;
    }

    /**
     * 对语句进行分词
     */
    private static List segment(String text) throws IOException {
        List<String> list = new ArrayList<>();
        StringReader stringReader = new StringReader(text);
        // true-最大颗粒切分 false-最细颗粒切分
        IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
        Lexeme lex;
        while ((lex = ikSegmenter.next()) != null) {
            list.add(lex.getLexemeText());
        }
        return list;
    }
}
