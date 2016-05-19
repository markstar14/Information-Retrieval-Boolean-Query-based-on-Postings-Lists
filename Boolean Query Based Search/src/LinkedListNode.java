

import java.util.ArrayList;
import java.util.Comparator;

public class LinkedListNode {

	private String _term;
	
	/*
	 * A temproary list for the DocIDs
	 */
	private ArrayList<Pair> _intermediatePostingList;
	
	/*
	 * The linked list that represents the postings list.
	 */
	private DocIDLinkedListNode _postingsList;
	private int _numberOfDocs;
	
	
	public LinkedListNode _next;
	//private LinkedListNode prev;
	
	public LinkedListNode(String term, ArrayList<Pair> tempList){
		_term = term;
		_intermediatePostingList = tempList;
		_next= null;
	}
	
	/*
	 * Iterates through the array lists holding all of the docIDs and creates a DocIDLinkedListNode for each ID. This is the list used for the queries
	 */
	public void convertToLinkedList(){
		_postingsList = new DocIDLinkedListNode(_intermediatePostingList.get(0).getDocID(), _intermediatePostingList.get(0).getfrequency());
		DocIDLinkedListNode temp = _postingsList;
		for(int i = 1; i < _intermediatePostingList.size(); i ++){
			temp._next = new DocIDLinkedListNode(_intermediatePostingList.get(i).getDocID(), _intermediatePostingList.get(i).getfrequency());
			temp = temp._next;
		}
		_numberOfDocs = _intermediatePostingList.size();
	}
	
	public DocIDLinkedListNode getFinalPostingsList(){
		return _postingsList;
	}
	
	public int getNumberOfDocs(){
		return _numberOfDocs;
	}
	
	public void setNext(LinkedListNode node){
		_next = node;
	}
	
	public LinkedListNode getNext(){
		return _next;
	}
	
	public String getTerm(){
		return _term;
	}
	
	public ArrayList<Pair> getInterPostingList(){
		return _intermediatePostingList;
	}
	
	
	
	public static Comparator<LinkedListNode> docIDComparator = new Comparator<LinkedListNode>() {

		public int compare(LinkedListNode n1, LinkedListNode n2) {
		   Integer n1DocNumber = n1.getNumberOfDocs();
		   Integer n2DocNumber = n2.getNumberOfDocs();

		   
		   return n2DocNumber - n1DocNumber;
	    }};
}
