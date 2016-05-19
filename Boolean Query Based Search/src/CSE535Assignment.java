

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class CSE535Assignment {

	/*The hashmaps are used as an intermediate when creating the linked lists. The queries do not use these, they use the linked lists below*/
	private HashMap<String, Posting> _termMap;
	private HashMap<Integer, Posting> _docIDMap;
	private BufferedReader _BR;

	/*The linked list that stores the terms in and their postings lists in order of docID*/
	private LinkedListNode _docIDList;

	/*The linked list that stores the terms in and their postings lists in order of frequency*/
	private LinkedListNode _freqList;


	private static Writer _writer;

	
	/*
	 * The main method that calls all of the other methods to achieve the queries. It has the term.idx file read and converted into linked lists, 
	 * and then reads the queries from a file. It will apply each line of queries to the getPostings, DAATOR, DAATAND, TAATOR, and TAATAND methods
	 * and then append the results to the bottom of the log file. The top K terms will be added at the top of the file. This will repeat for each line of 
	 * queries.
	 */
	public static void main(String[] args){
		CSE535Assignment driver = new CSE535Assignment();
		//driver.produceHashMaps("postings/term.idx");
		driver.produceHashMaps(args[0]);
		ArrayList<String> TAATAND;
		String TAATANDString = "";

		ArrayList<String> TAATOR;
		String TAATORString = "";

		ArrayList<String> DAATAND;
		String DAATANDString = "";

		ArrayList<String> DAATOR;
		String DAATORString = "";

		//		ArrayList<String> queries = driver.getQueries("postings/query_file.txt");
		ArrayList<String> queries = driver.getQueries(args[3]);
		try{
			//			_writer = new BufferedWriter(new FileWriter("postings/outputs.log"));
			_writer = new BufferedWriter(new FileWriter(args[1]));
			_writer.write("");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if (_writer != null)
					_writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		//		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("postings/outputs.log", true)))) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(args[1], true)))) {
			out.println(driver.getTopK(Integer.parseInt(args[2])));	
			for(String queryLine: queries){	

				//		System.out.println(driver.getTopK(14));

				ArrayList<String> testArray = tokenizeString(queryLine);
				for(String term: testArray){
					out.println(driver.getPostings(term));
					//		out.println(driver.getPostings("year"));
				}
				TAATAND = driver.termAtATimeQueryAnd(testArray);
				TAATANDString = "FUNCTION: TermAtATimeQueryAnd";
				for(String term: testArray){
					TAATANDString = TAATANDString + " " + term;
				}
				if(TAATAND.size() > 1){
					TAATANDString = TAATANDString + "\n"+ (TAATAND.size()-2) + 
							" documents are found\n"+ TAATAND.get(TAATAND.size()-1) + " comparisons are made\n"
							+ TAATAND.get(TAATAND.size()-2) + " seconds are used\n" + 
							driver.termAtATimeQueryAndOptimized(testArray) + " comparisons are made with optimization\n" 
							+ "Result:";
					//			System.out.print(TAATANDString);
					out.print(TAATANDString);
					for(int i = 0; i < TAATAND.size()-2; i++){
						if(i == 0){
							//					System.out.print(" "+TAATAND.get(i));
							out.print(" "+TAATAND.get(i));
						}
						else{
							//					System.out.print(", "+ TAATAND.get(i));
							out.print(", "+ TAATAND.get(i));
						}
					}
					out.print("\n");
				}
				else{
					out.print(TAATANDString + "\nterms not found\n");;
				}


				TAATOR = driver.termAtATimeQueryOr(testArray);
				TAATORString = "FUNCTION: TermAtATimeQueryOr";
				for(String term: testArray){
					TAATORString = TAATORString + " " + term;
				}
				if(TAATOR.size() > 1){
					if(TAATOR.size() > 1){
						TAATORString = TAATORString + "\n"+ (TAATOR.size()-2) + 
								" documents are found\n"+ TAATOR.get(TAATOR.size()-1) + " comparisons are made\n"
								+ TAATOR.get(TAATOR.size()-2) + " seconds are used\n" +
								driver.termAtATimeQueryOrOptimized(testArray) + " comparisons are made with optimization\n"
								+ "Result:";
						out.print(TAATORString);
						for(int i = 0; i < TAATOR.size()-2; i++){
							if(i == 0){
								out.print(" "+TAATOR.get(i));
							}
							else{
								out.print(", "+ TAATOR.get(i));
							}
						}
						out.print("\n");
					}
				}
				else{
					out.print(TAATORString + "\nterms not found\n");;
				}


				DAATAND = driver.docAtATimeQueryAnd(testArray);
				DAATANDString = "FUNCTION: docAtATimeQueryAnd";

				for(String term: testArray){
					DAATANDString = DAATANDString + " " + term;
				}

				if(DAATAND.size() > 1){
					DAATANDString = DAATANDString + "\n"+ (DAATAND.size()-2) + 
							" documents are found\n"+ DAATAND.get(DAATAND.size()-1) + " comparisons are made\n"
							+ DAATAND.get(DAATAND.size()-2) + " seconds are used\n"
							+ "Result:";
					out.print(DAATANDString);
					for(int i = 0; i < DAATAND.size()-2; i++){
						if(i == 0){
							out.print(" "+DAATAND.get(i));
						}
						else{
							out.print(", "+ DAATAND.get(i));
						}
					}
					out.print("\n");
				}

				else{
					out.print(DAATANDString + "\nterms not found\n");;
				}


				DAATOR = driver.docAtATimeQueryOr(testArray);
				DAATORString = "FUNCTION: docAtATimeQueryOr";
				for(String term: testArray){
					DAATORString = DAATORString + " " + term;
				}

				if(DAATOR.size() > 1){
					DAATORString = DAATORString + "\n"+ (DAATOR.size()-2) + 
							" documents are found\n"+ DAATOR.get(DAATOR.size()-1) + " comparisons are made\n"
							+ DAATOR.get(DAATOR.size()-2) + " seconds are used\n"
							+ "Result:";
					out.print(DAATORString);
					for(int i = 0; i < DAATOR.size()-2; i++){
						if(i == 0){
							out.print(" "+DAATOR.get(i));
						}
						else{
							out.print(", "+ DAATOR.get(i));
						}
					}
					out.print("\n");

				}
				else{
					out.print(DAATORString + "\nterms not found\n");
				}
			}
		}catch (IOException e) {

		}
		finally {
			try {_writer.close();} catch (Exception ex) {}
		}
	}

	/*
	 * Small helper method used to read the query_file.txt file to retrieve the query terms to be used in the queries. It reads each line and adds 
	 * them to an arrayList of Strings that will later be parsed apart. It returns this arrayList.
	 */
	public ArrayList<String> getQueries(String fileName){
		ArrayList<String> queries = new ArrayList<String>();

		try {
			String line = "";
			_BR = new BufferedReader(new FileReader(fileName));
			while ((line = _BR.readLine()) != null) {
				if(!(line.equals("")) && !(line.equals("\n"))&& !(line.equals(" "))){
					//					System.out.println(line);
					queries.add(line);
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (_BR != null)
					_BR.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return queries;
	}

/*
 * Method that reads the input term.idx file line by line and forms a hashmap with the data that will later be converted into the two linked lists
 * used for the queries. For every line of the file it breaks apart the string, and uses the term as a key for the hashmap. The value that goes with 
 * key is a posting object that will hold references to the term, the frequency, and all of the docIDs(in an arraylist). After this is done, it will
 * use the formLinkedList method to create the sorted linkedlists.
 */
	public void produceHashMaps(String filename) {
		_termMap = new HashMap<String, Posting>();
		_docIDMap = new HashMap<Integer, Posting>();

		try {
			String line = "";
			_BR = new BufferedReader(new FileReader(filename));
			while ((line = _BR.readLine()) != null) {
				boolean foundTerm = false;
				boolean foundDocCount = false;
				boolean finishedPostingList = false;
				String currentTerm = "";
				String currentDocCount = "";
				ArrayList<Pair> currentTermDocIDs = new ArrayList<Pair>();

				String currentDocIDString = "";
				int currentDocID = 0;
				for (int i = 0; i < line.length(); i++) {
					if (!foundTerm) {
						if ((line.charAt(i) == '\\') && (line.charAt(i + 1) == 'c')) {
							foundTerm = true;
							i++;
						} else {
							currentTerm = currentTerm + line.charAt(i);
						}
					} else {
						if (!foundDocCount) {
							if ((line.charAt(i) == '\\') && (line.charAt(i + 1) == 'm')) {
								foundDocCount = true;
								i = i + 2;
							} else {
								currentDocCount = currentDocCount + line.charAt(i);
							}
						} else {
							if (!finishedPostingList) {
								if (line.charAt(i) == '/') {
									currentDocID = Integer.parseInt(currentDocIDString);
									currentDocIDString = "";
								} else if ((line.charAt(i) == ',') || (line.charAt(i) == ']')) {
									if(line.charAt(i) == ']'){
										foundDocCount = false;
										foundTerm = false;
										_termMap.put(currentTerm, new Posting(currentTerm, currentTermDocIDs));
									}
									currentTermDocIDs.add(new Pair(currentDocID, Integer.parseInt(currentDocIDString)));
									if(_docIDMap.containsKey(currentDocID)){
										_docIDMap.get(currentDocID).getTermList().add(new Pair(currentTerm, Integer.parseInt(currentDocIDString)));
									}
									else{
										_docIDMap.put(currentDocID, new Posting(currentDocID));
										_docIDMap.get(currentDocID).getTermList().add(new Pair(currentTerm, Integer.parseInt(currentDocIDString)));
									}
									currentDocIDString = "";
								} else {
									if(line.charAt(i) != ' '){
										currentDocIDString = currentDocIDString + line.charAt(i);
									}
								}
							}
						}
					}

				}	
			}

			formLinkedLists();


		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (_BR != null)
					_BR.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/*
	 * Method to create the two linked list required for the query functions later on. It uses the HashMap and iterates through each entry in the map.
	 * It uses a boolean to see if it is at the first node, and forms one linked list where every node holds the term as a string, the term frequency, 
	 * an arraylist of docIDs. This arrayList is sorted by either frequency or docID, depending on which linked list it is, and then converts
	 * the docID arrayList into a linkedList which the node also holds. This is done for every term in the hashmap.
	 */
	public void formLinkedLists(){
		LinkedListNode docNode = null;
		LinkedListNode freqNode = null;
		boolean first = true;
		for(String Key : _termMap.keySet()){

			if(first){
				docNode = new LinkedListNode(Key, new ArrayList<Pair>(_termMap.get(Key).getDocList()));
				Collections.sort(docNode.getInterPostingList(), Pair.docIDComparator);
				docNode.convertToLinkedList();
				freqNode = new LinkedListNode(Key, new ArrayList<Pair>(_termMap.get(Key).getDocList()));
				Collections.sort(freqNode.getInterPostingList(), Pair.freqComparator);
				freqNode.convertToLinkedList();
				_docIDList = docNode;
				_freqList = freqNode;
				first = false;
			}
			else{
				docNode._next = new LinkedListNode(Key, new ArrayList<Pair>(_termMap.get(Key).getDocList()));
				docNode = docNode._next;
				Collections.sort(docNode.getInterPostingList(), Pair.docIDComparator);
				docNode.convertToLinkedList();

				freqNode._next = new LinkedListNode(Key, new ArrayList<Pair>(_termMap.get(Key).getDocList()));
				freqNode = freqNode._next;
				Collections.sort(freqNode.getInterPostingList(), Pair.freqComparator);
				freqNode.convertToLinkedList();
			}
		}
	}
	
/*
 * getTopK retrieved the top k most frequent terms. It accomplishes this by adding all of the terms to an array list, and then sorting the list by 
 * term frequency. It then loops from 0 to the K and adds the term to an array list of strings. This list is then returned.
 */
	public String getTopK(int K) {
		ArrayList<LinkedListNode> nodeList = new ArrayList<LinkedListNode>();
		LinkedListNode temp = _docIDList;
		String topTerms = "FUNCTION: getTopK " + K + "\n" + "Result: ";
		while(temp != null){
			nodeList.add(temp);
			temp = temp._next;
		}
		Collections.sort(nodeList, LinkedListNode.docIDComparator);
		for(int i = 0; i < K; i++){
			topTerms = topTerms + nodeList.get(i).getTerm();
			if(i < K-1){
				topTerms= topTerms + ", ";
			}
		}
		return topTerms;
	}

	
	/*
	 * A method to get the postings list for a given term. It does this by first finding the term in the linked list of terms that 
	 * is sorted by document ID and adding each DocID in the term's posting list into an array list of strings. It then iterates 
	 * through the array list of strings and creates a string that includes the IDs in sorted order, since they were added to the 
	 * list in sorted order.
	 */
	public String getPostings(String query_term) {
		String postings = "FUNCTION: getPostings " + query_term + "\n";
		boolean foundTerm = false;
		LinkedListNode temp = _docIDList;
		DocIDLinkedListNode IDTemp = null;
		boolean first = true;
		while(temp != null){
			if(query_term.equals(temp.getTerm())){
				postings = postings + "Ordered by doc IDs: ";
				foundTerm = true;
				IDTemp = temp.getFinalPostingsList();
				while(IDTemp != null){
					if(first){
						postings = postings + IDTemp.getID();
						first = false;
					}
					else {
						postings = postings + ", " + IDTemp.getID();
					}	
					IDTemp = IDTemp._next;
				}
				break;
			}
			temp = temp._next;
		}
		first = true;
		temp = _freqList;
		while(temp != null){
			if(query_term.equals(temp.getTerm())){
				postings = postings + "\nOrdered by TF: ";
				foundTerm = true;
				//				for(Pair p : temp.getPostingList()){
				IDTemp = temp.getFinalPostingsList();
				while(IDTemp != null){
					if(first){
						postings = postings + IDTemp.getID();
						first = false;
					}
					else {
						postings = postings + ", " + IDTemp.getID();
					}
					IDTemp = IDTemp._next;
				}
				break;
			}
			temp = temp._next;
		}
		if(!foundTerm){
			postings = postings + "term not found";
		}
		return postings;
	}

	
	/*
	 * A helper method used to parse apart a string of query terms into an array list that holds each of these terms in their own spot in the returned array.
	 */
	public static ArrayList<String> tokenizeString(String terms){
		ArrayList<String> termArray = new ArrayList<String>();
		String term = "";
		for(int i = 0; i < terms.length(); i++){
			if(terms.charAt(i) == ' '){
				if(!(term.equals(""))&& !(term.equals("\n"))){
					//					System.out.println(term);
					termArray.add(term);
					term = "";
				}	
			}
			else{
				term = term + terms.charAt(i);
			}
		}
		if(!(term.equals("")) && !(term.equals("\n"))){
			//			System.out.println(term);
			termArray.add(term);
			term = "";
		}	
		//		System.out.println(termArray.size());
		return termArray;
	}

/*
 * The below method is for the Term-at-a-time-and query. It starts by iterating through the linked list of terms that is ordered by term frequency 
 * to find the first query term from the input. Once it finds that term, it will iterate through the term's postings list, and add all of those 
 * document IDs to the temporary return list. The it will start a similar loop that will iterate through the rest of the query terms. For each term, 
 * it will iterate through the (frequency sorted) linked list of terms until it finds a match. At that point, it will loop through the matched term's
 * postings list and compare each of the IDs in the posting list against each of the IDs in the temporary return list. If it finds the same ID in both 
 * lists then it is added to a final return list. If there are more terms to check after this, then the final return list is assigned to the temporary 
 * list variable, and the final return list variable is given a new empty list to begin a new iteration of the loop. A comparison counter in incremented 
 * every time an ID from a term's posting list is compared against an ID from the temporary return list. This means that before adding a new result, it will
 * be compared against every other current result before being added to the final list. At the end, an arrayList holding all of the document IDs that are shared
 * between the query terms will be returned. In the last two spots of this list will be the time it took to run this method, and the number of comparisons used.
 */
	public ArrayList<String> termAtATimeQueryAnd(ArrayList<String> query_terms) {
		String Query = query_terms.get(0);
		LinkedListNode temp = _freqList;
		DocIDLinkedListNode postingsTemp;
		ArrayList<Integer> docsTemp = new ArrayList<Integer>();
		ArrayList<Integer> docs = new ArrayList<Integer>();
		ArrayList<String> finalReturnList = new ArrayList<String>();
		int comparisons = 0;
		double startTime = System.nanoTime();
		double endTime;
		double totalTime;
		while(temp != null){
			if(temp.getTerm().equals(Query)){
				postingsTemp = temp.getFinalPostingsList();
				while(postingsTemp != null){

					docs.add(postingsTemp.getID());
					postingsTemp = postingsTemp._next;
				}
				break;
			}
			temp = temp._next;
		}
		temp = _freqList;

		for(int i = 1; i < query_terms.size(); i++){
			//			System.out.println(docs.size());
			docsTemp = docs;
			//			System.out.println(docsTemp.size());
			docs = new ArrayList<Integer>();
			//			System.out.println(docs.size());
			Query = query_terms.get(i);
			while(temp != null){
				if(temp.getTerm().equals(Query)){
					postingsTemp = temp.getFinalPostingsList();
					while(postingsTemp != null){
						for(int doc : docsTemp){
							comparisons++;
							if(doc == postingsTemp.getID()){
								docs.add(postingsTemp.getID());
								//								System.out.println("hi");
								break;
							}
						}
						postingsTemp = postingsTemp._next;
					}
					temp = _freqList;
					break;
				}
				temp = temp._next;
			}
		}
		endTime = System.nanoTime();
		totalTime = (double) ((endTime - startTime)/1000000000.0);
		//		System.out.println("time take:  "+ totalTime);
		Collections.sort(docs);
		Query = "Term at a time AND results:  ";
		for(int doc : docs){
			Query = Query + "" + doc + "   ";
			finalReturnList.add("" + doc);
		}
		if(docs.size() == 0){
			Query = Query + "terms not found";
		}
		else{
			finalReturnList.add(""+totalTime);
			finalReturnList.add(""+comparisons);
		}
		//		System.out.println(comparisons);
		return finalReturnList;
	}

	
	/*
	 * The below method is for the optimized Term-at-a-time-and query. Firstly, for each term in the list of query terms, it will find that term's node in
	 * in the linked list of terms that is ordered by term frequency, and put the node in an arrayList. The ArrayList will be sorted by term frequency, and 
	 * then iterated through again to add all of the terms back into a new arrayList of query terms, that are now sorted by term frequency. This list is 
	 * now treated as the list of query terms, and the rest of the method is the same as the normal TAATAnd method, except for the object that is returned
	 *  It then iterates through the linked list of terms that is ordered by term frequency 
	 * to find the first query term from the input. Once it finds that term, it will iterate through the term's postings list, and add all of those 
	 * document IDs to the temporary return list. The it will start a similar loop that will iterate through the rest of the query terms. For each term, 
	 * it will iterate through the (frequency sorted) linked list of terms until it finds a match. At that point, it will loop through the matched term's
	 * postings list and compare each of the IDs in the posting list against each of the IDs in the temporary return list. If it finds the same ID in both 
	 * lists then it is added to a final return list. If there are more terms to check after this, then the final return list is assigned to the temporary 
	 * list variable, and the final return list variable is given a new empty list to begin a new iteration of the loop. A comparison counter in incremented 
	 * every time an ID from a term's posting list is compared against an ID from the temporary return list. This means that before adding a new result, it will
	 * be compared against every other current result before being added to the final list. At the end, the number of comparisons will be returned for use
	 * in the log file.
	 */
	public int termAtATimeQueryAndOptimized(ArrayList<String> query_terms) {

		LinkedListNode temp = _freqList;
		DocIDLinkedListNode postingsTemp;
		ArrayList<Integer> docsTemp = new ArrayList<Integer>();
		ArrayList<Integer> docs = new ArrayList<Integer>();
		ArrayList<String> finalReturnList = new ArrayList<String>();
		int comparisons = 0;
		double startTime = System.nanoTime();
		double endTime;
		double totalTime;
		ArrayList<LinkedListNode> tempQueryTerms = new ArrayList<LinkedListNode>();
		ArrayList<String> OptQueryTerms = new ArrayList<String>();

		while(temp != null){
			if(query_terms.contains(temp.getTerm())){
				tempQueryTerms.add(temp);
			}
			temp = temp._next;
		}

		Collections.sort(tempQueryTerms, LinkedListNode.docIDComparator);


		for(int i = tempQueryTerms.size()-1; i >= 0; i--){
			OptQueryTerms.add(tempQueryTerms.get(i).getTerm());
		}

		temp = _freqList;
		String Query = OptQueryTerms.get(0);
		while(temp != null){
			if(temp.getTerm().equals(Query)){
				postingsTemp = temp.getFinalPostingsList();
				while(postingsTemp != null){

					docs.add(postingsTemp.getID());
					postingsTemp = postingsTemp._next;
				}
				break;
			}
			temp = temp._next;
		}
		temp = _freqList;

		for(int i = 1; i < OptQueryTerms.size(); i++){
			//			System.out.println(docs.size());
			docsTemp = docs;
			//			System.out.println(docsTemp.size());
			docs = new ArrayList<Integer>();
			//			System.out.println(docs.size());
			Query = OptQueryTerms.get(i);
			while(temp != null){
				if(temp.getTerm().equals(Query)){
					postingsTemp = temp.getFinalPostingsList();
					while(postingsTemp != null){
						for(int doc : docsTemp){
							comparisons++;
							if(doc == postingsTemp.getID()){
								docs.add(postingsTemp.getID());
								//								System.out.println("hi");
								break;
							}
						}
						postingsTemp = postingsTemp._next;
					}
					temp = _freqList;
					break;
				}
				temp = temp._next;
			}
		}
		endTime = System.nanoTime();
		totalTime =(double)  ((endTime - startTime)/1000000000);
		//		System.out.println("time take:  "+ totalTime);
		Collections.sort(docs);
		Query = "Term at a time AND results:  ";
		for(int doc : docs){
			Query = Query + "" + doc + "   ";
			finalReturnList.add("" + doc);
		}
		if(docs.size() == 0){
			Query = Query + "terms not found";
		}
		else{
			finalReturnList.add(""+totalTime);
			finalReturnList.add(""+comparisons);
		}
		//		System.out.println(comparisons);
		return comparisons;
	}

	
	/*
	 * The below method determines the optimized term-at-a-time-query-or by first taking in an array list of the query terms.  It will find each of 
	 * these terms in the linked list that is ordered by frequency and add the corresponding nodes of the list to an arraylist. This array list
	 * will be sorted by frequency, and then looped through again. This time, it will add the terms back into an arrayList of Strings. From here, the method
	 * will be the same as the normal TAATOr method below, but the arraylist of terms that is used is now sorted by frequency.
	 * For each of these terms it will iterate through the linked list of terms that is ordered by frequency until it finds a match. Once it finds a match it will iterate through 
	 * the term's postings list, and for each posting it will iterate through the final return list. If the ID of the posting is not found in the
	 * final list, then it will be added to the final list. This will continue for each posting of the term, and for each term in the query until 
	 * all of the query terms have been iterated through. A comparison counter will be incremented every time a posting is compared against an ID that 
	 * is already in the final return list. This will result in a very large number of comparisons. At the end, the number of comparisons will be returned to add to the log file.
	 */
	public int termAtATimeQueryOrOptimized(ArrayList<String> query_terms) {
		LinkedListNode temp = _freqList;
		DocIDLinkedListNode postingsTemp;
		String Query = "";
		int comparisons = 0;
		boolean alreadyAdded = false;
		ArrayList<Integer> docs = new ArrayList<Integer>();
		ArrayList<String> finalReturnList = new ArrayList<String>();
		double startTime = System.nanoTime();
		double endTime;
		double totalTime;

		ArrayList<LinkedListNode> tempQueryTerms = new ArrayList<LinkedListNode>();
		ArrayList<String> OptQueryTerms =  new ArrayList<String>();

		while(temp != null){
			if(query_terms.contains(temp.getTerm())){
				tempQueryTerms.add(temp);
			}
			temp = temp._next;
		}

		Collections.sort(tempQueryTerms, LinkedListNode.docIDComparator);


		for(int i = tempQueryTerms.size()-1; i >= 0; i--){
			OptQueryTerms.add(tempQueryTerms.get(i).getTerm());
		}

		temp = _freqList;


		for(String term : OptQueryTerms){
			temp = _freqList;
			while(temp != null){
				if(term.equals(temp.getTerm())){
					postingsTemp = temp.getFinalPostingsList();
					while(postingsTemp != null){
						for(int doc : docs){
							comparisons++;
							if(doc == postingsTemp.getID()){
								alreadyAdded = true;
								break;
							}
						}
						if(!alreadyAdded){
							docs.add(postingsTemp.getID());
						}
						alreadyAdded = false;
						postingsTemp = postingsTemp._next;
					}
					temp = _freqList;
					break;
				}
				temp = temp._next;
			}
		}
		endTime = System.nanoTime();
		totalTime = (double) ((endTime - startTime)/1000000000);
		//		System.out.println("Time take:  "+ totalTime);
		Collections.sort(docs);
		Query = "Term at a time OR results:  ";
		for(int doc : docs){
			Query = Query + doc + "  ";
			finalReturnList.add(""+doc);
		}
		if(docs.size() == 0){
			Query = Query + "terms not found";
		}
		else{
			finalReturnList.add(""+totalTime);
			finalReturnList.add(""+comparisons);
		}
		//		System.out.println(comparisons);
		return comparisons;
	}

	/*
	 * The below method determines the term-at-a-time-query-or by first taking in an array list of the query terms. For each of these terms it will 
	 * iterate through the linked list of terms that is ordered by frequency until it finds a match. Once it finds a match it will iterate through 
	 * the term's postings list, and for each posting it will iterate through the final return list. If the ID of the posting is not found in the
	 * final list, then it will be added to the final list. This will continue for each posting of the term, and for each term in the query until 
	 * all of the query terms have been iterated through. A comparison counter will be incremented every time a posting is compared against an ID that 
	 * is already in the final return list. This will result in a very large number of comparisons. At the end, the final return list, in the form of 
	 * an array list will be returned that will hold all of the document IDs relevant to the query terms, and with no duplicates, and in the last two 
	 * spots will be the time it took to complete the operation, and the number of comparisons
	 */
	public ArrayList<String> termAtATimeQueryOr(ArrayList<String> query_terms) {
		LinkedListNode temp = _freqList;
		DocIDLinkedListNode postingsTemp;
		String Query = "";
		int comparisons = 0;
		boolean alreadyAdded = false;
		ArrayList<Integer> docs = new ArrayList<Integer>();
		ArrayList<String> finalReturnList = new ArrayList<String>();
		double startTime = System.nanoTime();
		double endTime;
		double totalTime;
		for(String term : query_terms){
			temp = _freqList;
			while(temp != null){
				if(term.equals(temp.getTerm())){
					postingsTemp = temp.getFinalPostingsList();
					while(postingsTemp != null){
						for(int doc : docs){
							comparisons++;
							if(doc == postingsTemp.getID()){
								alreadyAdded = true;
								break;
							}
						}
						if(!alreadyAdded){
							docs.add(postingsTemp.getID());
						}
						alreadyAdded = false;
						postingsTemp = postingsTemp._next;
					}
					temp = _freqList;
					break;
				}
				temp = temp._next;
			}
		}
		endTime = System.nanoTime();
		totalTime = (double) ((endTime - startTime)/1000000000);
		//		System.out.println("Time take:  "+ totalTime);
		Collections.sort(docs);
		Query = "Term at a time OR results:  ";
		for(int doc : docs){
			Query = Query + doc + "  ";
			finalReturnList.add(""+doc);
		}
		if(docs.size() == 0){
			Query = Query + "terms not found";
		}
		else{
			finalReturnList.add(""+totalTime);
			finalReturnList.add(""+comparisons);
		}
		//		System.out.println(comparisons);
		return finalReturnList;
	}

	public boolean isListNull(ArrayList<DocIDLinkedListNode> DocList){
		boolean isNull = true;
		for(DocIDLinkedListNode node : DocList){
			if(node != null){
				isNull = false;
			}
		}
		return isNull;
	}

	public boolean isPartOfListNull(ArrayList<DocIDLinkedListNode> DocList){
		for(DocIDLinkedListNode node : DocList){
			if(node == null){
				return true;
			}
		}
		return false;
	}

	
	/*
	 * Below is the method that finds the doc-at-a-time-and query. It loops through he input arrayList, which holds the query terms, 
     * and then iterates through the linked list that is sorted by docID and added the first node of a term's postings list to another array list, docNodes.
     * Afterwards, it enters a while loop that will end when any one of the nodes in docNodes reaches the end of it's list(i.e. node.Next = null). At every iteration
     * it will loop through docNodes to find the highest document ID currently among the current nodes in the list, and also sets a boolean to false 
     * if all of the IDs are not currently equal. If it finds that all of the nodes are equal, then it will add the max value to the final return list.
     * If all of the nodes do not have the same document ID, then it will loop through docNodes and find every node that has a lower ID, and replace it with
     * the next node in that node's linked list. A comparison counter is incremented every time a node's ID is compared against it's neighbor in docNodes. 
     * The method will return an arrayList that holds all of the document IDs that are shared among all of the query terms, and the last two spots in 
     * the list will hold the time taken to complete the operation, and the number of comparisons used.
	 */
	public ArrayList<String> docAtATimeQueryAnd(ArrayList<String> query_terms) {
		ArrayList<Integer> docsList = new ArrayList<Integer>();
		ArrayList<String> finalReturnList = new ArrayList<String>();
		LinkedListNode temp = _docIDList;
		String Query = "";
		int comparisons = 0;
		double startTime = System.nanoTime();
		double endTime;
		double totalTime;
		boolean allEqual = true;
		int currentMaxID = 0; 

		ArrayList<DocIDLinkedListNode> docNodes = new ArrayList<DocIDLinkedListNode>();
		for(String query : query_terms){
			temp = _docIDList;
			while(temp != null){
				if(temp.getTerm().equals(query)){
					docNodes.add(temp.getFinalPostingsList());
				}
				temp = temp._next;
			}
		}

		while(!(isPartOfListNull(docNodes))){
			allEqual = true;
			currentMaxID = docNodes.get(0).getID();
			for(int i = 0; i < docNodes.size()-1; i++){

				comparisons++;
				if(!(docNodes.get(i).getID() == docNodes.get(i+1).getID())){

					allEqual = false;
				}
				//				System.out.println(currentMaxID +"   "+docNodes.get(i).getID());
				if(currentMaxID < docNodes.get(i+1).getID()){
					currentMaxID = docNodes.get(i+1).getID();
				}
			}
			if(allEqual){
				//				System.out.println("hi1");

				docsList.add(currentMaxID);
				for(int i = 0; i < docNodes.size(); i++){

					docNodes.set(i, docNodes.get(i)._next);
				}
			}
			else{

				for(int i = 0; i < docNodes.size(); i++){
					if(docNodes.get(i).getID() != currentMaxID){

						docNodes.set(i, docNodes.get(i)._next);
					}	
					allEqual = true;
				}
			}
		}
		endTime = System.nanoTime();
		totalTime = (double) ((endTime - startTime)/1000000000);
		//		System.out.println("time taken:  "+ totalTime);
		Collections.sort(docsList);
		Query = "Doc at a time AND results:  ";
		for(int doc : docsList){
			Query = Query + doc + "  ";
			finalReturnList.add(""+doc);
		}
		if(docsList.size() == 0){
			Query = Query + "terms not found";
		}
		else{
			finalReturnList.add(""+totalTime);
			finalReturnList.add(""+comparisons);
		}
		//		System.out.println(comparisons);

		return finalReturnList;
	}

/*
 * The following method performs doc at a time or for the input query. It loops through he input arrayList, which holds the query terms, 
 * and then iterates through the linked list that is sorted by docID and added the first node of a term's postings list to another array list, docNodes.
 * Afterwards, it enters a while loop that will end when the aforementioned list no longer has any postings list nodes in it. At every iteration
 * it will loop through docNodes to find the lowest document ID currently among the current nodes in the list, and also sets a boolean to false 
 * if all of the IDs are not currently equal. After finding the minimum ID, it will add that minimum ID to the final list of found documents. 
 * It will then set every pointer in docNodes to the next node in its list if the current node's ID was equal to the minimum value. By ensuring that 
 * an ID is only added to the final list of when it is the minimum ID value, this ensures that duplicates will not be added to the list, while still 
 * making sure that all of the IDs are added eventually. A comparison counter is incremented when ever a node is compared to it's neighbor to see if it
 * is equal. An arraylist is returned that holds all of the document IDs and the number of comparisons and time in the last two spots of the list.
 */
	public ArrayList<String> docAtATimeQueryOr(ArrayList<String> query_terms) {
		ArrayList<Integer> docsList = new ArrayList<Integer>();
		ArrayList<String> finalReturnList = new ArrayList<String>();
		LinkedListNode temp = _docIDList;
		String Query = "";
		int comparisons = 0;
		double startTime = System.nanoTime();
		double endTime;
		double totalTime;
		boolean allEqual = true;
		int currentMinID = 0; 

		ArrayList<DocIDLinkedListNode> docNodes = new ArrayList<DocIDLinkedListNode>();
		for(String query : query_terms){
			temp = _docIDList;
			while(temp != null){
				if(temp.getTerm().equals(query)){
					docNodes.add(temp.getFinalPostingsList());
				}
				temp = temp._next;
			}
		}

		while(!(isListNull(docNodes)) || (docNodes.size() > 0)){
			//			System.out.println(docNodes.size()+" and the value of the first entry in docNodes is "+ docNodes.get(0).getID());
			allEqual = true;
			currentMinID = docNodes.get(0).getID();
			if(docNodes.size() > 1){
				for(int i = 0; i < docNodes.size()-1; i++){

					comparisons++;
					if(!(docNodes.get(i).getID() == docNodes.get(i+1).getID())){

						allEqual = false;
					}
					//				System.out.println(currentMaxID +"   "+docNodes.get(i).getID());
					if(currentMinID > docNodes.get(i+1).getID()){
						currentMinID = docNodes.get(i+1).getID();
					}

				}
			}

			if(allEqual){
				//				System.out.println("hi1");
				//				System.out.println(currentMinID + " equal and the docNodes list size is "+ docNodes.size());
				docsList.add(currentMinID);
				for(int i = 0; i < docNodes.size(); i++){
					//					
					if(currentMinID == 12900 && docNodes.get(i)._next != null){
						//						System.out.println(docNodes.size());//docNodes.get(i)._next.getID());
					}
					if(docNodes.get(i).getID() == currentMinID){
						if(docNodes.get(i)._next == null){
							docNodes.remove(i);
							i--;
						}
						else{
							docNodes.set(i, docNodes.get(i)._next);
						}
					}
				}

			}
			else {
				//				System.out.println(currentMinID + " not equal");
				docsList.add(currentMinID);
				for(int i = 0; i < docNodes.size(); i++){
					if(docNodes.get(i).getID() == currentMinID){

						if(docNodes.get(i)._next == null){
							docNodes.remove(i);
							i--;
						}
						else{
							docNodes.set(i, docNodes.get(i)._next);
						}

					}	

				}
				allEqual = true;
			}
		}


		endTime = System.nanoTime();
		totalTime = (double) ((endTime - startTime)/1000000000);
		//		System.out.println("time taken:  "+ totalTime);
		Collections.sort(docsList);
		Query = "Doc at a time AND results:  ";
		for(int doc : docsList){
			Query = Query + doc + "  ";
			finalReturnList.add(""+doc);
		}
		if(docsList.size() == 0){
			Query = Query + "terms not found";
		}
		else{
			finalReturnList.add(""+totalTime);
			finalReturnList.add(""+comparisons);
		}
		//		System.out.println(comparisons);

		return finalReturnList;
	}
}
