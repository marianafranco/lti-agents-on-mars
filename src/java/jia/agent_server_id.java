package jia;

import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * Retrieves the coworker ID.
 * </p>
 * Use: jia.agent_server_id(+Ag,-Id); </br>
 * Where: Ag is the agent name, and Id is agent's id (ex: a10).
 * 
 * @author mafranko
 */
public class agent_server_id extends DefaultInternalAction {

	private static final long serialVersionUID = 4384102918958466437L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String agentName = ((StringTerm) terms[0]).getString();
		if (null == agentName) {
			agentName =  ((Atom) terms[0]).getFunctor();
		}
		agentName = agentName.replace("marcian", "");
		agentName = WorldModel.usernamePrefix + agentName;
		return un.unifies(terms[1], ASSyntax.createString(agentName));
	}
}
