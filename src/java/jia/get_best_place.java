package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import model.graph.VertexComparator;
import arch.MarcianArch;
import arch.WorldModel;

public class get_best_place extends DefaultInternalAction {

	private static final long serialVersionUID = 3925844185488340425L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		ListTerm positions = new ListTermImpl();
		ListTerm agents = new ListTermImpl();

		List<Set<Vertex>> bestPlaces =  graph.getBestPlaces();
		if (!bestPlaces.isEmpty()) {
			List<Vertex> bestPlace = new ArrayList<Vertex>(bestPlaces.get(0));
			VertexComparator comparator = new VertexComparator();
			Collections.sort(bestPlace, comparator);
			
			List<Entity> coworkers = model.getCoworkersToOccupyZone();
			Entity me = model.getAgentEntity();
			if (me.getName().equals("no-named")) {
				me.setName(ts.getUserAgArch().getAgName());
			}
			coworkers.add(0, me);

			for (Entity ag : coworkers) {
				if (!bestPlace.isEmpty() && !bestPlace.contains(ag.getVertex())) {
					Vertex v = bestPlace.remove(0);
					agents.add(ASSyntax.createString(ag.getName()));
					positions.add(ASSyntax.createString("vertex" + v.getId()));
				}
				
			}
		}

		return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
	}
}
