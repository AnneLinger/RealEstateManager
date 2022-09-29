package com.openclassrooms.realestatemanager.utils;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * Useful method for chips
 */

public class ChipUtils {

    //Remove a chip from a view
    public static void deleteAChipFromAView(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
    }
}
