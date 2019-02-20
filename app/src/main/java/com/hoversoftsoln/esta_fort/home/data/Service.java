package com.hoversoftsoln.esta_fort.home.data;

import com.hoversoftsoln.esta_fort.core.data.BaseDao;

import java.util.Map;

public class Service extends BaseDao<Service> {

    private String id, name, desc, image_url;
    private long issue_time;

    @Override
    public Service maptoData(Map<String, Object> _datamap) {
        return null;
    }

    @Override
    public Map<String, Object> datatoMap(Service _data) {
        return null;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    private void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public long getIssue_time() {
        return issue_time;
    }

    private void setIssue_time(long issue_time) {
        this.issue_time = issue_time;
    }
}
