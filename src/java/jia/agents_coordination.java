package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import model.graph.VertexComparator;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Verify which is the team best zone in a determined time and returns a list of
 * agents and positions to increase that zone.
 * </p>
 * Use: jia.agents_coordination(-A,-P); </br>
 * Where: A is the list of agents and P the list of target positions.
 * 
 * @author mafranko
 */
public class agents_coordination extends DefaultInternalAction {

	private static final long serialVersionUID = -6858228332440013608L;

	static Logger logger = Logger.getLogger(agents_coordination.class.getName());

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		synchronized (this) {
			ListTerm positions = new ListTermImpl();
			ListTerm agents = new ListTermImpl();

			WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
			Graph graph = model.getGraph();
			List<Vertex> bestZone = model.getBestZone();	// team zone with the greatest value

//			logger.info("BEST ZONE: " + bestZone);
			if (null == bestZone || bestZone.isEmpty()) {
				return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
			}

			List<Set<Vertex>> bestPlaces =  graph.getBestPlaces();	// the best area in the graph
			List<Vertex> bestPlace = null;
			if (!bestPlaces.isEmpty()) {
				bestPlace = new ArrayList<Vertex>(bestPlaces.get(0));
				bestPlace.removeAll(bestZone);
				VertexComparator comparator = new VertexComparator();
				Collections.sort(bestPlace, comparator);
			}

			List<Vertex> zoneNeighbors = model.getZoneNeighbors(bestZone);

			// order neighbors by vertex value
			VertexComparator comparator = new VertexComparator();
			Collections.sort(zoneNeighbors, comparator);

			// bestNeighbors = neighbors with two or more blue neighbors
			List<Vertex> bestNeighbors = model.getBestZoneNeighbors(zoneNeighbors);
			zoneNeighbors.removeAll(bestNeighbors);

			if (zoneNeighbors.isEmpty() && bestNeighbors.isEmpty()) {
				return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
			}

			List<Entity> coworkers = model.getCoworkersToOccupyZone();
			Entity me = model.getAgentEntity();
			if (me.getName().equals("no-named")) {
			me.setName(ts.getUserAgArch().getAgName());
			}
			coworkers.add(0, me);

//			logger.info("COWORKERS: " + coworkers);
			for (Entity coworker : coworkers) {
				Vertex target = null;
				Vertex agentPosition = coworker.getVertex();
				if (bestZone.contains(agentPosition) && model.isFrontier(agentPosition)) {	// the agent is part of the best zone's frontier
					List<Entity> agsOnSameVertex = model.getCoworkersOnSameVertex(coworker);
					if (!agsOnSameVertex.isEmpty()) {	// if there are other agents on the same vertex
						boolean canMove = true;
						for (Entity ag : agsOnSameVertex) {
							if (ag.getId() > coworker.getId()) {
								canMove = false;	// only the agent with the lower id can move
								break;
							}
						}
						if (!canMove) {
							continue;
						}
					} else {
						continue;	// the agent must not move if he is in the frontier and has no other agent on the same vertex
					}
				}

				if (null != bestPlace && !bestPlace.isEmpty()) {
					target = model.closerVertex(agentPosition, bestPlace);
					if (null != target) {
						bestPlace.remove(target);
					}
				}
				if (null == target && !bestNeighbors.isEmpty()) {
					target = model.closerVertex(agentPosition, bestNeighbors);
					if (null != target) {
						bestNeighbors.remove(target);
					}
				}
				if (null == target && !zoneNeighbors.isEmpty()) {
					target = model.closerVertex(agentPosition, zoneNeighbors);
					if (null != target) {
						zoneNeighbors.remove(target);
					}
				}

				if (null != target) {
					agents.add(ASSyntax.createString(coworker.getName()));
					positions.add(ASSyntax.createString("vertex" + target.getId()));
				}
			}
			return un.unifies(terms[0], agents) & un.unifies(terms[1], positions);
		}
	}
}
