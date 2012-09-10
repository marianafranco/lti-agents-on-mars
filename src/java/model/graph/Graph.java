package model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import arch.WorldModel;
import env.Percept;

/**
 * Graph class.
 * 
 * @author mafranko
 */
public class Graph {

	/**
	 * Map <id, Vertex>. Id is the vertex number.
	 */
	private HashMap<Integer, Vertex> vertices;

	/**
	 * Map <Edge, value>. Value is the weight of the edge. 
	 */
	private HashMap<Edge, Integer> edges;

	private int maxNumOfVertices = Integer.MAX_VALUE;
	private int maxNumOfEdges = Integer.MAX_VALUE;

	private List<List<Vertex>> bestZones;
	private boolean updated = false;

	public Graph() {
		vertices = new HashMap<Integer, Vertex>();
		edges = new HashMap<Edge, Integer>();
		bestZones = new ArrayList<List<Vertex>>();
	}

	public boolean existsPath(int v1, int v2) {
		Vertex vertex1 = vertices.get(v1);
		Vertex vertex2 = vertices.get(v2);
		return existsPath(vertex1, vertex2);
	}

	public boolean existsPath(Vertex v1, Vertex v2) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(v1);
		while (true) {
			if (frontier.isEmpty()) {
				return false;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			if (v.equals(v2)) {
				return true;
			}
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					if (neighbor.equals(v2)) {
						return true;
					}
					frontier.add(neighbor);
				}
			}
		}
	}

	public boolean existsFrontier(Vertex v1, Vertex v2) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(v1);
		while (true) {
			if (frontier.isEmpty()) {
				return true;	// could not find a path
			}
			Vertex v = frontier.poll();
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)
					&& neighbor.getColor() != Vertex.BLUE) {
					if (neighbor.equals(v2)) {
						return false;
					}
					frontier.add(neighbor);
				}
			}
		}
	}

	public boolean existsOpponentFrontier(Vertex v1, Vertex v2) {
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(v1);
		while (true) {
			if (frontier.isEmpty()) {
				return true;	// could not find a path
			}
			Vertex v = frontier.poll();
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)
					&& neighbor.getColor() != Vertex.RED) {
					if (neighbor.equals(v2)) {
						return false;
					}
					frontier.add(neighbor);
				}
			}
		}
	}

	public List<Vertex> getNotColoredVertices() {
		List<Vertex> notColored = new ArrayList<Vertex>();
		List<Vertex> vList = new ArrayList<Vertex>(vertices.values());
		for (Vertex v : vList) {
			if (v.getColor() == Vertex.WHITE) {
				notColored.add(v);
			}
		}
		return notColored;
	}

	public List<Vertex> getBlueVertices() {
		List<Vertex> blueVertices = new ArrayList<Vertex>();
		for (Vertex v : vertices.values()) {
			if (v.getColor() == Vertex.BLUE) {
				blueVertices.add(v);
			}
		}
		return blueVertices;
	}

	public List<Vertex> getRedVertices() {
		List<Vertex> redVertices = new ArrayList<Vertex>();
		for (Vertex v : vertices.values()) {
			if (v.getColor() == Vertex.RED) {
				redVertices.add(v);
			}
		}
		return redVertices;
	}

	public List<List<Vertex>> getZones() {
		List<List<Vertex>> zones = new ArrayList<List<Vertex>>();
		List<Vertex> blueVertices = getBlueVertices();
		for (Vertex v : blueVertices) {
			List<Vertex> zone = getZone(v);
			zones.add(zone);
			blueVertices.remove(zone);
		}
		return zones;
	}

	public List<Vertex> getZone(Vertex v) {
		List<Vertex> zone = new LinkedList<Vertex>();
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		frontier.add(v);
		while (!frontier.isEmpty()) {
			Vertex vertice = frontier.poll();
			zone.add(vertice);
			Set<Vertex> neighbors = vertice.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!zone.contains(neighbor) && !frontier.contains(neighbor)
					&& neighbor.getColor() == Vertex.BLUE) {
					frontier.add(neighbor);
				}
			}
		}
		return zone;
	}

	public List<List<Vertex>> getOpponentZones() {
		List<List<Vertex>> zones = new ArrayList<List<Vertex>>();
		List<Vertex> redVertices = getRedVertices();
		for (Vertex v : redVertices) {
			List<Vertex> zone = getOpponentZones(v);
			zones.add(zone);
			redVertices.remove(zone);
		}
		return zones;
	}

	public List<Vertex> getOpponentZones(Vertex v) {
		List<Vertex> zone = new LinkedList<Vertex>();
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		frontier.add(v);
		while (!frontier.isEmpty()) {
			Vertex vertice = frontier.poll();
			zone.add(vertice);
			Set<Vertex> neighbors = vertice.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!zone.contains(neighbor) && !frontier.contains(neighbor)
					&& neighbor.getColor() == Vertex.RED) {
					frontier.add(neighbor);
				}
			}
		}
		return zone;
	}

	public int getDistance(Vertex vertex1, Vertex vertex2) {
		if (vertex1.equals(vertex2)) {
			return 0;
		}
		// uses breadth-first search
		vertex1.setDistance(0);
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(vertex1);
		while (true) {
			if (frontier.isEmpty()) {
				return Integer.MAX_VALUE;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					if (neighbor.equals(vertex2)) {
						return v.getDistance() + 1;
					}
					neighbor.setDistance(v.getDistance() + 1);
					frontier.add(neighbor);
				}
			}
		}
	}

	public int returnRandomMove(int v1) {
		Vertex vertex1 = vertices.get(v1);
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(neighbors.size());
		return neighbors.get(randomInt).getId();
	}

	public int returnLeastVisitedNeighbor(int v1) {
		Vertex vertex1 = vertices.get(v1);
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());

		if (neighbors.isEmpty()) {
			return -1;
		}

		Vertex leastVisited = neighbors.remove(0);
		int minValue = leastVisited.getVisited();
		for (Vertex neighbor : neighbors) {
			if (neighbor.getVisited() < minValue) {
				leastVisited = neighbor;
			}
		}
		return leastVisited.getId();
	}

	public List<Vertex> returnNotProbedNeighbors(Vertex vertex1) {
		List<Vertex> notProbedNeighbors = new ArrayList<Vertex>();
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());

		for (Vertex neighbor : neighbors) {
			if (!neighbor.isProbed()) {
				notProbedNeighbors.add(neighbor);
			}
		}
		return notProbedNeighbors;
	}

	public List<Vertex> returnTeamNotProbedNeighbors(Vertex vertex1) {
		List<Vertex> notProbedNeighbors = new ArrayList<Vertex>();
		List<Vertex> neighbors = new ArrayList<Vertex>(vertex1.getNeighbors());

		for (Vertex neighbor : neighbors) {
			if (!neighbor.isProbed() && neighbor.getTeam().equals(WorldModel.myTeam)) {
				notProbedNeighbors.add(neighbor);
			}
		}
		return notProbedNeighbors;
	}

	public List<Vertex> returnTeamNotProbedVertices() {
		List<Vertex> notProbed = new ArrayList<Vertex>();
		for (Vertex v : vertices.values()) {
			if (!v.isProbed() && v.getTeam().equals(WorldModel.myTeam)) {
				notProbed.add(v);
			}
		}
		return notProbed;
	}

	public int returnNextMove(int v1, int v2) {
		Vertex vertex1 = vertices.get(v1);
		vertex1.setParent(null);
		Vertex vertex2 = vertices.get(v2);
		// uses breadth-first search
		Queue<Vertex> frontier = new LinkedList<Vertex>();
		Set<Vertex> explored = new HashSet<Vertex>();
		frontier.add(vertex1);
		while (true) {
			if (frontier.isEmpty()) {
				return -1;	// failure, could not find a path
			}
			Vertex v = frontier.poll();
			explored.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			for (Vertex neighbor : neighbors) {
				if (!explored.contains(neighbor) && !frontier.contains(neighbor)) {
					neighbor.setParent(v);
					if (neighbor.equals(vertex2)) {
						Vertex nextVertex = nextMove(neighbor);
						return nextVertex.getId();
					}
					frontier.add(neighbor);
				}
			}
		}
	}

	private Vertex nextMove(Vertex v) {
		Vertex end = v;
		Vertex parent = v.getParent();
		while (null != parent.getParent()) {
			end = parent;
			parent = end.getParent();
		}
		return end;
	}

	public List<List<Vertex>> getBestZones() {
		if (!updated) {
			return bestZones;
		}

		List<List<Vertex>> newBestZones = new ArrayList<List<Vertex>>();

		List<Vertex> verticesList = new ArrayList<Vertex>(vertices.values());
		VertexComparator comparator = new VertexComparator();
		Collections.sort(verticesList, comparator);

		// best zone
		int maxZoneValue = 0;
		List<Vertex> bestZone = null;
		for (Vertex v : verticesList) {
			List<Vertex> zone = new ArrayList<Vertex>();
			zone.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			zone.addAll(neighbors);
			Set<Vertex> zoneMoreNeighbors = new HashSet<Vertex>(zone);
			for (Vertex neighbor : neighbors) {
				zoneMoreNeighbors.addAll(neighbor.getNeighbors());
			}
			int zoneValue = countZoneValue(zoneMoreNeighbors);
			if (zoneValue > maxZoneValue && zoneValue > 10) {
				maxZoneValue = zoneValue;
				bestZone = zone;
			}
		}
		if (bestZone != null) {
			newBestZones.add(bestZone);
		} else {
			return bestZones;
		}

		// second best zone
		maxZoneValue = 0;
		List<Vertex> secondBestZone = null;
		verticesList.removeAll(bestZone);
		for (Vertex v : verticesList) {
			List<Vertex> zone = new ArrayList<Vertex>();
			zone.add(v);
			Set<Vertex> neighbors = v.getNeighbors();
			zone.addAll(neighbors);
			Set<Vertex> zoneMoreNeighbors = new HashSet<Vertex>(zone);
			for (Vertex neighbor : neighbors) {
				zoneMoreNeighbors.addAll(neighbor.getNeighbors());
			}
			int zoneValue = countZoneValue(zoneMoreNeighbors);
			if (zoneValue > maxZoneValue && zoneValue > 10
					&& !hasAtLeastOneVertexOnZone(zone, bestZone)) {
				maxZoneValue = zoneValue;
				secondBestZone = zone;
			}
		}

		if (secondBestZone != null) {
			newBestZones.add(secondBestZone);
		}

		if (bestZones.size() < 2 || newBestZones.size() < 2) {
			bestZones = newBestZones;
		} else {
			if (hasAtLeastOneVertexOnZone(bestZones.get(0), newBestZones.get(0))) {
				bestZones = newBestZones;
			} else if (hasAtLeastOneVertexOnZone(bestZones.get(0), newBestZones.get(1))) {
				bestZones.set(0, newBestZones.get(1));
				bestZones.set(1, newBestZones.get(0));
			} else {
				bestZones = newBestZones;
			}
		}
		updated = false;
		return bestZones;
	}

	public boolean hasAtLeastOneVertexOnZone(List<Vertex> zone, List<Vertex> vertices) {
		for (Vertex v : vertices) {
			if (zone.contains(v)) {
				return true;
			}
		}
		return false;
	}

	private int countZoneValue(Set<Vertex> zone) {
		int totalValue = 0;
		for (Vertex v : zone) {
			totalValue += v.getValue();
		}
		return totalValue;
	}
	
	public void addVertex(int id, String team) {
		Vertex v = vertices.get(id);
		if (null == v) {
			vertices.put(id, new Vertex(id, team));
			updated = true;
		} else {
			v.setTeam(team);
		}
	}

	public void addVertex(Vertex v) {
		Vertex vertex = vertices.get(v.getId());
		if (null == vertex) {
			vertices.put(v.getId(), v);
			updated = true;
		} else {
			vertex.setTeam(v.getTeam());
		}
	}

	public void addEdge(int v1, int v2) {
		edges.put(new Edge(v1, v2), -1);
		Vertex vertex1 = vertices.get(v1);
		if (null == vertex1) {
			vertex1 = new Vertex(v1, Percept.TEAM_UNKNOWN);
			addVertex(vertex1);
		}
		Vertex vertex2 = vertices.get(v2);
		if (null == vertex2) {
			vertex2 = new Vertex(v2, Percept.TEAM_UNKNOWN);
			addVertex(vertex2);
		}
		vertex1.addNeighbor(vertex2);
		vertex2.addNeighbor(vertex1);
	}

	public boolean containsVertex(int id, String team) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			if (v.getTeam().equals(team)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEdge(int v1, int v2) {
		return edges.containsKey(new Edge(v1, v2));
	}

	public int getVertexValue(int id) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			return v.getValue();
		}
		return -1;
	}

	public void addVertexValue(int id, int value) {
		if (vertices.containsKey(id)) {
			Vertex v = vertices.get(id);
			v.setProbed(true);
			v.setValue(value);
		} else {
			Vertex v = new Vertex(id, Percept.TEAM_UNKNOWN);
			v.setValue(value);
			v.setProbed(true);
			vertices.put(id, v);
		}
		updated = true;
	}

	public int getEdgeValue(int v1, int v2) {
		Edge e = new Edge(v1, v2);
		if (edges.containsKey(e)) {
			return edges.get(e);
		}
		return -1;
	}

	public void addEdgeValue(int v1, int v2, int value) {
		Edge e = new Edge(v1, v2);
		edges.put(e, value);
		Vertex vertex1 = vertices.get(v1);
		if (null == vertex1) {
			vertex1 = new Vertex(v1, Percept.TEAM_UNKNOWN);
			addVertex(vertex1);
		}
		Vertex vertex2 = vertices.get(v2);
		if (null == vertex2) {
			vertex2 = new Vertex(v2, Percept.TEAM_UNKNOWN);
			addVertex(vertex2);
		}
		vertex1.addNeighbor(vertex2);
		vertex2.addNeighbor(vertex1);
	}

	public void removeVerticesColor() {
		List<Vertex> vList = new ArrayList<Vertex>(vertices.values());
		for (Vertex v : vList) {
			v.removeColor();
		}
	}

	public boolean isProbedVertex(int id) {
		Vertex v = vertices.get(id);
		if (null == v) {
			return false;
		}
		return v.isProbed();
	}

	/* Getters and Setters */

	public HashMap<Integer, Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(HashMap<Integer, Vertex> vertices) {
		this.vertices = vertices;
	}

	public HashMap<Edge, Integer> getEdges() {
		return edges;
	}

	public void setEdges(HashMap<Edge, Integer> edges) {
		this.edges = edges;
	}

	public int getMaxNumOfVertices() {
		return maxNumOfVertices;
	}

	public void setMaxNumOfVertices(int maxNumOfVertexs) {
		this.maxNumOfVertices = maxNumOfVertexs;
	}

	public int getMaxNumOfEdges() {
		return maxNumOfEdges;
	}

	public void setMaxNumOfEdges(int maxNumOfEdges) {
		this.maxNumOfEdges = maxNumOfEdges;
	}
}
