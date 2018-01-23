package com.chinalife.enterprise.domain;

import java.io.Serializable;

public class ImgInfo
        implements Serializable {
    private Integer id;
    private Integer claim_id;
    private String img_type;
    private String img_url;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClaim_id() {
        return this.claim_id;
    }

    public void setClaim_id(Integer claim_id) {
        this.claim_id = claim_id;
    }

    public String getImg_type() {
        return this.img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
