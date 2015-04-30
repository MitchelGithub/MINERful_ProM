package org.processmining.newpackage.parameters;

import org.processmining.basicutils.parameters.impl.PluginParametersImpl;

public class MinerParameters extends PluginParametersImpl {
	
	private boolean noconflict;
	private boolean nofolding;
	private boolean noredundancy; //Unstable
	
	private double threshold;
	private double interest;
	private double confidence;
	
	private double incompleteThreshold;
	private double incompleteInterest;
	
	private String[] args;

	public MinerParameters(MinerParameters parameters) {
		//super(parameters);
		setNoConflict(parameters.isNoConflict());
		setThreshold(parameters.getThreshold());
		setInterest(parameters.getInterest());
	}
	
	public MinerParameters() {
		//super(parameters);
		setNoConflict(false);
		setNoFolding(false);
		setNoRedundancy(false);
		setThreshold(0.0);
		setInterest(0.0);
		setConfidence(0.0);
	}
	
	public boolean equals(Object object) {
		if (object instanceof MinerParameters) {
			MinerParameters parameters = (MinerParameters) object;
			/*
			return super.equals(parameters) &&
					isYourBoolean() == parameters.isYourBoolean() &&
					getYourInteger() == parameters.getYourInteger() &&
					getYourString().equals(parameters.getYourString());
			*/
			if (threshold == parameters.getThreshold() && interest == parameters.getInterest()) {
				if (incompleteThreshold == parameters.getIncompleteThreshold() && incompleteInterest == parameters.getIncompleteInterest()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void setNoConflict(boolean noConf) {
		this.noconflict = noConf;
	}

	public Boolean isNoConflict() {
		return noconflict;
	}
	
	public void setNoRedundancy(boolean noRed) {
		this.noredundancy = noRed;
	}

	public Boolean isNoRedundancy() {
		return noredundancy;
	}
	
	public void setNoFolding(boolean noFold) {
		this.nofolding = noFold;
	}

	public Boolean isNoFolding() {
		return nofolding;
	}
	
	public void setThreshold(double thresh) {
		this.threshold = thresh;
	}

	public double getThreshold() {
		return threshold;
	}
	
	public void setConfidence(double conf) {
		this.confidence = conf;
	}

	public double getConfidence() {
		return confidence;
	}
	
	public double getIncompleteThreshold() {
		return incompleteThreshold;
	}

	public void setIncompleteThreshold(double incompleteThresh) {
		this.incompleteThreshold = incompleteThresh;
	}
	
	public void setInterest(double inter) {
		this.interest = inter;
	}

	public double getInterest() {
		return interest;
	}

	public double getIncompleteInterest() {
		return incompleteInterest;
	}

	public void setIncompleteInterest(double incompleteInter) {
		this.incompleteInterest = incompleteInter;
	}
	
	public void writeArgs() {
		
		String C;
		String R;
		String noCF;
		
		if (isNoConflict() == true) {
			C = "-C";
		}
		else C = " ";
		
		if (isNoRedundancy() == true) {
			R = "-R";
		}
		else R = " ";
		
		if (isNoConflict() == true) {
			noCF = "-noCF";
		}
		else noCF = " ";

		this.args =  new String[] {"-iLF", "dummyInput.xes", "-condec", "dummyOutput.xml", "-i", String.valueOf( getInterest() ), "-t", String.valueOf( getThreshold()), "-c", String.valueOf( getConfidence()),  R, C, noCF };
		
	}
	
	public String[] getArgs() {
		return args;
	}
}
