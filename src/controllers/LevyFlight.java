package controllers;

import java.util.Random;

import org.apache.commons.math3.distribution.LevyDistribution;

import logger.DataLogger;
import simulation.Simulator;
import simulation.robot.CISensorWrapper;
import simulation.robot.DifferentialDriveRobot;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class LevyFlight extends Controller{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/****** Variables ******/
	private CISensorWrapper borderSensor, insideSensor;
	private DataLogger logger;
	private LevyDistribution levy;
	private DifferentialDriveRobot robo;
	private double stepSize;
	private int minimumStep = 100;
	private Random rand;
	private String log;
	public enum State{
		SET_STEP, STEP, CHANGE_ORIENTATION
	}
	private State currentState = State.SET_STEP;
	private String file; 
	private int wide;
	private String classname = "LevyFlight";
	/***********************/
	
	
	/****** Constructor ******/
	public LevyFlight(Simulator simulator, Robot robot, Arguments args) {
		super(simulator, robot, args);
		// TODO Auto-generated constructor stub
		
		borderSensor = (CISensorWrapper) robot.getSensorWithId(3);
		insideSensor = (CISensorWrapper) robot.getSensorWithId(1);
		logger = new DataLogger();
		robo = (DifferentialDriveRobot)robot;
		levy = new LevyDistribution(0, 0.276);
		rand = new Random();
		file = args.getArgumentAsStringOrSetDefault("file", "no");
				
//		wide = args.getArgumentAsIntOrSetDefault("environment", 0);
	}
	

	/************************/
	
	/****** Control Steps ******/
	public void controlStep(double time){
			
		switch (currentState){
		case SET_STEP:
			setStep(time);
			break;
			
		case STEP:
			levyWalk(time);
			break;
			
		case CHANGE_ORIENTATION:
			orient();
		}
	
	}
	
	private void orient() {
		// TODO Auto-generated method stub
		
		robot.setOrientation(Math.PI * 2 * rand.nextDouble());
		currentState = State.SET_STEP;
		
	}

	private void levyWalk(double clock) {
		// TODO Auto-generated method stub
		
		log = Integer.toString(robot.getId()) + "," + Double.toString(robot.getPosition().x) + "," + Double.toString(robot.getPosition().y) + "," + Double.toString(stepSize);
		logger.loggerInit(file);
		logger.logStart(log);
		
		robo.setWheelSpeed(0.2, 0.2);
		
		if(insideSensor.getSensorReading(0) > 0.5) {
			robo.setOrientation(Math.PI * 2 * rand.nextDouble());
		}
		
		if (clock > stepSize || borderSensor.getSensorReading(0) > 0.5) {
			currentState = State.CHANGE_ORIENTATION;
		}
		
	}

	private void setStep(double clock) {
		// TODO Auto-generated method stub
		
//		System.out.println(levy.sample());
		stepSize = clock + Math.abs((int) levy.sample() * minimumStep + minimumStep);
//		System.out.println(stepSize);
		currentState = State.STEP;
		
	}
	

}
