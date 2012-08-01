package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.Collections;
import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import model.graph.VertexComparator;
import arch.MarcianArch;
import arch.WorldModel;

public class select_opponent_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 8122474421207101417L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		List<Vertex> zone = model.getBestOpponentZone();
		if (null != zone && !zone.isEmpty()) {
			// order neighbors by vertex value
			VertexComparator comparator = new VertexComparator();
			Collections.sort(zone, comparator);

			Vertex bestVertex = null;
			for (Vertex v : zone) {
				if (!model.containsOpponentSaboteurOnVertex(v)) {
					bestVertex = v;
					break;
				}
			}
			if (null != bestVertex) {
				String vertex = "vertex" + bestVertex.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}
		// else go 'attack' the closer opponent (not saboteur)
		Entity closerOpponent = model.getCloserActiveOpponentNotSaboteur();
		if (null != closerOpponent) {
			Vertex vCloserOpponent = closerOpponent.getVertex();
			if (null != vCloserOpponent) {
				String vertex = "vertex" + vCloserOpponent.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}
		// else go to the least visited vertex
		int nextMove = graph.returnLeastVisitedNeighbor(model.getMyVertex().getId());
		String vertex = "vertex" + nextMove;
		return un.unifies(terms[0], ASSyntax.createString(vertex));
	}
}
