package org.nkigen.eqr.simulation.input;

import info.pavie.basicosmparser.model.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.nkigen.eqr.simulation.input.filters.SimulationInputFilter;
import org.nkigen.eqr.simulation.input.filters.TagFilter;
import org.nkigen.maps.routing.FindSingleRouteBehaviour;

public class FetchSimulationPoints extends ParseOSMMap {

	SimulationInputFilter filter;

	public FetchSimulationPoints(String file) {
		super(file);
		this.filter = new TagFilter();
	}

	public FetchSimulationPoints(String file, SimulationInputFilter filter) {
		super(file);
		this.filter = filter;
	}

	public List<SimulationPoint> findSimulationPoints() {
		return filter.filterInput(result);
	}

	public SimulationInputFilter getFilter() {
		return filter;
	}

	public void setFilter(SimulationInputFilter filter) {
		this.filter = filter;
	}
	

	public static void main(String[] args) {
		FetchSimulationPoints p = new FetchSimulationPoints(
				"/home/nkigen/development/git/EQR/src/trentino.xml");
		ArrayList<String> f = new ArrayList<String>();
		f.add(TagFilter.HIGHWAY);
		p.setFilter(new TagFilter(f));
		List<SimulationPoint> pts = p.findSimulationPoints();
		System.out.println(pts.size());

	}

}
