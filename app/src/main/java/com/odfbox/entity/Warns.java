package com.odfbox.entity;

/**
 * Created by admin on 2017/5/4.
 */

public class Warns extends Entity {
    public String id;
    public String time;
    public String device;
    public String event;
    public String text;
    public String treat_state;
    public String related;
    public String state;
    public Position position;
    public boolean alarm;
    public String odf_box;
    public Picture picture;

    public class Picture extends Entity {
        public String id;
        public String url;
        public String thumbnail_url;
        public String md5;
        public String size;
        public String width;
        public String height;
        public String create_date;

    }


    public class Position extends Entity {
        public double longitude_baidu;
        public String longitude_amap;
        public String longitude_gps;
        public double latitude_baidu;
        public String latitude_amap;
        public String address;
        public String latitude_gps;

    }
}


