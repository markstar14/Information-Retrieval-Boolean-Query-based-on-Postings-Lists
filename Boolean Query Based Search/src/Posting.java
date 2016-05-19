

import java.util.ArrayList;

public class Posting {

	private ArrayList<Pair> _docList;
	private String _term;
	
	private ArrayList<Pair> _termList;
	private int _docID;
	
	/*The following constructor and methods are used for a 
	 * posting where the term in the key and it contains a 
	 * list of docID*/
	public Posting(String term, ArrayList<Pair> list){
		_term = term;
		_docList = list;
	}
	
	public String getTermName(){
		return _term;
	}
	
	public ArrayList<Pair> getDocList(){
		return _docList;
	}
	
	public void addToDocList(int docID, int frequency){
		_docList.add(new Pair(docID, frequency));
	}
	
	public boolean checkForDocID(int docID){
		for(Pair p : _docList){
			if(docID == p.getDocID()){
				return true;
			}
		}
		return false;
	}
	
	/*The following constructor and methods are used for a 
	 * posting where the docID in the key and it contains a 
	 * list of terms*/
	public Posting(int docID){
		_docID = docID;
		_termList = new ArrayList<Pair>();
	}
	
	public int getDocID(){
		return _docID;
	}
	
	public ArrayList<Pair> getTermList(){
		return _termList;
	}
	
	public void addToTermList(String term, int frequency){
		_termList.add(new Pair(term, frequency));
	}
	
	public boolean checkForTerm(String term){
		for(Pair p : _termList){
			if(term == p.getTerm()){
				return true;
			}
		}
		return false;
	}
}
