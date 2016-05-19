

import java.util.Comparator;

public class Pair {

	private int _frequencyOfTerm;
	private int _docID;
	
	private String _term;
	
	public Pair(String term, int frequency){
		_frequencyOfTerm = frequency;
		_term = term;
	}
	
	public Pair(int docID, int frequency){
		_docID = docID;
		_frequencyOfTerm = frequency;
	}
	
	/*Comparator for sorting the list by docID*/
	public static Comparator<Pair> docIDComparator = new Comparator<Pair>() {

		public int compare(Pair p1, Pair p2) {
		   Integer p1DocID = p1.getDocID();
		   Integer p2DocID = p2.getDocID();

		   
		   return p1DocID- p2DocID;
	    }};

	    /*Comparator for sorting the list by frequency*/
	    public static Comparator<Pair> freqComparator = new Comparator<Pair>() {

		public int compare(Pair p1, Pair p2) {

		   int p1Freq = p1.getfrequency();
		   int p2Freq = p2.getfrequency();

		   return p2Freq-p1Freq; 
	   }};
	
	   
	   
	   
	   
	public int getfrequency(){
		return _frequencyOfTerm;
	}
	
	public int getDocID(){
		return _docID;
	}
	
	public String getTerm(){
		return _term;
	}
	
}
