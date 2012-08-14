package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.List;
import java.util.Set;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

public class is_on_best_place extends DefaultInternalAction {

	private static final long serialVersionUID = 6055904827909060814L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		List<Vertex> myActualBestZone = model.getBestZone();
		List<Set<Vertex>> bestPlaces =  graph.getBestPlaces();

		if (!bestPlaces.isEmpty()) {
			Set<Vertex> bestPlace = bestPlaces.get(0);
			if (!myActualBestZone.containsAll(bestPlace)) {
				List<Entity> coworkers = model.getCoworkersToOccupyZone();
				coworkers.add(model.getAgentEntity());
				for (Entity agent : coworkers) {
					if (!bestPlace.contains(agent.getVertex())) {
						return false;
					}
				}
			}
		}

		return true;
	}
	
}
