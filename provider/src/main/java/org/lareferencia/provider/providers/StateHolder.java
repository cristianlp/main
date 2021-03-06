
package org.lareferencia.provider.providers;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lareferencia.provider.exceptions.BadResumptionTokenException;

public class StateHolder
{
   private Log log = LogFactory.getLog(getClass());	
   
   /// ****** ******* ///
   List<Long> snapshotIdList;
   List<Integer> totalPagesList;
   Integer actualSnapshotIndex;
   Integer actualPage;
   
   boolean finished = false;

   @Getter @Setter
   boolean firstCall = true;
   
   public StateHolder(String resumptionToken)
   {
	   
	  firstCall = false;
	   
	  snapshotIdList = new ArrayList<Long>();
	  totalPagesList = new ArrayList<Integer>();
	  
      String[] parsedParts = resumptionToken.split("\\|");
      
      if (parsedParts.length != 3)
    	  throw new BadResumptionTokenException("The session could not be found. (malformed)");
      
      String[] parsedSnaps = parsedParts[0].split("\\,");
      
      /// parsing de los snaps y sus páginas
      for (String pair:parsedSnaps) {
    	  String[] parsedPair = pair.split("\\:");
    	  
    	  if (parsedPair.length != 2)
        	  throw new BadResumptionTokenException("The session could not be found. (pairs problem)");

    	  try {
    		  snapshotIdList.add( Long.parseLong(parsedPair[0]) );
    		  totalPagesList.add( Integer.parseInt(parsedPair[1]) );
    	  }
    	  catch (Exception e) {
        	  throw new BadResumptionTokenException("The session could not be found. (pairs problem)");

    	  }
      }
      
      /// parsing del snap actual y su página
      try {
    	  this.actualSnapshotIndex = Integer.parseInt( parsedParts[1]);
    	  this.actualPage = Integer.parseInt( parsedParts[2]); 
      }
      catch (Exception e) {
    	  throw new BadResumptionTokenException("The session could not be found. (state)");
      } 
   }
   
   public StateHolder( ) {
       this.actualPage = 0;
       this.actualSnapshotIndex = 0;
   }
   
   public void initialize(List<Long> snapshotIdList, List<Integer> totalPageList ) {
       this.actualPage = 0;
       this.actualSnapshotIndex = 0;
       this.snapshotIdList = snapshotIdList;
       this.totalPagesList = totalPageList; 
       
       firstCall = true;
   }
  
   public String getResumptionToken() { 
	
	   
	   if (finished)
		   return "";
	   
	   
	   String rt = "";
	   
	   for (int i=0; i<snapshotIdList.size(); i++) {
		   
		   rt += snapshotIdList.get(i).toString() + ":" + totalPagesList.get(i).toString() ;
		   
		   if ( i < snapshotIdList.size() - 1 )
			   rt += ",";
	   }
	   
	   rt += "|" + actualSnapshotIndex.toString() + "|" + actualPage.toString();
	     
	   return rt;   
   }
   
   public void update() {
	   
	   int actualSnapshotTotalPages = totalPagesList.get(actualSnapshotIndex);
	   
	   // Si ya llego al fin de las páginas de este snapshot
	   if ( actualPage == actualSnapshotTotalPages - 1) {
		   
		   // Si ya llego al último snapshot
		   if ( actualSnapshotIndex == snapshotIdList.size() -1 ) {
			   finished = true; // significa que terminó
		   }
		   else { // pasa al próximo snapshot
			   actualPage = 0;
			   actualSnapshotIndex++;
		   }
	   } 
	   // todavía hay páginas en este snapshot
	   else {
		   actualPage++; // incrementa la página
	   }
   }
   
   public Long obtainActualSnapshotID() {
	   return snapshotIdList.get(actualSnapshotIndex);
   }
   
   public int obtainActualPage() {
	   return actualPage;
   }
   
}
