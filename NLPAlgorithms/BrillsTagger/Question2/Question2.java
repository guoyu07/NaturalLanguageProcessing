package Question2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Question2 {

	static ArrayList<String> allwords;
	static TreeSet<String> alltags = new TreeSet<String>();
	static HashMap<String, String> uniLikelyTags = new HashMap<>();
	static ArrayList<String> correcttags;
	static ArrayList<String> currenttags;

	public static void main(String[] args) throws IOException {

		PartA();
		Queue<Table> rules = PartB();
		String Sentence = "";
		if (args.length > 0) {
			Sentence = args[0];
		} else {
			Sentence = "The_DT president_NN wants_VBZ to_TO control_VB the_DT board_NN 's_POS control_NN";
		}
		PartC(Sentence, rules);

	}

	static void PartC(String input, Queue<Table> rules) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(input);

		ArrayList<String> inputwords = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			inputwords.add(tokenizer.nextToken());
		}
		int changedCount = 0;
		ArrayList<String> inputcorrtags = new ArrayList<String>();
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> inputcurrtags =new ArrayList<String> ();
		
		for (String word : inputwords) {
			int i = word.indexOf('_');
			String key = word.substring(0, i);
			words.add(key);
			String tag = word.substring(i + 1, word.length());
			inputcorrtags.add(tag);
			if (uniLikelyTags.containsKey(key)) {
				String tagC = uniLikelyTags.get(key);
				
				if (!tagC.equals(tag)) {
					inputcurrtags.add(tagC);
					changedCount++;
				}
				else{
					inputcurrtags.add(tag);
				}
			}
			
		}
		int length = 30;
		FileWriter wr1 = new FileWriter("POSTaggingforgivenSentenceformodelA.txt");
		wr1.write("After Applying unigram model(a) on the given Sentence it is changed to ");
		wr1.write("\n");
		
		
		for (int i = 0; i < words.size(); i++) {
			wr1.write(words.get(i));
			AddSpaces(length - words.get(i).length(), wr1);
		}
		wr1.write("Error rate %");
		wr1.write("\n");
		
		for(int i=0; i<inputcurrtags.size();i++){
			wr1.write(inputcurrtags.get(i));
			AddSpaces(length - inputcurrtags.get(i).length(), wr1);
			
		}
		float unierror=100*(float) changedCount / inputwords.size();
		
		wr1.write(unierror+"");
		wr1.flush();
		wr1.close();
		
		System.out.println(
				"The error rate by applying unigram model(a) on given sentence  is " + unierror);

		
		
		
		
	   FileWriter wr = new FileWriter("POSTaggingforgivenSentenceformodelB.txt");
		wr.write("words");
		
		AddSpaces(length - ("words").length(), wr);
		for (int i = 0; i < words.size(); i++) {
			wr.write(words.get(i));
			AddSpaces(length - words.get(i).length(), wr);
		}
		wr.write("Error rate %");
		wr.write("\n");

		int ruleno = 0;
		String prevtag;
		float error = 0;
		
		while (rules.peek() != null) {

			ruleno++;
			Transform t = rules.poll().t;
			prevtag = inputcurrtags.get(0);
			for (int i = 1; i < inputcurrtags.size(); i++) {
				String fromtag = inputcurrtags.get(i);
				if (prevtag.equals(t.prev) && fromtag.equals(t.from)) {
					inputcurrtags.set(i, t.to);

				}
				prevtag = fromtag;

			}

			error = BuildTable(inputcurrtags, inputcorrtags, wr, ruleno);
			if (error == 0f) {
				System.out.println("The error rate by applying modelB on given sentence is " + error);
				wr.write("\n");
				wr.write("will stop applying rules once the error rate becomes zero");
				wr.flush();
				wr.close();
				return;
			}

		}
		System.out.println("The error rate by applying modelB on given sentence is " + error);
		wr.flush();
		wr.close();

	}

	static float BuildTable(ArrayList<String> curtags, ArrayList<String> cortags, FileWriter wr, int ruleno)
			throws IOException {

		wr.write("Applying Ruleno" + " " + ruleno);
		int length = 30;
		AddSpaces(length - ("Applying Ruleno" + " ").length(), wr);
		for (int i = 0; i < curtags.size(); i++) {
			wr.write(curtags.get(i));
			AddSpaces(length - curtags.get(i).length(), wr);
		}
		float count = 0;
		for (int i = 0; i < cortags.size(); i++) {
			if (!cortags.get(i).equals(curtags.get(i))) {
				count++;
			}
		}
		float error = ((float) count / curtags.size()) * 100;
		wr.write(error + "");
		wr.write("\n");
		return error;

	}

	static Queue<Table> PartB() throws IOException {
		String correcttag = "";
		String currenttag = "";
		int i = -1;
		correcttags = new ArrayList<String>();
		currenttags = new ArrayList<String>();

		for (String pos : allwords) {
			i = pos.indexOf('_');
			correcttag = pos.substring(i + 1, pos.length());
			correcttags.add(correcttag);
			currenttag = uniLikelyTags.get(pos.substring(0, i));
			currenttags.add(currenttag);

		}
		Queue<Table> q = TBL();
		BuildTable(q);
		return q;

	}

	static void BuildTable(Queue<Table> q) throws IOException {
		FileWriter wr = new FileWriter("POSTaggingforCorpusformodelB.txt");
		wr.write("From");
		int length = 30;
		AddSpaces(length - "From".length(), wr);
		wr.write("To");
		AddSpaces(length - "To".length(), wr);
		wr.write("Previous");
		AddSpaces(length - "Previous".length(), wr);
		wr.write("Score");
		AddSpaces(length - "Score".length(), wr);
		wr.write("Error rate %");
		wr.write("\n");
		Iterator<Table> it = q.iterator();
		while (it.hasNext()) {
			Table obj = it.next();
			Transform t = obj.t;
			wr.write(t.from);
			AddSpaces(length - t.from.length(), wr);
			wr.write(t.to);
			AddSpaces(length - t.to.length(), wr);
			wr.write(t.prev);
			AddSpaces(length - t.prev.length(), wr);
			wr.write(t.score + "");
			AddSpaces(length - (t.score + "").length(), wr);
			wr.write((obj.error) + "");
			wr.write("\n");
		}
		wr.flush();
		wr.close();

	}

	public static void AddSpaces(int space, FileWriter wr) throws IOException {

		for (int i = 1; i <= space; i++) {
			wr.write(" ");

		}
		wr.write("\t\t");
	}

	static Queue<Table> TBL() throws IOException {
		Queue<Table> q = new LinkedList<>();
		Transform best = new Transform("", "", "", Integer.MIN_VALUE);
		float score = Integer.MAX_VALUE;
		float error;
		while (score > 1) {
			best = getBestInstance();
			error = ApplyTransform(best);
			score = best.score;
			q.add(new Table(best, error * 100));

		}
		BuildTable(q);
		return q;

	}

	static float ApplyTransform(Transform best) {

		for (int i = 1; i < allwords.size(); i++) {
			if (currenttags.get(i).equals(best.from) && currenttags.get(i - 1).equals(best.prev)) {
				currenttags.set(i, best.to);

			}
		}
		float error = 0;
		for (int i = 0; i < correcttags.size(); i++) {
			if (!currenttags.get(i).equals(correcttags.get(i))) {
				error++;
			}

		}

		

		return (float) error / allwords.size();

	}

	static Transform getBestInstance() {
		HashMap<String, Integer> NumGoodTransforms = new HashMap<String, Integer>();
		HashMap<String, Integer> NumBadTransforms = new HashMap();
		Transform bestL = new Transform("", "", "", Integer.MIN_VALUE);
		for (String fromtag : alltags) {
			for (String totag : alltags) {
				NumGoodTransforms = new HashMap<String, Integer>();
				NumBadTransforms = new HashMap<String, Integer>();
				for (int i = 1; i < allwords.size(); i++) {
					if (allwords.get(i - 1).equals("._.")) {
						continue;
					}
					if (correcttags.get(i).equals(totag) && currenttags.get(i).equals(fromtag)) {
						int val = NumGoodTransforms.getOrDefault(currenttags.get(i - 1), 0);
						NumGoodTransforms.put(currenttags.get(i - 1), val + 1);

					}

					else if (correcttags.get(i).equals(fromtag) && currenttags.get(i).equals(fromtag)) {
						int val = NumBadTransforms.getOrDefault(currenttags.get(i - 1), 0);
						NumBadTransforms.put(currenttags.get(i - 1), val + 1);
					}

				}
				Transform bestZ = getBestZ(NumGoodTransforms, NumBadTransforms);
				if (bestZ.score > bestL.score) {
					bestL = new Transform(fromtag, totag, bestZ.prev, bestZ.score);

				}

			}

		}
		return bestL;

	}

	static Transform getBestZ(HashMap<String, Integer> map1, HashMap<String, Integer> map2) {
		int bestScore = Integer.MIN_VALUE;
		String prevword = "";
		if (map2.size() == 0) {
			return new Transform("", "", prevword, bestScore);
		}
		for (Map.Entry<String, Integer> entry : map1.entrySet()) {
			if (map2.containsKey(entry.getKey())) {
				int val = map1.get(entry.getKey()) - map2.get(entry.getKey());
				if (bestScore < val) {
					bestScore = val;
					prevword = entry.getKey();
				}

			}

		}
		Transform bestZ = new Transform("", "", prevword, bestScore);
		return bestZ;

	}

	static void PartA() throws IOException {

		BufferedReader bf = new BufferedReader(new FileReader("HW2_F17_NLP6320_POSTaggedTrainingSet-Unix.txt"));
		String eachline = bf.readLine();
		String lines = "";
		while (eachline != null) {
			lines = lines + " " + eachline;
			eachline = bf.readLine();
		}
		StringTokenizer tokenizer = new StringTokenizer(lines);

		allwords = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			allwords.add(tokenizer.nextToken());
		}

		HashMap<String, HashMap<String, Integer>> unigramTag = new HashMap<>();
		HashMap<String, Integer> tagmap = new HashMap<>();
		String uniword = "";
		String tag = "";

		int tagcount = 0;
		for (String word : allwords) {
			int i = word.indexOf('_');
			uniword = word.substring(0, i);
			tag = word.substring(i + 1, word.length());
			alltags.add(tag);
			tagmap = unigramTag.getOrDefault(uniword, new HashMap<String, Integer>());
			tagcount = tagmap.getOrDefault(tag, 0);
			tagmap.put(tag, tagcount + 1);
			unigramTag.put(uniword, tagmap);
		}

		HashMap<String, Integer> taggermap;
		Map.Entry<String, Integer> first;
		LinkedHashMap<String, Integer> sortedTagMap;

		for (Map.Entry<String, HashMap<String, Integer>> entry : unigramTag.entrySet()) {
			taggermap = entry.getValue();
			sortedTagMap = (LinkedHashMap<String, Integer>) sortByValues(taggermap);
			first = sortedTagMap.entrySet().iterator().next();

			uniLikelyTags.put(entry.getKey(), first.getKey());

		}
		
		
		
		ArrayList<String> correctedtags=new ArrayList<String>();
		ArrayList<String> corpuswords=new ArrayList<String>();
		for (String word : allwords) {
			int i = word.indexOf('_');
			String key = word.substring(0, i);
			corpuswords.add(key);
			tag=word.substring(i + 1, word.length());
			
			if (uniLikelyTags.containsKey(key)) {
				String tagC = uniLikelyTags.get(key);
				
				if (!tagC.equals(tag)) {
					correctedtags.add(tagC);
					
				}
				else{
					correctedtags.add(tag);
				}
			}
			
		}
		int length = 30;
		FileWriter wr1 = new FileWriter("POSTaggingforCorpusformodelA.txt");
		
		wr1.write("After Applying unigram model(a) on the given Corpus it is changed to ");
		wr1.write("\n");
		wr1.write("word");
		AddSpaces(length-"word".length(), wr1);
		wr1.write("Correctedtag");
		wr1.write("\n");
		
		
		
		for (int i = 0; i < corpuswords.size(); i++) {
			wr1.write(corpuswords.get(i));
			AddSpaces(length - corpuswords.get(i).length(), wr1);
			wr1.write(correctedtags.get(i));
			wr1.write("\n");
		}
		
		
		wr1.flush();
		wr1.close();
		
		
		
		

	}

	private static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}

class Transform {
	String prev;
	String from;
	String to;
	int score;

	Transform(String from, String to, String prev, int score) {
		this.prev = prev;
		this.from = from;
		this.to = to;
		this.score = score;

	}

	public String toString() {
		return from + " " + to + " " + prev + " " + score;

	}
}

class Table {
	Transform t;
	float error;

	Table(Transform t, float error) {
		this.t = t;
		this.error = error;

	}

}
