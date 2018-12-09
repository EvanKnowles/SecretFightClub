package za.co.knonchalant.liketosee.domain.fightclub.enums;

import sun.print.PSPrinterJob;

import java.util.Arrays;

public enum EApprovalStatus {

    PENDING("meh"), APPROVED("Yay"), DENIED("Nay");

    private String text;

    EApprovalStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // really bored of writing plumbing ;)
    public static EApprovalStatus from(String text) {
        return Arrays.stream(EApprovalStatus.values()).filter(v->v.getText().equalsIgnoreCase(text)).findFirst().orElse(null);
    }
}
