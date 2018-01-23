package com.chinalife.enterprise.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class ClaimInfo
        implements Serializable {
    private Integer id;
    private String add_user;
    private Integer type;
    private Timestamp add_time;
    private String status;
    private String file_str;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdd_user() {
        return this.add_user;
    }

    public void setAdd_user(String add_user) {
        this.add_user = add_user;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getAdd_time() {
        return this.add_time;
    }

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile_str() {
        return file_str;
    }

    public void setFile_str(String file_str) {
        this.file_str = file_str;
    }
}
