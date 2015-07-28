package entities;

import java.util.ArrayList;
import java.util.LinkedList;
import commoninterface.entities.Entity;
import commoninterface.entities.Waypoint;
import commoninterface.AquaticDroneCI;
import commoninterface.utils.jcoord.LatLon;
import commoninterface.utils.logger.EntityManipulation;
import commoninterface.utils.logger.LogCodex;
import commoninterface.utils.logger.LogCodex.LogType;

public class EnvironmentBoundary extends Entity {
	
	private LinkedList<Waypoint> waypoints;
	
	public EnvironmentBoundary(String name, LinkedList<Waypoint> waypoints) {
		super(name);
		this.waypoints = waypoints;
	}
	
	public EnvironmentBoundary(String name) {
		super(name);
		waypoints = new LinkedList<Waypoint>();
	}
	
	public void addWaypoint(Waypoint wp) {
		waypoints.add(wp);
	}
	
	public void addWaypoint(LatLon latLon) {
		Waypoint wp = new Waypoint("geofence_wp_"+waypoints.size(), latLon);
		waypoints.add(wp);
	}
	
	public void clear() {
		waypoints.clear();
	}
	
	public LinkedList<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public static ArrayList<EnvironmentBoundary> getWaypoints(AquaticDroneCI drone) {
		ArrayList<EnvironmentBoundary> geoFences = new ArrayList<EnvironmentBoundary>();
		
		for(Entity e : drone.getEntities()) {
			if(e instanceof EnvironmentBoundary)
				geoFences.add((EnvironmentBoundary)e);
		}
		
		return geoFences;
	}

	public String getLogMessage() {
		ArrayList<Entity> entities = new ArrayList<Entity>(getWaypoints());
		
		return LogCodex.encodeLog(LogType.ENTITIES,
				new EntityManipulation(
						EntityManipulation.Operation.ADD, entities,
						this.getClass().getSimpleName()));
	}
	
	@Override
	public String toString() {
		String s = this.getClass().getSimpleName()+";";
		for(Waypoint w : getWaypoints())
			s+= "FENCEWP;"+w.getLatLon().getLat()+";"+w.getLatLon().getLon()+";";
		return s;
	}
	
}