package za.co.knonchalant.telegram.handlers.fightclub.details;

import za.co.knonchalant.candogram.domain.BaseDetail;

public class RegisterDetails extends BaseDetail {
    private String chosenClass;

    public void setChosenClass(String chosenClass) {
        this.chosenClass = chosenClass;
    }

    public String getChosenClass() {
        return chosenClass;
    }
}
