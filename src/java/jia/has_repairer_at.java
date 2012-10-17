package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns true or false indicating if exists another repairer in the given position.
 * </p>
 * Use: jia.has_repairer_at(+P);</br>
 * Where: P is the position.
 * 
 * @author mafranko
 */
public class has_repairer_at extends DefaultInternalAction {

	private static final long serialVersionUID = 1586273788080285500L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String position = ((StringTerm) terms[0]).getString();
		if (null == position) {
			position =  ((Atom) terms[0]).getFunctor();
		}
		position = position.replace("vertex", "");
		int pos = Integer.parseInt(position);
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		return model.hasRepairerOnVertex(pos);
	}
}
