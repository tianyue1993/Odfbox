package com.odfbox.entity;

/**
 * Created by admin on 2017/7/11.
 */

public class Org extends Entity {

    public String id;
    public String name;
    public String logo;
    public Integer total_odf_box;
    public Integer odf_box_monitored;
    public String odf_box_count;

    @Override
    public String toString() {
        return "Org{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", total_odf_box=" + total_odf_box +
                ", odf_box_monitored=" + odf_box_monitored +
                ", odf_box_count='" + odf_box_count + '\'' +
                '}';
    }
}
