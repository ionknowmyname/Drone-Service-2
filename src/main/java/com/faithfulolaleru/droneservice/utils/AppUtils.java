package com.faithfulolaleru.droneservice.utils;

import com.faithfulolaleru.droneservice.entity.Drone;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppUtils {

    public static boolean validateDroneEntityToSave(Drone entity) {
        if(entity.getBatteryCapacity() < 0 || entity.getBatteryCapacity() > 100
                || entity.getWeight() > 500) {

            return false;
        }
        return true;
    }

    public static String generateRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789" + "abcdefghijklmnopqrstuvxyz" + "_";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
