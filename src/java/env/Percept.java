package env;

/**
 * Defines some constants used in the communication with the server or between the agents.
 * 
 * @author mafranko
 */
public class Percept {

	public final static String edges = "edges";
	public final static String vertices = "vertices";

	public final static String visibleEdge = "visibleEdge";
	public final static String visibleVertex = "visibleVertex";
	public final static String visibleEntity = "visibleEntity";

	public final static String probedVertex = "probedVertex";
	public final static String surveyedEdge = "surveyedEdge";
	public final static String inspectedEntity = "inspectedEntity";

	public final static String position = "position";

	public final static String TEAM_NONE = "none";
	public final static String TEAM_UNKNOWN = "unknown";

	public final static String STATUS_UNKNOWN = "unknown";
	public final static String STATUS_DISABLED = "disabled";
	public final static String STATUS_NORMAL = "normal";

	public final static String ROLE_UNKNOWN = "unknown";
	public final static String ROLE_EXPLORER = "explorer";
	public final static String ROLE_INSPECTOR = "inspector";
	public final static String ROLE_REPAIRER = "repairer";
	public final static String ROLE_SABOTEUR = "saboteur";
	public final static String ROLE_SENTINEL = "sentinel";

	/* Team Percepts */
	public final static String coworkerPosition = "coworkerPosition";
	public final static String coworkerStatus = "coworkerStatus";
	public final static String coworker = "coworker";
	public final static String saboteur = "saboteur";

	public final static String MISSION_UNKNOWN = "unknown";
}
