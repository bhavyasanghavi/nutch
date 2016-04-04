package org.apache.nutch.scoring.similarity.util;

import java.util.HashSet;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;

public class SynonymsHelper extends Synonyms{

	private static Set<Set<String>> getSynonymList(String word)
	{
		return Synonyms.getSynonymSets(word);
	}
	
	public static String fetchSynonyms(String word)
	{
		StringBuilder synset = new StringBuilder();
		Set<Set<String>> resultSet = getSynonymList(word);
		Set<String> result = new HashSet<String>();
		for(Set<String> innerSet : resultSet)
		{
			for(String syn : innerSet)
			{
				if(!syn.equals(word) && !syn.contains("_")){
					synset.append(syn+" ");
				}

			}

		}
		return synset.toString();
	}
}

