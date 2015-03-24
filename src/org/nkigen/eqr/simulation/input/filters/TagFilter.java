package org.nkigen.eqr.simulation.input.filters;

import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Relation;
import info.pavie.basicosmparser.model.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nkigen.eqr.simulation.input.SimulationPoint;
import org.nkigen.maps.routing.EQRPoint;

public class TagFilter implements SimulationInputFilter {
	public static final String HIGHWAY = "highway";

	List<String> filters;

	public TagFilter() {
		filters = new ArrayList<String>();
	}

	public TagFilter(List<String> filters) {
		this.filters = filters;
	}

	public List<String> getFilters() {
		return filters;
	}

	public void setFilters(List<String> filters) {
		this.filters = filters;
	}

	@Override
	public boolean isFilter(Object filter) {
		return filters.contains(filter);
	}

	List<Node> getNodes(Element e, List<Node> nodes) {
		if (nodes == null)
			nodes = new ArrayList<Node>();
		if (e instanceof Node) {
			nodes.add((Node) e);
		} else if (e instanceof Way)
			nodes.addAll(((Way) e).getNodes());
		else if (e instanceof Relation)
			for (Element elem : ((Relation) e).getMembers()) {
				getNodes(elem, nodes);
			}
		return nodes;
	}

	@Override
	public ArrayList<SimulationPoint> filterInput(Map<String, Element> result) {
		ArrayList<SimulationPoint> input = new ArrayList<SimulationPoint>();
		for (Entry<String, Element> e : result.entrySet()) {

			for (String tag : e.getValue().getTags().keySet()) {
				if (isFilter(tag)) {
					EQRPoint point = null;
					System.out.println(tag);
					for (Node n : getNodes(e.getValue(), null)) {
						point = new EQRPoint(n.getLat(), n.getLon());
						input.add(new SimulationPoint(point));
						
					}

				}
			}

		}

		return input;
	}
}
