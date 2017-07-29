package com.odfbox.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/4.
 */

public class AccessKey extends Entity {

    public String access_key;
    public ArrayList<Org> orgs;

    public class Org extends Entity {

        public int org_id;
        public String user_id;
        public String name;
        public String logo;


    }
}
