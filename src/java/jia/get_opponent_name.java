package jia;

import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

/**
 * Retrieves the name of the opponent which is in the same position as me.
 * </p>
 * Use: jia.get_opponent_name(-A); </br>
 * Where: A is the name of the opponent in the same position as me.
 * 
 * @author mafranko
 */
public class get_opponent_name extends DefaultInternalAction {

	private static final long serialVersionUID = 2815473594430522520L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		String opponentName = model.getOpponentName(myPosition);
		return un.unifies(terms[0], ASSyntax.createString(opponentName));
	}
}
