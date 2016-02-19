package com.gifsoundroid.moosetown.quickaddcontact;

import java.util.Comparator;

/**
 * Created by alexm_000 on 8/16/2015.
 */
public class CompareRecentContacts implements Comparator<RecentContact> {
    @Override
    public int compare(RecentContact r1, RecentContact r2){
        return r1.getTimestamp().compareTo(r2.getTimestamp());
    }

}
