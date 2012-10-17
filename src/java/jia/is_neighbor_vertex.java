package jia;

import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * Returns true or false indicating if the given position is a neighbor vertex of my position.
 * </p>
 * Use: jia.is_neighbor_vertex(+Pos);</br>
 * Where: Pos is the vertex to be checked.
 * 
 * @author mafranko
 */
public class is_neighbor_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = -9034696305188899426L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		String vertex = ((Atom) terms[0]).getFunctor();
		if (null == vertex) {
			vertex = ((StringTerm) terms[0]).getString();
		}
		vertex = vertex.replace("vertex", "");
		int v = Integer.parseInt(vertex);
		for (Vertex neighbor1 : myPosition.getNeighbors()) {
			if (neighbor1.getId() == v) {
				return true;
			}
			for (Vertex neighbor2: neighbor1.getNeighbors()) {
				if (neighbor2.getId() == v) {
					return true;
				}
			}
		}
		return false;
	}
}
