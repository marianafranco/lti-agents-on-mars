package jia;

import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * Return true or false indicating if exists an opponent saboteur in the given position.
 * </p>
 * Use: jia.has_saboteur_at(+P);</br>
 * Where: P is the position.
 * 
 * @author mafranko
 */
public class has_saboteur_at extends DefaultInternalAction {

	private static final long serialVersionUID = 2711797878816377722L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String position = ((StringTerm) terms[0]).getString();
		if (null == position) {
			position =  ((Atom) terms[0]).getFunctor();
		}
		position = position.replace("vertex", "");
		int pos = Integer.parseInt(position);
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		return model.hasSaboteurOnVertex(pos);
	}
	
}
