package za.co.knonchalant.telegram.handlers.fightclub.details;

import za.co.knonchalant.candogram.domain.BaseDetail;

public class ItemDetails extends BaseDetail {

    private String name;
    private double damage;
    private int affectedKeyboard;
    private int itemId;

    public ItemDetails(String name) {
        this.name = name;
    }

    public ItemDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setAffectedKeyboard(int affectedKeyboard) {
        this.affectedKeyboard = affectedKeyboard;
    }

    public int getAffectedKeyboard() {
        return affectedKeyboard;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }
}
