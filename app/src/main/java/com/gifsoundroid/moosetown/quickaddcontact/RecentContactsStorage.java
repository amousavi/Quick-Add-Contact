package com.gifsoundroid.moosetown.quickaddcontact;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by alexm_000 on 8/16/2015.
 */
public class RecentContactsStorage {

    final static String filename = "recentcontacts";
    public static ArrayList<RecentContact> getContacts(Context context) throws Exception{

        FileInputStream fis = context.openFileInput(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<RecentContact> result = (ArrayList<RecentContact>) ois.readObject();

        if (result == null) result = new ArrayList<RecentContact>();

        ois.close();
        fis.close();
        return result;
    }

    public static  void saveContacts(Context context, ArrayList<RecentContact> contacts) throws Exception{
        if(contacts == null) return;
        FileOutputStream fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(contacts);
        oos.close();
        fos.close();

    }

}
