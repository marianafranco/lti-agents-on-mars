package jia;

import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * Returns true or false to indicate if there are opponents to be inspected near my position.
 * </p>
 * Use: jia.is_inspect_goal; </br>
 * 
 * @author mafranko
 */
public class is_inspect_goal extends DefaultInternalAction {

	private static final long serialVersionUID = -7144570990733006566L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		if (null == myPosition) {
			return false;
		}
		if (model.hasOpponentOnVertex(myPosition)) {
			return true;
		}
		for (Vertex neighbor : myPosition.getNeighbors()) {
			if (model.hasOpponentOnVertex(neighbor)) {
				return true;
			}
		}
		return false;
	}
}
