package com.damavandit.apps.chair.models;

import java.util.ArrayList;

public class SectionItems {

    private String headTitle;
    private ArrayList<Items> itemsInSection;

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public ArrayList<Items> getItemsInSection() {
        return itemsInSection;
    }

    public void setItemsInSection(ArrayList<Items> itemsInSection) {
        this.itemsInSection = itemsInSection;
    }
}
