package com.odfbox.entity;

/**
 * Created by admin on 2017/7/3.
 */

public class Event extends Entity {
    public int id;

    public String source;

    public String create_user;

    public String manhole;

    public String time;

    public String device;

    public String event;

    public String text;

    public Handle handle;

    public String treat_state;

    public int odf_box;

    public String related;

    public Position position;

    public TaskSheet task_sheet;

    public boolean alarm;

    public class Position extends Entity {
        public String longitude_baidu;
        public String longitude_amap;
        public String longitude_gps;
        public String latitude_baidu;
        public String latitude_amap;
        public String address;
        public String latitude_gps;

    }



    public class Handle {
        public int user;
        public String check_user;
        public String treat_time;
        public String solve_time;
        public String treat_state;
        public String user_treat;
        public String user_check;
    }

}
