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
@Component
public class SensitiveFiliter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFiliter.class);

    private static final String REPLACESTRING = "***";

    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try(
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                ){
            String keyword = null;
            while((keyword=br.readLine())!=null){
                this.addKeyword(keyword);
            }

        }
        catch (IOException e){
            logger.error("加载敏感词失败" + e.getMessage());
        }

    }

    public void addKeyword(String keyword){

        TrieNode tmpNode = rootNode;
        int len = keyword.length();
        for(int i=0;i<len;i++){

            Character ch = keyword.charAt(i);
            TrieNode sonNode = tmpNode.getSon(ch);
            if(sonNode == null){
                sonNode = new TrieNode();
                tmpNode.addSon(ch,sonNode);
            }

            tmpNode = sonNode;
            if(i==len-1){
                tmpNode.setKeyWordEnd(true);
            }
        }
    }


    //过滤敏感词
    public String Filiter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        TrieNode tmp = rootNode;
        int begin = 0;
        int end = 0;
        StringBuilder sb = new StringBuilder();
        int len = text.length();

        while(end<len){
            char ch = text.charAt(end);
            if(isSymbol(ch)){
                if (tmp==rootNode){//或者begin==end
                    sb.append(ch);
                    begin++;
                }
                end++;
                continue;
            }
            tmp = tmp.getSon(ch);
            if(tmp==null){
                sb.append(text.charAt(begin));
                end = ++begin;
                tmp = rootNode;
            }else if(tmp.isKeyWordEnd){
                sb.append(REPLACESTRING);
                begin = ++end;
                tmp = rootNode;
            }else{
                end++;
            }
        }
        sb.append(text.substring(begin));
        return  sb.toString();
    }


    private boolean isSymbol(Character c){
        //东亚文字的范围0x2E80~0x9FFF
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }





    private class TrieNode{

        private Boolean isKeyWordEnd = false;

        public Boolean getKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(Boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        private Map<Character,TrieNode> sonNodes  = new HashMap<>();

        public void addSon(Character ch,TrieNode node){
            sonNodes.put(ch,node);
        }

        public TrieNode getSon(Character ch){
            return sonNodes.get(ch);
        }


    }



}
