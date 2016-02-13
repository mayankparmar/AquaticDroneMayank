package environment;

import java.util.Random;

import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class voronoiEnvironment extends OpenEnvironment {
	
	protected PolygonSimple outerBoundary;
	protected PowerDiagram voronoiDiag;
	protected OpenList sites;
	public PolygonSimple rootPolygon;
	
	public voronoiEnvironment(Simulator simulator, Arguments args){
		super (simulator, args);
		
	}
	
	public void setup (Simulator simulator) {
		super.setup(simulator);
		
		// Creating an outer  boundary to limit the voronoi partitioning
		rootPolygon = new PolygonSimple();
		
		rootPolygon.add(-width/2, -height/2);
		rootPolygon.add(width/2, -height/2);
		rootPolygon.add(width/2, height/2);
		rootPolygon.add(-width/2, height/2);
		
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
