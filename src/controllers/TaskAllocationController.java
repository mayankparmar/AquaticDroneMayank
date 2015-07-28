package controllers;

import java.awt.Color;

import commoninterface.sensors.GeoFenceCISensor;
import mathutils.Vector2d;

import controllers.Controller;
import simulation.Simulator;
import simulation.robot.Robot;

import simulation.robot.CISensorWrapper;
import simulation.robot.DifferentialDriveRobot;

import simulation.util.Arguments;



public class TaskAllocationController extends Controller {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DifferentialDriveRobot r;
	private CISensorWrapper gfSensor, boundarySensor;
	
	private int numberOfRobots;
	private double width;
	private double height;
	private Simulator sim;
	
	public TaskAllocationController(Simulator simulator, Robot robot,
			Arguments args) {
		super(simulator, robot, args);
		// TODO Auto-generated constructor stub
		numberOfRobots = args.getArgumentAsIntOrSetDefault("numberofrobot", 1);
		width = args.getArgumentAsDoubleOrSetDefault("width", 45);
		height = args.getArgumentAsDoubleOrSetDefault("height", 45);
		r = (DifferentialDriveRobot)robot;
		sim = simulator;
	}

	public enum State{
		AREA_ALLOCATION, RECRUITING, EXPLORING, RECRUITED, CHANGE_ORIENTATION
	}
	
	private State currentState = State.AREA_ALLOCATION;	
	
	@Override
	public void controlStep(double time){
		
		
		gfSensor = (CISensorWrapper)robot.getSensorWithId(3);
		boundarySensor = (CISensorWrapper)robot.getSensorWithId(4);
		
		switch(currentState){
		
		case AREA_ALLOCATION:
			//explore();
			allocateArea();
			break;
			
		case CHANGE_ORIENTATION:
			randomOrientation();
		
		case EXPLORING:
			r.setBodyColor(Color.YELLOW);
			explore();
			break;
			
		case RECRUITING:
			break;
			
		case RECRUITED:
			break;
		}
		
	}
	
	private void allocateArea() {
		// TODO Auto-generated method stub
		
		r.setWheelSpeed(0.3, 0.3);
		
		if(gfSensor.getSensorReading(0) > 0.5) {
			r.setOrientation(sim.getRandom().nextDouble() * Math.PI * 2);
		}
		
		
		
		// for experiment commenting bottom part and using upper code
		
//		double areaToMonitor = (width*height)/numberOfRobots;
//		
//		System.out.println(areaToMonitor);
//		boolean flag = false;
//		r.setWheelSpeed(0.3, 0.3);
//		if(flag == false && r.getPosition().x > (width/2) || r.getPosition().x < -(width/2) || r.getPosition().y > (height/2) || r.getPosition().y < -(height/2)){
//			currentState = State.CHANGE_ORIENTATION;
//		}
	}
	
	private void randomOrientation(){
		r.setOrientation(sim.getRandom().nextDouble()*Math.PI*2);
		currentState = State.AREA_ALLOCATION;
	}

	private void explore(){
		Vector2d currentPosition;
		r.setWheelSpeed(0.5, 0.5);
		currentPosition = r.getPosition();
		System.out.println(currentPosition.x);
	}

}
