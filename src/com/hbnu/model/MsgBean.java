package com.hbnu.model;

import java.io.Serializable;
import java.util.Date;

public class MsgBean implements Serializable {
    private static final long serialVersionUID = -3565218418174747516L;
    private Date date;
    private User from;
    private String to;
    private String msg;

    @Override
    public String toString() {
        return "MsgBean{" +
                "date=" + date +
                ", from=" + from +
                ", to='" + to + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
