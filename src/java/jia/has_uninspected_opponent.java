package jia;

import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * Returns true or false indicating if exists an uninspected opponent in the graph.
 * </p>
 * Use: jia.has_uninspected_opponent;</br>
 * 
 * @author mafranko
 */
public class has_uninspected_opponent extends DefaultInternalAction {

	private static final long serialVersionUID = 971151577602578564L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		return model.hasUninspectedOpponent();
	}

}
