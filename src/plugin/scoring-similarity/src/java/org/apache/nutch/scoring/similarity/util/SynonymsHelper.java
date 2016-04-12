package org.apache.nutch.scoring.similarity.util;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;

public class SynonymsHelper extends Synonyms {

	public static void setPath(Configuration conf) {
		BufferedReader brAdj = new BufferedReader(conf.getConfResourceAsReader((conf.get("cosine.data.adj"))));
		BufferedReader brNoun = new BufferedReader(conf.getConfResourceAsReader((conf.get("cosine.data.noun"))));
		Synonyms.load(brAdj);
		Synonyms.load(brNoun);
	}

	public static String fetchSynonyms(String word) {
		StringBuilder synset = new StringBuilder();
		Set<Set<String>> resultSet = Synonyms.getSynonymSets(word);
		Set<String> result = new HashSet<String>();
		for (Set<String> innerSet : resultSet) {
			for (String syn : innerSet) {
				if (!syn.equals(word) && !syn.contains("_")) {
					synset.append(syn + " ");
				}

			}

		}
		return synset.toString();
	}
}
