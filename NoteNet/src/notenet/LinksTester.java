package notenet;

import java.util.Map.Entry;

public class LinksTester {
	
	public static void main(String[] args){
		LinkStore store = new LinkStore();
		store.addNote("1");
		store.addNote("2");
		store.addNote("3");
		store.setLink("1", "3", 0.5);	
		for(Entry<String,Double> e : store.getLinks("1").entrySet()){
			System.out.println(e.getKey() + " with weight " + e.getValue());
		}
	}

}
