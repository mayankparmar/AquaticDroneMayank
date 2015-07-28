package main;

import gui.CombinedGui;


public class StartPoint {
	
	public static void main(String[] args) {
		try {
			new CombinedGui(new String[]{"--gui","classname=CIResultViewerGui,renderer=(classname=EnvironmentRenderer))"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
