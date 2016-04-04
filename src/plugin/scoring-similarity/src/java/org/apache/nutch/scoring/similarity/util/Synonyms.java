package org.apache.nutch.scoring.similarity.util;

import java.util.*;
import java.io.*;
import org.apache.hadoop.conf.Configuration;

public abstract class Synonyms {
    private static final Map<String, Set<Set<String>>> WORD_TO_SYNOYMYM_SETS = new HashMap<String, Set<Set<String>>>(15000);
    private static final Set<Set<String>> EMPTY_SET = Collections.unmodifiableSet(new HashSet<Set<String>>());

    static {
        synchronized (WORD_TO_SYNOYMYM_SETS) {
            load("data.adj");
           // load("WordNetData/data.adv");
           // load("WordNetData/data.verb");
            load("data.noun");
        }
    }

    private static void load(String path) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            ClassLoader classLoader = Synonyms.class.getClassLoader();
            inputStream = classLoader.getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    private static void processLine(String line) {
        if ((line.length() > 17) && (line.charAt(0) == '0')) {
            // the data we want starts at the 17th character
            line = line.substring(17);

            Set<String> synonymSet = new HashSet<String>();
            StringTokenizer st = new StringTokenizer(line, " ");
            while (st.hasMoreElements()) {
                String word = st.nextToken();

                if (word.startsWith("00")) {
                    break;
                }

                if (word.length() > 2 && Character.isLetter(word.charAt(0))) {
                    synonymSet.add(word);
                }
            }

            if (synonymSet.size() > 1) {
                synonymSet = Collections.unmodifiableSet(synonymSet);
                for (String word : synonymSet) {
                    Set<Set<String>> synonymSetsForThisWord = WORD_TO_SYNOYMYM_SETS.get(word);
                    if (synonymSetsForThisWord == null) {
                        synonymSetsForThisWord = new HashSet<Set<String>>();
                        WORD_TO_SYNOYMYM_SETS.put(word, synonymSetsForThisWord);
                    }
                    synonymSetsForThisWord.add(synonymSet);
                }
            }
        }
    }

    public static Set<Set<String>> getSynonymSets(String word) {
        synchronized (WORD_TO_SYNOYMYM_SETS) {
            Set<Set<String>> synonymSets = WORD_TO_SYNOYMYM_SETS.get(word);
            return ((synonymSets != null) ? Collections.unmodifiableSet(synonymSets) : EMPTY_SET);
        }
    }
}
