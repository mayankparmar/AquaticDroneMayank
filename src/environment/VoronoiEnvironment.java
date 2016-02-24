package environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Random;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.python.util.PythonInterpreter;

import commoninterface.AquaticDroneCI;
import commoninterface.entities.GeoFence;
import commoninterface.entities.Waypoint;
import commoninterface.utils.CoordinateUtilities;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

import simulation.Simulator;
import simulation.robot.AquaticDrone;
import simulation.robot.Robot;
import simulation.util.Arguments;


public class VoronoiEnvironment extends OpenEnvironment {
	
	protected PolygonSimple outerBoundary;
	protected PowerDiagram voronoiDiag;
	protected OpenList sites;
	protected GeoFence fence;
	public PolygonSimple rootPolygon;
	protected double wallDistance = 5;
	protected double width, height;
	Random rand;
	protected int numberRobots;
	
	public VoronoiEnvironment(Simulator simulator, Arguments args){
		super (simulator, args);
		rand = simulator.getRandom();
		wallDistance = args.getArgumentAsDoubleOrSetDefault("wallDistance", wallDistance);		
	}
	
	public void setup (Simulator simulator) {
		super.setup(simulator);
		
		width = simulator.getEnvironment().getWidth();
		height = simulator.getEnvironment().getHeight();
		numberRobots = simulator.getRobots().size();
		
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
		
		/*StringWriter writer = new StringWriter();
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptContext context = new SimpleScriptContext();
		
		context.setWriter(writer);
		ScriptEngine engine = manager.getEngineByName("python");
		try{
			engine.eval(new FileReader("levyDistribution.py"), context);
		}catch(ScriptException | IOException e){
			e.printStackTrace();
		}
		
		System.out.println(writer.toString());*/
		
		try
		{
			PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);
			PythonInterpreter interp = new PythonInterpreter();
			interp.execfile("foo.py");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
/*		try
		{
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("python levyDistribution.py");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			try{			
				p.waitFor();
			}catch(InterruptedException i){
				i.printStackTrace();
			}
			String line = "";
			while (br.ready())
				System.out.println(br.readLine());
		}catch(IOException e){
			e.printStackTrace();
		}*/
		
		
		/*try{
			Process P = Runtime.getRuntime().exec("python levyDistribution.py");
			//OutputStream out = P.getOutputStream();
			String s = null;
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(P.getInputStream()));
			System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println();
            }
			//System.out.println(P.getOutputStream());
		} catch (IOException e){
			e.printStackTrace();
		}*/
		
		
		/****** tesselation*/
		
		// Setting up Voronoi here and not in update because here voronois will be set up only once
		voronoiDiag = new PowerDiagram();
		sites = new OpenList();
		
		for(int i = 0; i < numberRobots; i++) {
			
			// width/2 and height/2 is because the axis meet at the centre of the environment
			//
			Site site = new Site((width/2)*rand.nextDouble(), (height/2)*rand.nextDouble());
			sites.add(site);
		}
		
		/* Commented because this is making partition to change as and when the robots move which we don't want.. at least for now (or maybe forever :p)
		 * 
		for (Robot r : getRobots()) {
			Site site = new Site (r.getPosition().x, r.getPosition().y);
			sites.add(site);
		}*/
		
		voronoiDiag.setSites(sites);
		voronoiDiag.setClipPoly(rootPolygon);
		
		voronoiDiag.computeDiagram();
		
		/*for (int i = 0; i < numberRobots; i++) {
			Site s = sites.array[i];
			System.out.println(i + "\t" + s.getPoint() + "\n");			
		}*/
		
		//voronoiDiag.getDetails();
		
		
		//
		
		Site[] array = sites.array;
		int size = sites.size;
		
//		System.out.println(array.length);
		
		
			
			AquaticDrone r = (AquaticDrone)getRobots().get(0);
			r.setPosition(0.20, -10.26);
			getRobots().get(1).setPosition(30.844, 14.473);
			getRobots().get(2).setPosition(-37.5, -37.5);
		
		
		for (int temp = 0; temp < size; temp++) {
			Site s = array[temp];
			//System.out.println("Sites.array: " + s);
			
			PolygonSimple poly = s.getPolygon();
			if(poly != null) {
				System.out.println("Polygon " + temp + ": " + poly.getInnerPoint());
			}
		}
		
				
				
		//
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
		
		/*voronoiDiag = new PowerDiagram();

		// normal list based on an array
		sites = new OpenList();

	    for (Robot r : getRobots()) {
	        Site site = new Site(r.getPosition().x, r.getPosition().y);
	        // we could also set a different weighting to some sites
	        // site.setWeight(30)
	        sites.add(site);
	    }

		// set the list of points (sites), necessary for the power diagram
		voronoiDiag.setSites(sites);
		// set the clipping polygon, which limits the power voronoi diagram
		voronoiDiag.setClipPoly(rootPolygon);

		// do the computation
		voronoiDiag.computeDiagram();   */
		
	}
	
	public OpenList getSites() {
		return (sites);
	}

}
