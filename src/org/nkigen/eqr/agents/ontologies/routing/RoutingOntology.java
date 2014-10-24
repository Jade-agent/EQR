package org.nkigen.eqr.agents.ontologies.routing;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;

public class RoutingOntology extends Ontology implements
		GraphHopperRoutingVocabulary {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7883004145540575055L;
	public static final String ONTOLOGY_NAME = "routing_ontology";

	private static Ontology instance = new RoutingOntology();

	private RoutingOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		
	}

	public static Ontology getInstance() {
		return instance;
	}

}
