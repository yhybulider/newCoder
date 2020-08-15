package com.whllow.community.entity;

//用于存储分页有关的消息
public class Page {

    //当前页码
    private int current = 1;
    //上限
    private int limit = 10;
    //数据总数
    private int rows;
    //分页连接路径
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1)
        this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=1&&limit<=100)
        this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows>=0)
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     *
     * */
    public int getOffset(){
        return (current-1)*limit;
    }
    public int getTotal(){
        if(rows%limit==0){
            return  rows/limit;
        }else
        {
            return  rows/limit + 1;
        }
    }

    public int getFrom(){
        int from  = current-2;
        return from<1?1:from;
    }


    public int getTo(){
        int to  = current+2;
        int total = getTotal();
        return to>total?total:to;
    }



}
