import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.StringTokenizer;

public class Question1 {

	static HashMap<String, Integer> biMap = new HashMap<String, Integer>();
	static HashMap<String, Integer> uiMap = new HashMap<String, Integer>();
	static BufferedReader bf;
	static Map<Integer, Integer> HistMap;
	static ArrayList<String> allwords;

	public static void main(String[] args) throws IOException {
		String sentence="";
        if(args.length>0){
        	sentence=args[0];
        }
        else{
		sentence = "Richard W. Lock , retired vice president and treasurer of";}
		fileRead();
		BuildHistogram();

		String[] FileTokens = new String[allwords.size()];
		allwords.toArray(FileTokens);
		boolean fileread = true;
		differentSmoothing(FileTokens, 1, fileread);
		differentSmoothing(FileTokens, 2, fileread);
		differentSmoothing(FileTokens, 3, fileread);

		fileread = false;
		
		StringTokenizer tempTokenize = new StringTokenizer(sentence);
         ArrayList<String> inputWords=new ArrayList<String>();

		while (tempTokenize.hasMoreTokens()) {
			inputWords.add(tempTokenize.nextToken());
		}
		String[] inputtokens = new String[inputWords.size()];
		inputWords.toArray(inputtokens);

		System.out.println("TotalProbability without Smoothing for given Sentence" + " "
				+ differentSmoothing(inputtokens, 1, fileread));

		System.out.println("TotalProbability for Addone Smoothing for given Sentence" + " "
				+ differentSmoothing(inputtokens, 2, fileread));

		System.out.println("TotalProbability for Good discounting for given Sentence" + " "
				+ differentSmoothing(inputtokens, 3, fileread));

	}

	static void fileRead() throws IOException {
		biMap = new HashMap<String, Integer>();
		uiMap = new HashMap<String, Integer>();
		
		BufferedReader bf = new BufferedReader(
				new FileReader("HW2_F17_NLP6320-NLPCorpusTreebank2Parts-CorpusA-Unix.txt"));
      String eachline = bf.readLine();
          String lines="";
		while (eachline != null) {
			lines =lines + " " + eachline;
			eachline = bf.readLine();
		}
		StringTokenizer tokenizer = new StringTokenizer(lines);
		
		allwords = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()) {
			allwords.add(tokenizer.nextToken());
		}
		
		
		String prevWord = null;
		if (allwords.size() > 0)
			prevWord = allwords.get(0);
		for (int i = 1; i < allwords.size(); i++) {
			String currWord = allwords.get(i);
			String biWord = prevWord + " " + currWord;

			Integer val = biMap.getOrDefault(biWord, 0);
			biMap.put(biWord, val + 1);

			val = uiMap.getOrDefault(prevWord, 0);
			uiMap.put(prevWord, val + 1);

			prevWord = currWord;
		}

	}

	
	public static void AddSpaces(int space, FileWriter wr) throws IOException {

		for (int i = 1; i <= space; i++) {
			wr.write(" ");

		}
		wr.write("\t\t");
	}

	static float differentSmoothing(String[] tokens, int number, boolean fileread) throws IOException {

		float totalProb = 1;
		float prob = 1;

		String token1 = "";
		int biTokenCount = 0;
		int uniTokenCount = 0;
		if (tokens.length > 0) {
			token1 = tokens[0];
		}
		String token2 = "";

		ArrayList<String> biList = new ArrayList<String>();
		ArrayList<Float> probList = new ArrayList<Float>();
		ArrayList<Float> countList = new ArrayList<Float>();

		for (int i = 1; i < tokens.length; i++) {
			token2 = tokens[i];
			String bigram = token1 + " " + token2;
			biList.add(bigram);

			biTokenCount = biMap.getOrDefault(bigram, 0);
			uniTokenCount = uiMap.getOrDefault(token1, 0);

			if (number == 1) {
				if (biTokenCount != 0 && uniTokenCount != 0) {
					prob = (float) (biTokenCount) / (float) (uniTokenCount);
					countList.add((float) biTokenCount);
					probList.add(prob);
					totalProb *= prob;

				} else {
					countList.add((float) 0);
					probList.add(prob);
					prob = 0;
					totalProb = 0;
				}
			}
			if (number == 2) {
				countList.add((float) (biTokenCount + 1));
				prob = (float) (biTokenCount + 1) / (float) (uniTokenCount + uiMap.size());
				totalProb *= prob;
				probList.add(prob);
			}

			if (number == 3) {
				if (biTokenCount > 0) {
					float c = 0;
					float c1 = 0;
					int histval = 0;
					c = biTokenCount;
					if (HistMap.containsKey((int) (c + 1))) {
						histval = HistMap.get((int) (c + 1));
						c1 = (c + 1) * (float) histval / (float) HistMap.get((int) c);
						countList.add(c1);
						prob = c1 / (float) allwords.size();
						probList.add(prob);
						totalProb = totalProb * prob;
					}
					
					else{
						countList.add(0f);
						probList.add(0f);
						totalProb =0;
					}

				} else {
					prob = (float) HistMap.get(1) / (float) allwords.size();
					countList.add((float) HistMap.get(1));
					probList.add(prob);
					totalProb *= prob;
				}
			}

			token1 = token2;
		}

		BuildTable(biList, countList, probList, number, fileread);

		return totalProb;
	}

	static void BuildTable(ArrayList<String> Bigram, ArrayList<Float> countList, ArrayList<Float> probList, int number,
			boolean fileread) throws IOException {
		FileWriter wr = null;
		if (fileread) {
			if (number == 1)
				wr = new FileWriter("NoSmoothingforCorpus.txt");
			else if (number == 2) {
				wr = new FileWriter("AddoneSmoothingforCorpus.txt");
			} else {
				wr = new FileWriter("GoodTuningForCorpus.txt");
			}
		}

		else {

			if (number == 1)
				wr = new FileWriter("NoSmoothingforSentence.txt");
			else if (number == 2) {
				wr = new FileWriter("AddoneSmoothingforSentence.txt");
			} else {
				wr = new FileWriter("GoodTuningForSentence.txt");
			}
		}

		int bigramlength = 30;
		int countlength = 15;

		wr.write("Bigram");
		AddSpaces(bigramlength - "Bigram".length(), wr);
		wr.write("Count");
		AddSpaces(countlength - "Count".length(), wr);
		wr.write("Probabiltites");
		wr.write("\n");

		for (int i = 0; i < Bigram.size(); i++) {
			String word = Bigram.get(i);
			wr.write(word);
			AddSpaces(bigramlength - word.length(), wr);
			float biCount = countList.get(i);
			wr.write(biCount + "");

			AddSpaces(countlength - ((biCount + "").length()), wr);

			float probi = probList.get(i);
			wr.write(probi + "");
			wr.write("\n");

		}

		wr.flush();
		wr.close();

	}

	static void BuildHistogram() {
		HistMap = new HashMap<Integer, Integer>();
		for (Map.Entry<String, Integer> entry : biMap.entrySet()) {
			Integer biCount = entry.getValue();
			int count = HistMap.getOrDefault(biCount, 0);
			HistMap.put(biCount, count + 1);

		}
	}

}
