package org.processmining.newpackage.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.model.XLog;
import org.processmining.newpackage.parameters.MinerParameters;

//import MINERfulPackage.parameters.MinerParameters;
import com.fluxicon.slickerbox.factory.SlickerFactory;

public class MINERfulDialog extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -60087716353524468L;
	private final ParametersWrapper p = new ParametersWrapper();
	
	private final JLabel thresholdLabel;
	private final JSlider thresholdSlider;
	private final JLabel thresholdValue;
	
	private final JLabel interestLabel;
	private final JSlider interestSlider;
	private final JLabel interestValue;
	
	private final JLabel confidenceLabel;
	private final JSlider confidenceSlider;
	private final JLabel confidenceValue;
/*	
	private final JLabel noconflictLabel;
	private final JCheckBox noconflictBox;
	private final JLabel noconflictValue;
	
	private final JLabel noredundancyLabel;
	private final JCheckBox noredundancyBox;
	private final JLabel noredundancyValue;
	
	private final JLabel nofoldingLabel;
	private final JCheckBox nofoldingBox;
	private final JLabel nofoldingValue;
*/	
	
	public class ParametersWrapper {
		public MinerParameters parameters;
	}


	/**
	 * The JPanel that allows the user to set (a subset of) the parameters.
	 */
	public MINERfulDialog(XLog input1 ) { //, final MinerParameters parameters UIPluginContext context, 
		
		p.parameters = new MinerParameters();
		SlickerFactory factory = SlickerFactory.instance();

		int gridy = 1;

		setLayout(new GridBagLayout());
		
		//-------------------------------------------------
		//------------ Threshold GUI
		//-------------------------------------------------
		
		thresholdLabel = factory.createLabel("Threshold");
		{
			GridBagConstraints cThresholdLabel = new GridBagConstraints();
			cThresholdLabel.gridx = 0;
			cThresholdLabel.gridy = gridy;
			cThresholdLabel.anchor = GridBagConstraints.WEST;
			add(thresholdLabel, cThresholdLabel);
		}

		thresholdSlider = factory.createSlider(SwingConstants.HORIZONTAL);
		{
			thresholdSlider.setMinimum(0);
			thresholdSlider.setMaximum(1000);
			thresholdSlider.setValue((int) (p.parameters.getThreshold() * 1000));
			GridBagConstraints cThresholdSlider = new GridBagConstraints();
			cThresholdSlider.gridx = 1;
			cThresholdSlider.gridy = gridy;
			cThresholdSlider.fill = GridBagConstraints.HORIZONTAL;
			add(thresholdSlider, cThresholdSlider);
		}

		thresholdValue = factory.createLabel(String.format("%.2f", p.parameters.getThreshold()));
		{
			GridBagConstraints cThresholdValue = new GridBagConstraints();
			cThresholdValue.gridx = 2;
			cThresholdValue.gridy = gridy;
			add(thresholdValue, cThresholdValue);
		}

		gridy++;
		
		final JLabel thresholdExplanation = factory.createLabel("Threshold for support (reliability), value ranging from 0.0 to 1.0");
		{
			GridBagConstraints cThresholdExplanation = new GridBagConstraints();
			cThresholdExplanation.gridx = 1;
			cThresholdExplanation.gridy = gridy;
			cThresholdExplanation.gridwidth = 3;
			cThresholdExplanation.anchor = GridBagConstraints.WEST;
			add(thresholdExplanation, cThresholdExplanation);
		}
		
		gridy++;
		
		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}
		
		gridy++;
		
		//-------------------------------------------------
		//------------ Interest GUI
		//-------------------------------------------------
		interestLabel = factory.createLabel("Interest");
		{
			GridBagConstraints cInterestLabel = new GridBagConstraints();
			cInterestLabel.gridx = 0;
			cInterestLabel.gridy = gridy;
			cInterestLabel.anchor = GridBagConstraints.WEST;
			add(interestLabel, cInterestLabel);
		}

		interestSlider = factory.createSlider(SwingConstants.HORIZONTAL);
		{
			interestSlider.setMinimum(0);
			interestSlider.setMaximum(1000);
			interestSlider.setValue((int) (p.parameters.getInterest() * 1000));
			GridBagConstraints cInterestSlider = new GridBagConstraints();
			cInterestSlider.gridx = 1;
			cInterestSlider.gridy = gridy;
			cInterestSlider.fill = GridBagConstraints.HORIZONTAL;
			add(interestSlider, cInterestSlider);
		}

		interestValue = factory.createLabel(String.format("%.2f", p.parameters.getInterest()));
		{
			GridBagConstraints cInterestValue = new GridBagConstraints();
			cInterestValue.gridx = 2;
			cInterestValue.gridy = gridy;
			add(interestValue, cInterestValue);
		}

		gridy++;
		
		final JLabel interestExplanation = factory.createLabel("Threshold for interest factor (relevance), value ranging from 0.0 to 1.0");
		{
			GridBagConstraints cinterestExplanation = new GridBagConstraints();
			cinterestExplanation.gridx = 1;
			cinterestExplanation.gridy = gridy;
			cinterestExplanation.gridwidth = 3;
			cinterestExplanation.anchor = GridBagConstraints.WEST;
			add(interestExplanation, cinterestExplanation);
		}
		
		gridy++;
		
		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}
		
		gridy++;
		
		//-------------------------------------------------
		//------------ Confidence GUI
		//-------------------------------------------------
		
		confidenceLabel = factory.createLabel("Confidence");
		{
			GridBagConstraints cConfidenceLabel = new GridBagConstraints();
			cConfidenceLabel.gridx = 0;
			cConfidenceLabel.gridy = gridy;
			cConfidenceLabel.anchor = GridBagConstraints.WEST;
			add(confidenceLabel, cConfidenceLabel);
		}

		confidenceSlider = factory.createSlider(SwingConstants.HORIZONTAL);
		{
			confidenceSlider.setMinimum(0);
			confidenceSlider.setMaximum(1000);
			confidenceSlider.setValue((int) (p.parameters.getConfidence() * 1000));
			GridBagConstraints cConfidenceSlider = new GridBagConstraints();
			cConfidenceSlider.gridx = 1;
			cConfidenceSlider.gridy = gridy;
			cConfidenceSlider.fill = GridBagConstraints.HORIZONTAL;
			add(confidenceSlider, cConfidenceSlider);
		}

		confidenceValue = factory.createLabel(String.format("%.2f", p.parameters.getConfidence()));
		{
			GridBagConstraints cConfidenceValue = new GridBagConstraints();
			cConfidenceValue.gridx = 2;
			cConfidenceValue.gridy = gridy;
			add(confidenceValue, cConfidenceValue);
		}

		gridy++;
		
		final JLabel confidenceExplanation = factory.createLabel("Threshold for confidence level (relevance), value ranging from 0.0 to 1.0");
		{
			GridBagConstraints cConfidenceExplanation = new GridBagConstraints();
			cConfidenceExplanation.gridx = 1;
			cConfidenceExplanation.gridy = gridy;
			cConfidenceExplanation.gridwidth = 3;
			cConfidenceExplanation.anchor = GridBagConstraints.WEST;
			add(confidenceExplanation, cConfidenceExplanation);
		}
		
		gridy++;
		
		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}
		
		gridy++;
		
		//-------------------------------------------------
		//------------ No Conflict GUI
		//-------------------------------------------------
/*		
		noconflictLabel = factory.createLabel("No Conflict");
		{
			GridBagConstraints cNoConflictLabel = new GridBagConstraints();
			cNoConflictLabel.gridx = 0;
			cNoConflictLabel.gridy = gridy;
			cNoConflictLabel.anchor = GridBagConstraints.WEST;
			add(noconflictLabel, cNoConflictLabel);
		}


		
		noconflictBox = factory.createCheckBox("      ", false); //"Select No Conflicts: Yes/No "
		{
			noconflictBox.setSelected(p.parameters.isNoConflict());
			GridBagConstraints cnoconflictbox = new GridBagConstraints();
			cnoconflictbox.gridx = 1;
			cnoconflictbox.gridy = gridy;
			cnoconflictbox.fill = GridBagConstraints.HORIZONTAL;
			add(noconflictBox, cnoconflictbox);
			
//			checkBox.setOpaque(false);
//			checkBox.setPreferredSize(new Dimension(100, 30));
//			add(checkBox, "0, 2");
		}
		

		noconflictValue = factory.createLabel(String.valueOf(p.parameters.isNoConflict()));
		{
			GridBagConstraints cNoConflictValue = new GridBagConstraints();
			cNoConflictValue.gridx = 2;
			cNoConflictValue.gridy = gridy;
			add(noconflictValue, cNoConflictValue);
		}
		
		gridy++;
		
		final JLabel noconflictExplanation = factory.createLabel("Avoid conflicts");
		{
			GridBagConstraints cnoconflictExplanation = new GridBagConstraints();
			cnoconflictExplanation.gridx = 1;
			cnoconflictExplanation.gridy = gridy;
			cnoconflictExplanation.gridwidth = 3;
			cnoconflictExplanation.anchor = GridBagConstraints.WEST;
			add(noconflictExplanation, cnoconflictExplanation);
		}
		
		gridy++;
		
		//spacer
				{
					JLabel spacer = factory.createLabel(" ");
					GridBagConstraints cSpacer = new GridBagConstraints();
					cSpacer.gridx = 0;
					cSpacer.gridy = gridy;
					cSpacer.anchor = GridBagConstraints.WEST;
					add(spacer, cSpacer);
				}
				
		gridy++;
*/		
		//-------------------------------------------------
		//------------ No Redundancy GUI
		//-------------------------------------------------
/*		
		noredundancyLabel = factory.createLabel("No Redundancy");
		{
			GridBagConstraints cNoRedundancyLabel = new GridBagConstraints();
			cNoRedundancyLabel.gridx = 0;
			cNoRedundancyLabel.gridy = gridy;
			cNoRedundancyLabel.anchor = GridBagConstraints.WEST;
			add(noredundancyLabel, cNoRedundancyLabel);
		}


		
		noredundancyBox = factory.createCheckBox("      ", false); //"Select No Conflicts: Yes/No "
		{
			noredundancyBox.setSelected(p.parameters.isNoRedundancy());
			GridBagConstraints cnoredundancybox = new GridBagConstraints();
			cnoredundancybox.gridx = 1;
			cnoredundancybox.gridy = gridy;
			cnoredundancybox.fill = GridBagConstraints.HORIZONTAL;
			add(noredundancyBox, cnoredundancybox);
			
//			checkBox.setOpaque(false);
//			checkBox.setPreferredSize(new Dimension(100, 30));
//			add(checkBox, "0, 2");
		}
		

		noredundancyValue = factory.createLabel(String.valueOf(p.parameters.isNoRedundancy()));
		{
			GridBagConstraints cNoRedundancyValue = new GridBagConstraints();
			cNoRedundancyValue.gridx = 2;
			cNoRedundancyValue.gridy = gridy;
			add(noredundancyValue, cNoRedundancyValue);
		}
		
		gridy++;
		
		final JLabel noredundancyExplanation = factory.createLabel("Avoid redundancy, WARNING: Unstable implementation!");
		{
			GridBagConstraints cnoredundancyExplanation = new GridBagConstraints();
			cnoredundancyExplanation.gridx = 1;
			cnoredundancyExplanation.gridy = gridy;
			cnoredundancyExplanation.gridwidth = 3;
			cnoredundancyExplanation.anchor = GridBagConstraints.WEST;
			add(noredundancyExplanation, cnoredundancyExplanation);
		}
		
		gridy++;
		
		//spacer
				{
					JLabel spacer = factory.createLabel(" ");
					GridBagConstraints cSpacer = new GridBagConstraints();
					cSpacer.gridx = 0;
					cSpacer.gridy = gridy;
					cSpacer.anchor = GridBagConstraints.WEST;
					add(spacer, cSpacer);
				}
				
		gridy++;
*/		
		//-------------------------------------------------
		//------------ No Folding GUI
		//-------------------------------------------------
/*		
		nofoldingLabel = factory.createLabel("No Folding");
		{
			GridBagConstraints cNoFoldingLabel = new GridBagConstraints();
			cNoFoldingLabel.gridx = 0;
			cNoFoldingLabel.gridy = gridy;
			cNoFoldingLabel.anchor = GridBagConstraints.WEST;
			add(nofoldingLabel, cNoFoldingLabel);
		}


		
		nofoldingBox = factory.createCheckBox("      ", false); //"Select No Conflicts: Yes/No "
		{
			nofoldingBox.setSelected(p.parameters.isNoFolding());
			GridBagConstraints cnofoldingbox = new GridBagConstraints();
			cnofoldingbox.gridx = 1;
			cnofoldingbox.gridy = gridy;
			cnofoldingbox.fill = GridBagConstraints.HORIZONTAL;
			add(nofoldingBox, cnofoldingbox);
			
//			checkBox.setOpaque(false);
//			checkBox.setPreferredSize(new Dimension(100, 30));
//			add(checkBox, "0, 2");
		}
		

		nofoldingValue = factory.createLabel(String.valueOf(p.parameters.isNoFolding()));
		{
			GridBagConstraints cNoFoldingValue = new GridBagConstraints();
			cNoFoldingValue.gridx = 2;
			cNoFoldingValue.gridy = gridy;
			add(nofoldingValue, cNoFoldingValue);
		}
		
		gridy++;
		
		final JLabel nofoldingExplanation = factory.createLabel("Avoid the discovered constraints to be folded within implying activities");
		{
			GridBagConstraints cnofoldingExplanation = new GridBagConstraints();
			cnofoldingExplanation.gridx = 1;
			cnofoldingExplanation.gridy = gridy;
			cnofoldingExplanation.gridwidth = 3;
			cnofoldingExplanation.anchor = GridBagConstraints.WEST;
			add(nofoldingExplanation, cnofoldingExplanation);
		}
		
		gridy++;
		
		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}
				
		gridy++;
*/		
		//----------------------------------------
		//--------- Change listeners
		//----------------------------------------
		
		thresholdSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setThreshold(/*(float)*/ (thresholdSlider.getValue() / 1000.0));
				thresholdValue.setText(String.format("%.2f", p.parameters.getThreshold()));
			}
		});
		
		interestSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setInterest(/*(float)*/ (interestSlider.getValue() / 1000.0));
				interestValue.setText(String.format("%.2f", p.parameters.getInterest()));
			}
		});
		
		confidenceSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setConfidence(/*(float)*/ (confidenceSlider.getValue() / 1000.0));
				confidenceValue.setText(String.format("%.2f", p.parameters.getConfidence()));
			}
		});
/*		
		noconflictBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setNoConflict(noconflictBox.isSelected());
				noconflictValue.setText(String.valueOf(p.parameters.isNoConflict()));
			}
		});
		
		nofoldingBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setNoFolding(nofoldingBox.isSelected());
				nofoldingValue.setText(String.valueOf(p.parameters.isNoFolding()));
			}
		});
		
		noredundancyBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setNoRedundancy(noredundancyBox.isSelected());
				noredundancyValue.setText(String.valueOf(p.parameters.isNoRedundancy()));
			}
		});
*/				
		
		
		//------------------------------------------------------
		//-------------- Original code
		//------------------------------------------------------
		
//		double size[][] = { { TableLayoutConstants.FILL }, { TableLayoutConstants.FILL, 30, 30 } };
//		setLayout(new TableLayout(size));
//		Set<String> values = new HashSet<String>();
//		values.add("Option 1");
//		values.add("Option 2");
//		values.add("Option 3");
//		values.add("Option 4");
//
//		DefaultListModel<String> listModel = new DefaultListModel<String>();
//		for (String value: values) {
//			listModel.addElement(value);
//		}
//		final ProMList<String> list = new ProMList<String>("Select option", listModel);
//		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		final String defaultValue = "Option 1";
//		list.setSelection(defaultValue);
		
		
//		list.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				List<String> selected = list.getSelectedValuesList();
//				if (selected.size() == 1) {
//					parameters.setYourString(selected.get(0));
//				} else {
//					/*
//					 * Nothing selected. Revert to selection of default classifier.
//					 */
//					list.setSelection(defaultValue);
//					parameters.setYourString(defaultValue);
//				}
//			}
//		});
		
//		list.setPreferredSize(new Dimension(100, 100));
//		add(list, "0, 0");
//		
//		final NiceSlider doubleSlider = SlickerFactory.instance().createNiceDoubleSlider("Select threshold value ", -10,
//				10, p.parameters.getThreshold(), Orientation.HORIZONTAL);
//		doubleSlider.addChangeListener(new ChangeListener() {
//
//			public void stateChanged(ChangeEvent e) {
//				p.parameters.setThreshold(doubleSlider.getSlider().getValue());
//			}
//		});
//		add(doubleSlider, "0, 1");
//
//		final JCheckBox checkBox = SlickerFactory.instance().createCheckBox("Select No Conflicts: Yes/No ", false);
//		checkBox.setSelected(p.parameters.isNoConflict());
//		checkBox.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				p.parameters.setNoConflict(checkBox.isSelected());
//			}
//
//		});
//		checkBox.setOpaque(false);
//		checkBox.setPreferredSize(new Dimension(100, 30));
//		add(checkBox, "0, 2");
	}
	
	public MinerParameters getMinerParameters() {
		return p.parameters;
	}
}
