package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Retrieves the position of the given coworker.
 * </p>
 * Use: jia.agent_position(+Ag,-Pos); </br>
 * Where: Ag is the agent name, and Pos is agent's position (ex: vertex10).
 * 
 * @author mafranko
 */
public class agent_position extends DefaultInternalAction {

	private static final long serialVersionUID = -1524646971431382627L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String agentName = ((StringTerm) terms[0]).getString();
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex v = model.getCoworkerPosition(agentName);
		if (null == v) {
			return un.unifies(terms[1], new Atom("null"));
		}
		return un.unifies(terms[1], new Atom("vertex" + v.getId()));
	}
}
