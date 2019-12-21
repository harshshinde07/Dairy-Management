package com.kshitijharsh.dairymanagement.utils;

import java.text.DecimalFormat;

public class RoundUtil {

    public static Float roundTwoDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(d));
    }

}
