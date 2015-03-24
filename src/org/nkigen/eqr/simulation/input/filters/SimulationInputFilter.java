package org.nkigen.eqr.simulation.input.filters;

import info.pavie.basicosmparser.model.Element;

import java.util.ArrayList;
import java.util.Map;

import org.nkigen.eqr.simulation.input.SimulationPoint;

public interface SimulationInputFilter {

	public abstract boolean isFilter(Object filter);
	public abstract ArrayList<SimulationPoint> filterInput(Map<String, Element> result);

}