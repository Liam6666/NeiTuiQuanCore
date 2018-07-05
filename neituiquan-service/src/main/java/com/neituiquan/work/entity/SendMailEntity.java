package com.neituiquan.work.entity;

import java.io.Serializable;

public class SendMailEntity implements Serializable {

    private String mailPath;

    private String title;

    private String content;

    public String getMailPath() {
        return mailPath;
    }

    public void setMailPath(String mailPath) {
        this.mailPath = mailPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
