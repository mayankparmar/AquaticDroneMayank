package environment;

import java.util.LinkedList;
import java.util.Random;

import commoninterface.AquaticDroneCI;
import commoninterface.entities.GeoFence;
import commoninterface.entities.Waypoint;
import commoninterface.utils.CoordinateUtilities;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class VoronoiEnvironment extends OpenEnvironment {
	
	protected PolygonSimple outerBoundary;
	protected PowerDiagram voronoiDiag;
	protected OpenList sites;
	protected GeoFence fence;
	public PolygonSimple rootPolygon;
	protected double wallDistance = 5;
	
	public VoronoiEnvironment(Simulator simulator, Arguments args){
		super (simulator, args);
		
		wallDistance = args.getArgumentAsDoubleOrSetDefault("wallDistance", wallDistance);
		
	}
	
	public void setup (Simulator simulator) {
		super.setup(simulator);
		
		// Creating an outer  boundary to limit the voronoi partitioning
		rootPolygon = new PolygonSimple();
		
		rootPolygon.add(-width/2, -height/2);
		rootPolygon.add(width/2, -height/2);
		rootPolygon.add(width/2, height/2);
		rootPolygon.add(-width/2, height/2);
		
		fence = new GeoFence("fence");
		addNode(fence, -width/2, -height/2, simulator.getRandom());
		addNode(fence, width/2, -height/2, simulator.getRandom());
		addNode(fence, width/2, height/2, simulator.getRandom());
		addNode(fence, -width/2, height/2, simulator.getRandom());
		
		addLines(fence.getWaypoints(), simulator);
		
		for (Robot r : robots){
			AquaticDroneCI drone = (AquaticDroneCI) r;
			drone.getEntities().add(fence);
		}
		
	}
	
	private void addLines(LinkedList<Waypoint> waypoints, Simulator simulator) {
		// TODO Auto-generated method stub
		for (int i = 1; i < waypoints.size(); i++) {

	        Waypoint wa = waypoints.get(i - 1);
	        Waypoint wb = waypoints.get(i);
	        commoninterface.mathutils.Vector2d va = CoordinateUtilities.GPSToCartesian(wa.getLatLon());
	        commoninterface.mathutils.Vector2d vb = CoordinateUtilities.GPSToCartesian(wb.getLatLon());
	
	        simulation.physicalobjects.Line l = new simulation.physicalobjects.Line(simulator, "line" + i, va.getX(), va.getY(), vb.getX(), vb.getY());
	        addObject(l);
        }

        Waypoint wa = waypoints.get(waypoints.size() - 1);
        Waypoint wb = waypoints.get(0);
        commoninterface.mathutils.Vector2d va = CoordinateUtilities.GPSToCartesian(wa.getLatLon());
        commoninterface.mathutils.Vector2d vb = CoordinateUtilities.GPSToCartesian(wb.getLatLon());

        simulation.physicalobjects.Line l = new simulation.physicalobjects.Line(simulator, "line0", va.getX(), va.getY(), vb.getX(), vb.getY());
        addObject(l);
	}

	private void addNode(GeoFence fence, double x, double y, Random r) {
		// TODO Auto-generated method stub
		
		x *= wallDistance;
		y *= wallDistance;
		double rand = 0.5;

        if (rand > 0) {
            x += r.nextDouble() * rand * wallDistance * 2 - rand * wallDistance;
            y += r.nextDouble() * rand * wallDistance * 2 - rand * wallDistance;
        }

        fence.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(x, y)));
		
	}

	public void update(double time){
		
		voronoiDiag = new PowerDiagram();
		sites = new OpenList();
		
		for (Robot r : getRobots()) {
			Site site = new Site (r.getPosition().x, r.getPosition().y);
			sites.add(site);
		}
		
		voronoiDiag.setSites(sites);
		voronoiDiag.setClipPoly(rootPolygon);
		
		voronoiDiag.computeDiagram();
		
	}
	
	public OpenList getSites() {
		return (sites);
	}

}
