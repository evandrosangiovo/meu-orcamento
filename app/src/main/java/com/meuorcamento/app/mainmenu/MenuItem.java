package com.meuorcamento.app.mainmenu;


import android.app.Fragment;
import android.util.Log;



public class MenuItem {
    private String title;
    private Fragment fragment;

    private String subtitle;
    private int icon;
    private EnumView enumView;

    public MenuItem(Fragment fragment, String title, String subtitle, int icon, EnumView enumView) {
        this.fragment = fragment;
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
        this.enumView = enumView;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getIcon() {
        return icon;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTag() {
        Log.i("getTag", fragment.getClass().getName());
        return fragment.getClass().getName();
    }

    public EnumView getEnumView() {
        return enumView;
    }
}