package distribution;

import java.util.List;
import java.util.LinkedList;


public class randomLevyNormal{
	
	protected static List<Double> sampleSpace = new LinkedList<>();
	protected static List<Double> levyDist = new LinkedList<>();
	
	public static void main(){
		
		range(0.1, 100, 0.1);
		double mu = 0;
		double sigma = 0.27674;
		createLevyDist(mu, sigma);
		showList(levyDist);
	}
	
	private static void showList(List<Double> levyDist2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < levyDist.size(); i++) {
			System.out.println(levyDist.get(i));
		}
	}

	private static void range(double min, double max, double diff) {
		// TODO Auto-generated method stub
		for (double i = min; i < max; i+=diff) {
			sampleSpace.add(i);
		}
	}

	private static void createLevyDist(double mu, double sigma) {
		// TODO Auto-generated method stub
		
		int sampleSize = sampleSpace.size();
		double expression1, expression2, expression3, expression4;
		
		for(int i = 0; i < sampleSize; i++) {
			
			double sample = sampleSpace.get(i);
			if (sample > mu){
				expression1 = Math.exp(-sigma / (2 * (sample - mu)));
				expression2 = Math.pow((sigma / (sample - mu)), 1.5);
				expression3 = Math.sqrt(2 * Math.PI * (Math.pow(sigma, 2)));
				expression4 = (expression1 * expression2) / expression3;
				levyDist.add(expression4);
			}
			else{
				levyDist.add(0.0);
			}
		}
		
	}

}