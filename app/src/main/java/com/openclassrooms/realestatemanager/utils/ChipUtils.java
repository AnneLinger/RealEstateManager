package com.openclassrooms.realestatemanager.utils;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

/**
 * Useful methods for chips
 */

public class ChipUtils {

    //Remove a chip from a view
    public static void deleteAChipFromAView(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
    }

    public static void deleteAChipFromAList(Chip chip, List<String> list) {
        for(String string : list) {
            String uriStringLastCharacters = string.substring(string.length() - 5);
            if(chip.getText().toString().contains(uriStringLastCharacters)){
                list.remove(string);
            }
        }
    }

    public static void deleteAChipFromAListDependsOnItemPosition(Chip chip, List<String> list) {
        int itemPosition = 0;
        for(String string : list) {
            if(chip.getText().toString().contains(String.valueOf(itemPosition))){
                list.remove(string);
            }
            itemPosition +=1;
        }
    }

}
