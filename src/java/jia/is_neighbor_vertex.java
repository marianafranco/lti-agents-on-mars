package jia;

import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

public class is_neighbor_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = -9034696305188899426L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		String vertex = ((Atom) terms[0]).getFunctor();
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
