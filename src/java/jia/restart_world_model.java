package jia;

import arch.CoordinatorArch;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class restart_world_model extends DefaultInternalAction {

	private static final long serialVersionUID = -3578681262463903329L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = null;
		if (ts.getUserAgArch().getAgName().equals("coordinator")) {
			model = ((CoordinatorArch) ts.getUserAgArch()).getModel();
		} else {
			model = ((MarcianArch) ts.getUserAgArch()).getModel(); 
		}
		model.restart();
		return true;
	}

}
