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

    //Remove a chip from a list
    public static void deleteAChipFromAList(Chip chip, List<String> list) {
        String chipToRemove = chip.getText().toString();
        list.remove(chipToRemove);
    }

}
