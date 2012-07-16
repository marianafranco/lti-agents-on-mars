package jia;

import model.graph.Graph;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Path legth.
 * </p>
 * Use: jia.path_length(+X,+Y,-D); </br>
 * Where: X is the start location, Y is the end location and D is the distance.
 * 
 * @author mafranko
 */
public class path_length extends DefaultInternalAction {

	private static final long serialVersionUID = -4871787944544975153L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String vertex1 = ((Atom) terms[0]).getFunctor();
		vertex1 = vertex1.replace("vertex", "");
		String vertex2 = ((StringTerm) terms[1]).getString();
		vertex2 = vertex1.replace("vertex", "");
		int v1 = Integer.parseInt(vertex1);
		int v2 = Integer.parseInt(vertex2);

		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		int distance = graph.getDistance(v1, v2);
		return un.unifies(terms[2], new NumberTermImpl(distance));
	}
}
