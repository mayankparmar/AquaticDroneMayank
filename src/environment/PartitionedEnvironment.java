package environment;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import mathutils.Vector2d;
import commoninterface.AquaticDroneCI;
import commoninterface.entities.GeoFence;
import commoninterface.entities.Waypoint;
import commoninterface.utils.CoordinateUtilities;
import simulation.Simulator;
import simulation.physicalobjects.Line;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.PhysicalObjectType;
import simulation.robot.AquaticDrone;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

public class PartitionedEnvironment extends OpenEnvironment {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ArgumentsAnnotation(name = "wallsdistance", defaultValue = "5")
    protected double wallsDistance = 5;
    @ArgumentsAnnotation(name = "random", defaultValue = "0.5")
    protected double rand = 0.5;
    protected boolean placeOutside = false;
    protected int numberOfRobots;
    protected double width, height; 
    
    protected GeoFence fence1, fence2, fence3 , fence4, fence5, environmentBoundary;
    

    public PartitionedEnvironment(Simulator simulator, Arguments args) {
        super(simulator, args);
        rand = args.getArgumentAsDoubleOrSetDefault("random", rand);
        wallsDistance = args.getArgumentAsDoubleOrSetDefault("wallsdistance", wallsDistance);
        placeOutside = args.getFlagIsTrue("placeoutside");
        width = args.getArgumentAsDouble("width");
        height = args.getArgumentAsDouble("height");
    }

    @Override
    public void setup(Simulator simulator) {
    	
    	super.setup(simulator);	//Sets the position of the drones randomly
    	numberOfRobots = simulator.getRobots().size();
    	
    	positionDrones(simulator);
//    	findRandomPoint(simulator, numberOfRobots);
    	drawGeoFence(simulator);
    }

private void drawGeoFence(Simulator simulator) {
		// TODO Auto-generated method stub
		Double maxX = width/2;
		Double maxY = height/2;
		Double minX = -maxX;
		Double minY = -maxY;
		
		fence1 = new GeoFence("fence1");
        fence2 = new GeoFence("fence2");
        fence3 = new GeoFence("fence3");
        fence4 = new GeoFence("fence4");
        fence5 = new GeoFence("fence5");
        environmentBoundary = new GeoFence("environmentBoundary");
		
		fence1.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX, minY)));
		fence1.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+15, minY)));
		fence1.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+15, maxY)));
		fence1.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX, maxY)));
		addLines(fence1.getWaypoints(), simulator);
		
		fence2.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+15, minY)));
		fence2.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+30, minY)));
		fence2.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+30, maxY)));
		fence2.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+15, maxY)));
		addLines(fence2.getWaypoints(), simulator);
		
		fence3.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+30, minY)));
		fence3.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+45, minY)));
		fence3.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+45, maxY)));
		fence3.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+30, maxY)));
		addLines(fence3.getWaypoints(), simulator);
		
		fence4.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+45, minY)));
		fence4.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+60, minY)));
		fence4.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+60, maxY)));
		fence4.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+45, maxY)));
//		addLines(fence4.getWaypoints(), simulator);
		
		fence5.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+60, minY)));
		fence5.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+75, minY)));
		fence5.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+75, maxY)));
		fence5.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX+60, maxY)));
//		addLines(fence5.getWaypoints(), simulator);
		
		environmentBoundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX, minY)));
		environmentBoundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(maxX, minY)));
		environmentBoundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(maxX, maxY)));
		environmentBoundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(minX, maxY)));
//		addBoundaryLines(environmentBoundary.getWaypoints(), simulator);
		
		
		for (Robot r : robots){
			AquaticDroneCI drone = (AquaticDroneCI) r;
			if(r.getId() == 0){
				drone.getEntities().add(fence1);
//				drone.getEntities().add(environmentBoundary);
			}
			if(r.getId() == 1){
				drone.getEntities().add(fence2);
//				drone.getEntities().add(environmentBoundary);
			}
			if(r.getId() == 2){
				drone.getEntities().add(fence3);
//				drone.getEntities().add(environmentBoundary);
			}
			if(r.getId() == 3){
//				drone.getEntities().add(fence4);
//				drone.getEntities().add(environmentBoundary);
			}
			if(r.getId() == 4){
//				drone.getEntities().add(fence5);
//				drone.getEntities().add(environmentBoundary);
			}
		}
	}

private void addBoundaryLines(LinkedList<Waypoint> waypoints, Simulator simulator) {
	// TODO Auto-generated method stub

    for (int i = 1; i < waypoints.size(); i++) {

        Waypoint wa = waypoints.get(i - 1);
        Waypoint wb = waypoints.get(i);
        commoninterface.mathutils.Vector2d va = CoordinateUtilities.GPSToCartesian(wa.getLatLon());
        commoninterface.mathutils.Vector2d vb = CoordinateUtilities.GPSToCartesian(wb.getLatLon());

        simulation.physicalobjects.Line l = new simulation.physicalobjects.Line(simulator, "line" + i, va.getX(), va.getY(), vb.getX(), vb.getY(), Color.BLUE);
        l.setColor(Color.GREEN);
        addObject(l);
        
    }

    Waypoint wa = waypoints.get(waypoints.size() - 1);
    Waypoint wb = waypoints.get(0);
    commoninterface.mathutils.Vector2d va = CoordinateUtilities.GPSToCartesian(wa.getLatLon());
    commoninterface.mathutils.Vector2d vb = CoordinateUtilities.GPSToCartesian(wb.getLatLon());

    simulation.physicalobjects.Line l = new simulation.physicalobjects.Line(simulator, "line0", va.getX(), va.getY(), vb.getX(), vb.getY(), Color.BLUE);
    l.setColor(Color.GREEN);
    addObject(l);

}

private void findRandomPoint(Simulator simulator, int points) {
		// TODO Auto-generated method stub
		
		double rangeMin = -37;
		double rangeMax = 37;
		
		ArrayList<Object> X = new ArrayList<>();
		ArrayList<Object> Y = new ArrayList<>();
		
		for (int i = 0; i < points; i ++) {
			double x = rangeMin + (rangeMax - rangeMin) * simulator.getRandom().nextDouble();
			double y = rangeMin + (rangeMax - rangeMin) * simulator.getRandom().nextDouble();
			
			X.add(x);
			Y.add(y);
		}
		
		System.out.println(X + "&" +Y);		
	}

private void positionDrones(Simulator simulator) {
		// TODO Auto-generated method stub
		for (Robot r : robots) {
			AquaticDrone drone = (AquaticDrone) r;
			drone.setPosition(15, 15);
			drone.setOrientation(0);
			    		
			double rangeMin = -37;
			double rangeMax = -33;
			double x = rangeMin + (rangeMax - rangeMin) * simulator.getRandom().nextDouble();
			double y = rangeMin + (rangeMax - rangeMin) * simulator.getRandom().nextDouble();
			drone.setPosition(x, y);            
	//        System.out.println(x + " & " +y);            
	        drone.setOrientation(simulator.getRandom().nextDouble() * Math.PI * 2);
		}
	}

//    @Override
//    protected boolean safe(Robot r, Simulator simulator) {
//    	return super.safe(r, simulator) &&
//                (placeOutside || insideLines(new Vector2d(r.getPosition().x, r.getPosition().y), simulator));
//    }

    protected void addLines(LinkedList<Waypoint> waypoints, Simulator simulator) {

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

    protected void addNode(GeoFence fence, double x, double y, Random r) {

        x *= wallsDistance;
        y *= wallsDistance;

        if (rand > 0) {
            x += r.nextDouble() * rand * wallsDistance * 2 - rand * wallsDistance;
            y += r.nextDouble() * rand * wallsDistance * 2 - rand * wallsDistance;
        }

        fence.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(x, y)));
    }

    public boolean insideLines(Vector2d v, Simulator sim) {
        //http://en.wikipedia.org/wiki/Point_in_polygon
        int count = 0;
        for (PhysicalObject p : sim.getEnvironment().getAllObjects()) {
            if (p.getType() == PhysicalObjectType.LINE) {
                Line l = (Line) p;
                if (l.intersectsWithLineSegment(v, new Vector2d(0, -Integer.MAX_VALUE)) != null) {
                    count++;
                }
            }
        }
        return count % 2 != 0;
    }

    @Override
    public void update(double time) {

    }

}
