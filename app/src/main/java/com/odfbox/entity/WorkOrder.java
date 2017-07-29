package com.odfbox.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/1.
 */

public class WorkOrder extends Entity {
    public int id;
    public Solve solve;
    public ArrayList<Tasks> tasks = new ArrayList<>();
    public String appoint_from_name;
    public String appoint_to_name;
    public String serial;
    public String state;
    public String task_sheet_type;
    public String create_time;
    public String appoint_time;
    public String dead_line_time;
    public String accomplish_time;
    public String close_time;
    public String title;
    public String comment;

    @Override
    public String toString() {
        return "WorkOrder{" +
                "id=" + id +
                ", solve=" + solve +
                ", tasks=" + tasks +
                ", appoint_from_name='" + appoint_from_name + '\'' +
                ", appoint_to_name='" + appoint_to_name + '\'' +
                ", serial='" + serial + '\'' +
                ", state='" + state + '\'' +
                ", task_sheet_type='" + task_sheet_type + '\'' +
                ", create_time='" + create_time + '\'' +
                ", appoint_time='" + appoint_time + '\'' +
                ", dead_line_time='" + dead_line_time + '\'' +
                ", accomplish_time='" + accomplish_time + '\'' +
                ", close_time='" + close_time + '\'' +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
