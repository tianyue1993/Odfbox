package com.odfbox.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/3.
 */

public class UserList extends Entity {
    public ArrayList<UserName> results;
    public String count;

    public class UserName {
        public String fullname;
        public String id;
    }

}
