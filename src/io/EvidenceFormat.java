package io;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by chaoli on 3/10/17.
 */
public class EvidenceFormat {

    public static Map<Integer, Integer> readUAI(String evidFileName) {

        Map<Integer, Integer> evid = new LinkedHashMap<>();

        Scanner is = new Scanner(evidFileName);

        int nrObs = is.nextInt();

        for (int j = 0; j < nrObs; j++) {
            int label, val;

            label = is.nextInt();
            val = is.nextInt();

            evid.put(label, val);

        }

        return null;
    }
}