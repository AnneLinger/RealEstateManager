package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;

/**
 * Useful methods for popup menus
 */
public class PopupUtils {

    //Create a Popup menu
    public static PopupMenu createPopupMenu(Context context, View view) {
        return new PopupMenu(context, view);
    }

    //Create a menu in a Popup menu
    public static Menu createMenu(PopupMenu popupMenu) {
        return popupMenu.getMenu();
    }
}
