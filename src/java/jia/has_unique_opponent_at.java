package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if has an unique opponent at the given position.
 * </p>
 * Use: jia.has_unique_opponent_at(+P);</br>
 * Where: P is the position.
 * 
 * @author mafranko
 */
public class has_unique_opponent_at extends DefaultInternalAction {

	private static final long serialVersionUID = -2843424196718445042L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String position = ((Atom) terms[0]).getFunctor();
		position = position.replace("vertex", "");
		int pos = Integer.parseInt(position);
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		if (model.hasUniqueActiveOpponentOnVertex(pos)) {
			String opponentName = model.getOpponentName(pos);
			return un.unifies(terms[1], ASSyntax.createString(opponentName));
		} else {
			return false;
		}
	}
}
