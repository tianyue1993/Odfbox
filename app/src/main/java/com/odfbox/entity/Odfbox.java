package com.odfbox.entity;


/**
 * Created by admin on 2017/7/1.
 */

public class Odfbox extends Entity {
    public int id;
    public String serial_text = "";//编号
    public boolean alarming;
    public String comment = "";
    public String address = "";
    public SmartLock smart_lock;
    public double longitude_baidu = 0;
    public double latitude_baidu = 0;
    public boolean sensor_installed;
    public String model = "";
    public String status = "";
    public EnvPicture inside_picture;
    public String manufacturer = "";
    public String signature = "";
    public EnvPicture picture;
    public EnvPicture env_picture;
    public String material = "";
    public String cable_serial = "";
    public String business_capacity = "";
    public String name = "";




    public class EnvPicture extends Entity {
        public String id;
        public String url;
        public String thumbnail_url;
        public String md5;
        public String size;
        public String width;
        public String height;
        public String create_date;

    }

    @Override
    public String toString() {
        return "Odfbox{" +
                "id=" + id +
                ", serial_text='" + serial_text + '\'' +
                ", alarming=" + alarming +
                ", comment='" + comment + '\'' +
                ", address='" + address + '\'' +
                ", smart_lock=" + smart_lock +
                ", longitude_baidu=" + longitude_baidu +
                ", latitude_baidu=" + latitude_baidu +
                ", sensor_installed=" + sensor_installed +
                ", model='" + model + '\'' +
                ", status='" + status + '\'' +
                ", inside_picture=" + inside_picture +
                ", manufacturer='" + manufacturer + '\'' +
                ", signature='" + signature + '\'' +
                ", picture=" + picture +
                ", env_picture=" + env_picture +
                ", material='" + material + '\'' +
                ", cable_serial='" + cable_serial + '\'' +
                ", business_capacity='" + business_capacity + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
