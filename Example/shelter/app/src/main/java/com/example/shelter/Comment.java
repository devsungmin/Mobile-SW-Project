package com.example.shelter;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String nickname;
    public String comment;
    public String date;
    public int goodcount=0;
    public int badcount=0;

    public Comment() { }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGoodcount() {
        return goodcount;
    }

    public void setGoodcount(int goodcount) {
        this.goodcount = goodcount;
    }

    public int getBadcount() {
        return badcount;
    }

    public void setBadcount(int badcount) {
        this.badcount = badcount;
    }

    public Comment(String nickname,String comment,String date,int goodcount,int badcount){
        this.nickname=nickname;
        this.comment=comment;
        this.date=date;
        this.goodcount=goodcount;
        this.badcount=badcount;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("nickname",nickname);
        result.put("date",date);
        result.put("comment",comment);
        result.put("goodcount",goodcount);
        result.put("badcount",badcount);
        return result;
    }
}
