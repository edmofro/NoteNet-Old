package notenet;

import java.util.Map.Entry;

public class LinksTester {
	
	public static void main(String[] args){
		LinkStore store = new LinkStore();
		store.addNote("1", "Test one");
		store.addNote("2", "Hello");
		store.addNote("3", "Blah");
		store.setLink("1", "3", 0.5);	
		for(Entry<String,Double> e : store.getLinks("1").entrySet()){
			System.out.println(e.getKey() + " with weight " + e.getValue());
		}
	}

}
