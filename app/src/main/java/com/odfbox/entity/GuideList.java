package com.odfbox.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/4.
 */

public class GuideList extends Entity {
    public ArrayList<Guide> results;
    public String count;

    public class Guide {

        public int id;
        public String document;
        public String revision;
        public String comment;
        public int size;
        public String md5;
        public String uploader_name;
        public String file_name;
        public int uploader;
        public String upload_time;

    }
}


