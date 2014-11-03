package org.nkigen.eqr.agents.ontologies.routing;

import java.io.Serializable;


/**
 * 
 * @author nkigen
 *
 */
public class EQRRoutingResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5193316627578157563L;
	public static int RESULT_ERROR = 0;
	public static int RESULT_ROUTE = 1;
	private int result_type;
	
	public EQRRoutingResult(int type){
		result_type = type;
	}
	public EQRRoutingResult(){
		result_type = RESULT_ROUTE;
	}

}
