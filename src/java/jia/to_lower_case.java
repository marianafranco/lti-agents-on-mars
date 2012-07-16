package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

/**
 * Converts all of the characters in the passed string to lower case using the rules of the
 * default locale.
 * </p>
 * Use: jia.to_lower_case(+X,-Y); </br>
 * Where: X is the string to be converted, and Y is the string with all characters on lower case.
 * 
 * @author mafranko
 */
public class to_lower_case extends DefaultInternalAction {

	private static final long serialVersionUID = 939740900164809743L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		 String string = ((StringTerm) terms[0]).getString();
		 string = string.toLowerCase();
		 return un.unifies(terms[1], new Atom(string));
	}
}
