package notenet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.application.Platform;

import cx.fbn.nevernote.Global;
import cx.fbn.nevernote.config.InitializationException;

/**
 * @author Edwin
 * This class holds on to a bounded number of active notes, storing
 * them in a priority queue ordered by their inverse activation to 
 * pull off the least activated whenever a new note is added. 
 * 
 */

public class ActivationTable{
	
	private int bound;
	private int size;
	private double minThreshold = 0.1;
	protected List<ActivationNode> heap;
	final private Comparator<ActivationNode> comparator;
	private VisualizerWindow visualizer;
	
	public ActivationTable(int bound){
		comparator = new Comparator<ActivationNode>(){
			public int compare(ActivationNode arg0, ActivationNode arg1) {
				return arg0.compareTo(arg1);
				}
			};
		this.bound = bound;
		size = 0;
		heap = new ArrayList<ActivationNode>(bound+1);
	}
	
	public void activate(String guid, double actChange, String from, double linkStrength){
		if(guid==null) return;
		ActivationNode act; 
		int actIndex = heap.indexOf(new ActivationNode(guid)); 
		if(actIndex>=0){
			act  = (ActivationNode) remove(actIndex);
			act.changeActivation(actChange);
			insert(act);
			if(Global.view != null) Global.view.boost(act, from, linkStrength);
		}
		else{
			String name = "";
			if(Global.linksTable!=null)
				name = Global.linksTable.getName(guid);
			act = new ActivationNode(guid, name, actChange);
			insert(act);
			if(Global.view != null) Global.view.add(act, from, linkStrength);
//			if(size>=bound){
//				if(Global.view != null) Global.view.remove(pop());
//				else pop();
//			}
		}
		if(actChange>Global.ACTIVAION_PROPOGATION_THRESHOLD){
			for(Entry<String,Double> e: Global.linksTable.getLinks(guid).entrySet()){
				activate(e.getKey(), e.getValue()*actChange*Global.ACTIVAION_PROPOGATION_DAMPENING, guid, e.getValue());
			}
		}
		if(from==null)
			if(Global.view!=null)
				Global.view.start();
	}
	
	private ActivationNode remove(int actIndex) {
		size--;
		ActivationNode removed = heap.remove(actIndex);
		return removed;
	}

	public void fadeActivation(double proportionFade){
		ArrayList<ActivationNode> deactivate = new ArrayList<ActivationNode>(bound/4);
		for(ActivationNode act : heap){
			act.fade(proportionFade);
//			if(Global.view != null) Global.view.fade(act);
			if(act.getActivation()<minThreshold){
				deactivate.add(act);
			}
		}
		for(ActivationNode act : deactivate){
			remove(heap.indexOf(act));
//			if(Global.view != null) Global.view.remove(act);
		}
		while(heap.size()>bound){
			deactivate.add(pop());
		}
		if(Global.view!=null){
			Global.view.removeAll(deactivate);
			Global.view.fadeAll(heap);
		}
//		if(Global.view!=null) Global.view.start();
	}
	
	public List<ActivationNode> getActivatedNotes(){
		List<ActivationNode> ret = toSortedList();
		Collections.reverse(ret);
		return ret;
	}
	
	
	
	
	/**
	 * MinHeap methods based on 
	 * http://www.java2s.com/Code/Java/Collections-Data-Structure/Minimumheapimplementation.htm
	 * 
	 * Copyright (c) 2008-2010  Morten Silcowitz.
	 *
	 * This file is part of the Jinngine physics library
	 *
	 * Jinngine is published under the GPL license, available 
	 * at http://www.gnu.org/copyleft/gpl.html. 
	 */
	
	
	public void insert( final ActivationNode node ) {
	    size++;
	    node.setPosition(size-1);
	    heap.add(node);
	    decreaseKey( node );
	    //return node;
	  }

	  public final void clear() {
	    heap.clear();
	    size = 0;
	  }

	  /**
	   * Return a reference to the top-most element on the heap. The method does not change the state
	   * of the heap in any way. O(k).
	   * @return Reference to top-most element of heap
	   */
	  public final ActivationNode top() {
	    return heap.get(0);
	  }

	  //bound check missing

	  /**
	   * Pop an element of the heap. O(lg n) where n is the number of elements in heap.
	   */
	  public ActivationNode pop() {
	    ActivationNode returnNode = top();
	    exchange( 0, size-1 );
	    heap.remove(size-1);
	    size--;

	    //if any elements left in heap, do minHeapify
	    if (size>0) {
	      minHeapify( heap.get(0) );
	    }
	    
	    return returnNode;
	  }
	  
	  private final boolean decreaseKey( final ActivationNode node ) {
		    int index = node.getPosition();
		    boolean modified = false;

		    //    while ( index>0 &&  (heap[parent(index)]).compareTo( heap[index]) >= 0 ) {
		    while ( index>0 &&  comparator.compare(heap.get(parent(index)), heap.get(index) ) >= 0 ) {
		      exchange( index, parent(index) );
		      index = parent(index);
		      modified = true;
		    }

		    return modified;
		  }
	  
	  private final void minHeapify( final ActivationNode node ) {
		    int smallest;
		    int index = node.getPosition();
		    int left = left(index);
		    int right = right(index);

		    //  if (left<size && (heap[left]).compareTo(heap[index]) <= 0 )
		    if (left<size && comparator.compare(heap.get(left), heap.get(index)) <= 0 )
		      smallest= left;
		    else
		      smallest = index;

		    //    if (right<size && (heap[right]).compareTo(heap[smallest]) <=0 )
		    if (right<size && comparator.compare(heap.get(right), heap.get(smallest) ) <=0 )
		      smallest= right;
		    if (smallest!= index) {
		      exchange( index, smallest );
		      minHeapify( heap.get(smallest) );
		    }
		  }
	  
	  private final void exchange( final int index, final int index2 ) {
		    ActivationNode temp = heap.get(index);
		    temp.setPosition(index2);

		    ActivationNode temp2 = heap.get(index2);
		    temp2.setPosition(index);

		    heap.set(index, temp2 );
		    heap.set( index2, temp);


		    //Update posistion in Node
		    //    heap.get(index).position=index;
		    //    heap.get(index2).position=index2;
		  }


		  private final int parent(final int i) {
		    return i/2;
		  }

		  private final int left(final int i) {
		    return 2*i;
		  }

		  private final int right(final int i) {
		    return 2*i+1;
		  }
		  
		  private List<ActivationNode> toSortedList(){
			  int size = this.size;
			  List<ActivationNode> originalList = new ArrayList<ActivationNode>(heap);
			  List<ActivationNode> sortedList = new ArrayList<ActivationNode>(size+1);
			  while(this.size>0){
				  ActivationNode popped = pop();
				  sortedList.add(popped);
			  }
			  heap = originalList;
			  this.size = size;
			  return sortedList;
		  }

		public List<String> getFullyActive() {
			  List<String> fullyActiveList = new ArrayList<String>();
			  for(ActivationNode act : toSortedList()){
				  if(act.getActivation()==1){
					  fullyActiveList.add(act.getNoteGuid());
				  }
			  }
			  return fullyActiveList;
		}

}
