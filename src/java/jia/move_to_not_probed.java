package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		String vertex1 = ((Atom) terms[0]).getFunctor();
		vertex1 = vertex1.replace("vertex", "");
		int v1 = Integer.parseInt(vertex1);

		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		List<Vertex> notProbedNeighbors = graph.returnNotProbedNeighbors(v1);
		if (null == notProbedNeighbors || notProbedNeighbors.isEmpty()) {
			int nextMove = graph.returnLeastVisitedNeighbor(v1);
			if (nextMove == -1) {
				return un.unifies(terms[1], ASSyntax.createString("none"));
			}
			String vertex = "vertex" + nextMove;
			return un.unifies(terms[1], ASSyntax.createString(vertex));
		} else {
			List<Vertex> possibleMoves = new ArrayList<Vertex>();
			for (Vertex notProbedNeighbor : notProbedNeighbors) {
				if (!model.hasActiveOpponentOnVertex(notProbedNeighbor)) {
					possibleMoves.add(notProbedNeighbor);
				}
			}
			if (!possibleMoves.isEmpty()) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(possibleMoves.size());
				String vertex = "vertex" + possibleMoves.get(randomInt).getId();
				return un.unifies(terms[1], ASSyntax.createString(vertex));
			} else {
				String vertex = "vertex" + notProbedNeighbors.get(0).getId();
				return un.unifies(terms[1], ASSyntax.createString(vertex));
			}
		}
	}

}
