package jia;

import model.graph.Graph;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

/**
 * Returns the neighbor least visited of the given position.
 * </p>
 * Use: jia.least_visited_neighbor(+P,-N); </br>
 * Where: P is the position and N is the neighbor.
 * 
 * @author mafranko
 */
public class least_visited_neighbor extends DefaultInternalAction {

	private static final long serialVersionUID = 3980449556511086140L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String vertex1 = ((Atom) terms[0]).getFunctor();
		vertex1 = vertex1.replace("vertex", "");
		int v1 = Integer.parseInt(vertex1);

		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		int nextMove = graph.returnLeastVisitedNeighbor(v1);
		String vertex = "vertex" + nextMove;
		return un.unifies(terms[1], ASSyntax.createString(vertex));
	}
	
}
