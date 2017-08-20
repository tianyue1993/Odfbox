package com.odfbox.entity;

import java.util.List;

/**
 * Created by admin on 2017/6/7.
 */

public class VersionOdfbox extends Entity {

    public int count;
    public String next;
    public String previous;
    public List<VersionData> results;

    public class VersionData extends Entity {
        public int id;
        public String app;
        public String platform;
        public String revision;
        public String comment;
        public String tag;
        public String md5;
        public int size;
        public int uploader;
        public String uploader_name;
        public String upload_time;
        public String file_name;
    }
}
