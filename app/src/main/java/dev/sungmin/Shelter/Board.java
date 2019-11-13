package dev.sungmin.Shelter;

import java.util.HashMap;
import java.util.Map;

public class Board{

    public String title;
    public String date;
    public String content;
    public String nickname;
    public String b_key;

    public String getKey() {
        return b_key;
    }

    public void setKey(String key) {
        this.b_key = b_key;
    }

    public int goodCount = 0;


    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public Map<String, Boolean> getGood() {
        return good;
    }

    public void setGood(Map<String, Boolean> good) {
        this.good = good;
    }

    public Map<String, Boolean> good = new HashMap<>();

    public Board(){}

    Board(String title,String date,String content,String nickname,int goodCount,String b_key){
        this.title=title;
        this.date=date;
        this.content=content;
        this.nickname=nickname;
        this.goodCount=goodCount;
        this.b_key=b_key;
    }
    public String getTitle(){
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title);
        result.put("date",date);
        result.put("content",content);
        result.put("nickname",nickname);
        result.put("goodCount",goodCount);
        result.put("good",good);
        result.put("b_key",b_key);
        return result;
    }

}
