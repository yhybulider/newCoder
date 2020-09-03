package com.whllow.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yhy
 * @Date: 2020/8/18
 * @Time: 23:12
 */
@Component
public class SensitiveFilter {
    //日志的定义
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //敏感字符的替换
    private static final String REPALCEMENT = "***";

    //根节点
    private TireNode rootNode = new TireNode();

    // 初始化
    @PostConstruct
    public void init() {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感文件失败" + e.getMessage());
        }


    }

    //将敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TireNode tempnode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TireNode subNode = tempnode.getSubNode(c);

            if (subNode == null) {
                //初始化子节点
                subNode = new TireNode();
                tempnode.addKeywordEnd(c, subNode);
            }
            //指向子节点，进入下一个循环
            tempnode = subNode;
            //设置结束的标志
            if (i == keyword.length() - 1) {
                tempnode.setKeywordEnd(true);
            }
        }
    }

    //真正实现功能
    //过滤敏感词
    /*
     * @Description:
     * @Param: [text] -- 这里是文本，要过滤的
     * @return: java.lang.String --返回过滤后的字符文本
     * @Author: yhy
     * @Date: 2020/8/18
     **/
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        TireNode temp = rootNode;

        int begin = 0;
        int position = 0;

        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            //判断是否是其他符号
            if (isSymbol(c)) {
                if (temp == rootNode) {
                    sb.append(c);
                    begin++;
                }

                // 无论符号在中间还是开头，指针3向下走一步
                position++;
                continue;
            }

            //检查下级节点
            temp = temp.getSubNode(c);

            if (temp == null){
                sb.append(text.charAt(begin));

                position=++begin;
                //重新回到根节点
                temp = rootNode;
            }else if (temp.isKeywordEnd()){
                //敏感词
                sb.append(REPALCEMENT);
                //进入下个位置
                begin=++position;
                //重新回到根节点
                temp = rootNode;
            }else{
                position++;
            }

        }
        //跳出循环后，将最后的计入结果
        sb.append(text.substring(begin));
        return sb.toString();

    }

    private boolean isSymbol(char c) {
        //  c < 0x2E80 || c > 0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphaUpper(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    //定义前缀树的结构
    private class TireNode {
        //关键字的结束标语
        private boolean isKeywordEnd = false;

        //子节点
        private Map<Character, TireNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addKeywordEnd(Character c, TireNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TireNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }


    //


}
