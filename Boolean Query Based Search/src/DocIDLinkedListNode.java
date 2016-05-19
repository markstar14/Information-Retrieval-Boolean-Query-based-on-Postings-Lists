

public class DocIDLinkedListNode {

	private int _docID;
	private int _termFrequency;
	
	public DocIDLinkedListNode _next;
	
	public DocIDLinkedListNode(int ID, int freq){
		_docID = ID;
		_termFrequency = freq;
		_next = null;
	}
	
	public int getID(){
		return _docID;
	}
	
	public int getFrequency(){
		return _termFrequency;
	}
	
}
