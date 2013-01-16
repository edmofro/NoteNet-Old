package notenet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.*;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;
import org.neo4j.graphdb.index.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.*;

import com.evernote.edam.type.Note;

enum RelTypes implements RelationshipType
{
    LINKS
}

public class LinkStore {
	
	private static String DB_PATH;
	private static Index<Node> nodeIndex;
	GraphDatabaseService graphDb;
	public boolean setup = false;
	
	public LinkStore(){
		DB_PATH = "/Users/Edwin/workspace/NoteNet/linksDatabase";
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		nodeIndex = graphDb.index().forNodes( "nodes" );
		registerShutdownHook( graphDb );
	}
	
	public void addNote(String uid){		
		Transaction tx = graphDb.beginTx();
		try
		{
			Node newNode = graphDb.createNode();
			newNode.setProperty( "uid", uid );
			nodeIndex.add( newNode, "uid", uid );
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}	
	}
	
	public void setLink(String from, String to, double weight){
		Node fromNode = nodeIndex.get("uid", from).getSingle();
		Node toNode = nodeIndex.get("uid", to).getSingle();
		Transaction tx = graphDb.beginTx();
		try
		{
			Relationship relationship = fromNode.createRelationshipTo( toNode, RelTypes.LINKS );
			relationship.setProperty("weight", weight);	
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}
	}
	
	public void calculateLink(Note from, Note to){
		double weight = 0.5;
		
		//Calculate relatedness of titles
		
		//Calculate relatedness of content
		
		//Calculate relatedness of tags
		
		//Calculate relatedness of creation time
		
		//Calculate relatedness of update time
		
		//Calculate relatedness of attributes (do they contain pdfs, images, etc.)
		
		//Calculate relatedness of source url
		
		//Add standard feedback component (for later use)
		
		//Combine into total weight using proportions from fields
		
		Node fromNode = nodeIndex.get("uid", from.getGuid()).getSingle();
		Node toNode = nodeIndex.get("uid", to.getGuid()).getSingle();
		Transaction tx = graphDb.beginTx();
		try
		{
			Relationship relationship = fromNode.createRelationshipTo( toNode, RelTypes.LINKS );
			relationship.setProperty("weight", weight);	
		    tx.success();
		}
		finally
		{
		    tx.finish();
		}		
	}
	
	public Map<String, Double> getLinks(String note){
		HashMap<String, Double> ret = new HashMap<String, Double>();
		Node node = nodeIndex.get("uid", note).getSingle();
		org.neo4j.graphdb.traversal.Traverser linkedNodesTraverser = getLinkedNodes(node);
		try{
			for(Path link : linkedNodesTraverser){
				if(link.length()>0){
					ret.put((String)link.endNode().getProperty("uid"), (Double)link.lastRelationship().getProperty("weight"));
				}
			}
		}
		catch(NullPointerException e){
			System.out.println("Null pointer: " + e.getLocalizedMessage());
		}
		return ret;
	}
	

	private static org.neo4j.graphdb.traversal.Traverser getLinkedNodes(
	        final Node note )
	{
	    TraversalDescription td = Traversal.description()
	            .breadthFirst()
	            .evaluator(Evaluators.toDepth(1))
	            .relationships( RelTypes.LINKS, Direction.BOTH );
	    return td.traverse( note );
	}



private static void registerShutdownHook( final GraphDatabaseService graphDb )
{
    // Registers a shutdown hook for the Neo4j instance so that it
    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // running example before it's completed)
    Runtime.getRuntime().addShutdownHook( new Thread()
    {
        @Override
        public void run()
        {
            graphDb.shutdown();
            //deleteFileOrDirectory(new File(DB_PATH)); //While testing, we want to clear the database when done
        }
    } );
}

public static void deleteFileOrDirectory( final File file ) {
    if ( file.exists() ) {
        if ( file.isDirectory() ) {
            for ( File child : file.listFiles() ) {
                deleteFileOrDirectory( child );
            }
        }
        file.delete();
    }
}

public void clearLinks() {
	graphDb.shutdown();
	deleteFileOrDirectory(new File(DB_PATH));
	graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	nodeIndex = graphDb.index().forNodes( "nodes" );
	registerShutdownHook(graphDb);
}

}