package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import arch.MarcianArch;
import arch.WorldModel;

public class set_my_status  extends DefaultInternalAction {

	private static final long serialVersionUID = 1323569319099989862L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String status = ((Atom) terms[0]).getFunctor();
		if (null == status) {
			status = ((StringTerm) terms[0]).getString();
		}
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		model.setAgentStatus(status);
		return true;
	}

}
