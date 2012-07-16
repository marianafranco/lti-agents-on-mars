package model;

import env.Percept;
import model.graph.Vertex;

/**
 * Represents an opponent or a coworker.
 * 
 * @author mafranko
 */
public class Entity {

	private String name;
	private Vertex vertex;
	private String team = Percept.TEAM_UNKNOWN;
	private String status = Percept.STATUS_UNKNOWN;
	private String role = Percept.ROLE_UNKNOWN;

	private boolean inspected = false;
	private int energy;
	private int maxEnergy;
	private int health;
	private int maxHealth;
	private int strenght;
	private int visibility;

	public Entity(String name) {
		this.name = name;
		this.vertex = new Vertex(-1);
	}

	public Entity(String name, String team, Vertex vertex, String status) {
		this.name = name;
		this.team = team;
		this.vertex = vertex;
		this.status = status;
	}

	/**
	 * Updates the entity's internal values (energy, health, strengt and visibility).
	 * 
	 * @param entityEnergy the energy.
	 * @param entityMaxEnergy the max energy.
	 * @param entityHealth the health.
	 * @param entityMaxHealth the max health.
	 * @param entityStrenght the strenght.
	 * @param entityVisibility the visibility range.
	 */
	public void update(int entityEnergy, int entityMaxEnergy, int entityHealth,
			int entityMaxHealth, int entityStrenght, int entityVisibility) {
		this.energy = entityEnergy;
		if (energy == 0) {
			status = Percept.STATUS_DISABLED;
		} else {
			status = Percept.STATUS_NORMAL;
		}
		this.maxEnergy = entityMaxEnergy;
		this.health = entityHealth;
		this.maxHealth = entityMaxHealth;
		this.strenght = entityStrenght;
		this.visibility = entityVisibility;
	}

	/* Getters and Setters */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role.toLowerCase();
	}

	public boolean isInspected() {
		return inspected;
	}

	public void setInspected(boolean inspected) {
		this.inspected = inspected;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getStrenght() {
		return strenght;
	}

	public void setStrenght(int strenght) {
		this.strenght = strenght;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		if (object instanceof Entity) {
			Entity other = (Entity) object;
			if (this.name.equals(other.getName())) {
				return true;
			}
		}
		return false;
	}
}
