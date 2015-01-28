package com.xplorer.hope.object;

import com.mobsandgeeks.adapters.Sectionizer;

/**
 * Created by Raghavendra on 25-01-2015.
 */
public class CategorySectionizer implements Sectionizer<WorkAd> {
    @Override
    public String getSectionTitleForItem(WorkAd workad) {
        return workad.getCategory();
    }
}
