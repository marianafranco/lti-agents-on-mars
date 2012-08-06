package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Retrieves the position of a not probed neighbor if there is at least one,
 * or the position of the least visited neighbor.
 * </p>
 * Use: jia.move_to_not_probed(+P,-N); </br>
 * Where: P is the position and N is the neighbor.
 * 
 * @author mafranko
 */
public class move_to_not_probed extends DefaultInternalAction {

	private static final long serialVersionUID = -5827900602735423794L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		Vertex myPosition = model.getMyVertex();

		List<Vertex> notProbedNeighbors = graph.returnTeamNotProbedNeighbors(myPosition);
		if (null != notProbedNeighbors && !notProbedNeighbors.isEmpty()) {
			List<Vertex> possibleMoves = new ArrayList<Vertex>();
			for (Vertex notProbedNeighbor : notProbedNeighbors) {
				if (!model.hasActiveSaboteurOpponentOnVertex(notProbedNeighbor)) {
					possibleMoves.add(notProbedNeighbor);
				}
			}
			if (!possibleMoves.isEmpty()) {
				String vertex = "vertex" + possibleMoves.get(0).getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}

		List<Vertex> teamNotProbedVertices = graph.returnTeamNotProbedVertices();
		if (null != teamNotProbedVertices && !teamNotProbedVertices.isEmpty()) {
			List<Vertex> possibleMoves = new ArrayList<Vertex>();
			for (Vertex notProbed : teamNotProbedVertices) {
				if (!model.hasActiveSaboteurOpponentOnVertex(notProbed)) {
					possibleMoves.add(notProbed);
				}
			}
			if (!possibleMoves.isEmpty()) {
				// go to more closer vertex
				int minDist = Integer.MAX_VALUE;
				int closerPosition = -1;
				for (Vertex v : possibleMoves) {
					int dist = graph.getDistance(myPosition, v);
					if (dist < minDist) {
						closerPosition = v.getId();
					}
				}
				if (closerPosition != -1) {
					String vertex = "vertex" + closerPosition;
					return un.unifies(terms[0], ASSyntax.createString(vertex));
				}
			}
		}

		notProbedNeighbors = graph.returnNotProbedNeighbors(myPosition);
		if (null != notProbedNeighbors && !notProbedNeighbors.isEmpty()) {
			String vertex = "vertex" + notProbedNeighbors.get(0).getId();
			return un.unifies(terms[0], ASSyntax.createString(vertex));
		}

		// go to the least visited vertex
		int nextMove = graph.returnLeastVisitedNeighbor(myPosition.getId());
		if (nextMove == -1) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		}
		String vertex = "vertex" + nextMove;
		return un.unifies(terms[0], ASSyntax.createString(vertex));
	}

}
