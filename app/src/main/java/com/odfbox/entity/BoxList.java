package com.odfbox.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/4.
 */

public class BoxList extends Entity {
    public ArrayList<Odfbox> results;
    public int count;

    @Override
    public String toString() {
        return "BoxList{" +
                "results=" + results +
                '}';
    }
}


