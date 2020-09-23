package com.fm.modules.app.commons.permissions;

import android.app.Activity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class Permisos {
    public static void comprobarPermiso(Activity activity,String typePermission){
    boolean a = ActivityCompat.shouldShowRequestPermissionRationale(activity, typePermission);
    if (a){
        Toast.makeText(activity,"Favor Authorice Permisos",Toast.LENGTH_SHORT).show();
    }else {
        ActivityCompat.requestPermissions(activity,new String[]{typePermission},1);
    }
    }
    public static void comprobarPermisos(Activity activity,String[] typePermission){
        List<String> list = new ArrayList<>();
        for (String t : typePermission){
             if (ActivityCompat.shouldShowRequestPermissionRationale(activity, t)){
                 list.add(t);
             };
        }
        if (list.isEmpty()){
            Toast.makeText(activity,"Favor Authorice Permisos",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(activity,typePermission,1);
        }
    }
}
