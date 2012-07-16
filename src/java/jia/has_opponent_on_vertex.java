package jia;

import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * Returns true or false indicating if exists an opponent in the same vertex as me.
 * </p>
 * Use: jia.has_opponent_on_vertex;</br>
 * 
 * @author mafranko
 */
public class has_opponent_on_vertex extends DefaultInternalAction {

	private static final long serialVersionUID = 1182494310168253005L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		return model.hasActiveOpponentOnVertex(myPosition);
	}
}
