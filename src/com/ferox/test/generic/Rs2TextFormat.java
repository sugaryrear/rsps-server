package com.ferox.test.generic;

import com.ferox.util.Utils;

public class Rs2TextFormat {

    public static void main(String[] args) {
        System.out.printf("%s %s %s %s %s%n",
            Utils.formatText("LA HACIENDA"),
            Utils.formatText("LA HACIeNdA"),
            Utils.formatText("lA H2CIeNdA"),
            Utils.formatText("lA H2cIeNdA"),
            Utils.formatText("la 2HACIeNdA")
        );
    }
}
