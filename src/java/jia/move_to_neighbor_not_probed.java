package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

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
public class move_to_neighbor_not_probed extends DefaultInternalAction {

	private static final long serialVersionUID = -5827900602735423794L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		Vertex myPosition = model.getMyVertex();

		List<Vertex> notProbedNeighbors = graph.returnNotProbedNeighbors(myPosition);
		if (null == notProbedNeighbors || notProbedNeighbors.isEmpty()) {
			// TODO go to least visited
			int nextMove = -1;
			String vertex = "vertex" + nextMove;
			return un.unifies(terms[0], ASSyntax.createString(vertex));
		} else {
			for (Vertex notProbedNeighbor : notProbedNeighbors) {
				if (notProbedNeighbor.getTeam().equals(WorldModel.myTeam)
						&& !model.hasActiveOpponentOnVertex(notProbedNeighbor)) {
					String vertex = "vertex" + notProbedNeighbor.getId();
					return un.unifies(terms[0], ASSyntax.createString(vertex));
				}
			}
			for (Vertex notProbedNeighbor : notProbedNeighbors) {
				if (!model.hasActiveOpponentOnVertex(notProbedNeighbor)) {
					String vertex = "vertex" + notProbedNeighbor.getId();
					return un.unifies(terms[0], ASSyntax.createString(vertex));
				}
			}
			// TODO go to least visited
			String vertex = "vertex" + "-1";
			return un.unifies(terms[0], ASSyntax.createString(vertex));
		}
	}

}
