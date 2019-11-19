package dev.sungmin.Shelter;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String cimageUrl;
    public String bimageUrl;
    public String nickname;
    public String comment;
    public String date;
    public int goodcount=0;
    public int badcount=0;
    public String comkey;
    public String borkey;

    public String getBorkey() {
        return borkey;
    }

    public void setBorkey(String borkey) {
        this.borkey = borkey;
    }

    public Map<String, Boolean> good = new HashMap<>();
    public Map<String, Boolean> bad = new HashMap<>();

    public Map<String, Boolean> getGood() {
        return good;
    }

    public void setGood(Map<String, Boolean> good) {
        this.good = good;
    }

    public Map<String, Boolean> getBad() {
        return bad;
    }

    public void setBad(Map<String, Boolean> bad) {
        this.bad = bad;
    }

    public String getComkey() {
        return comkey;
    }

    public void setComkey(String comkey) {
        this.comkey = comkey;
    }

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

    public Comment(String nickname,String comment,String date,int goodcount,int badcount,String comkey,String borkey){
        this.nickname=nickname;
        this.comment=comment;
        this.date=date;
        this.goodcount=goodcount;
        this.badcount=badcount;
        this.comkey=comkey;
        this.borkey=borkey;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("good",good);
        result.put("bad",bad);
        result.put("nickname",nickname);
        result.put("date",date);
        result.put("comment",comment);
        result.put("goodcount",goodcount);
        result.put("badcount",badcount);
        result.put("comkey",comkey);
        result.put("borkey",borkey);
        return result;
    }
}
