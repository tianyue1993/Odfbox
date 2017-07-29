package com.odfbox.entity;

import java.util.ArrayList;

public class Solve extends Entity {
    public int id;
    public String worker_comment;
    public boolean finish;
    public ArrayList<Attachments> attachments;

    public class Attachments extends Entity {
        public String id;
        public String url;
    }
}