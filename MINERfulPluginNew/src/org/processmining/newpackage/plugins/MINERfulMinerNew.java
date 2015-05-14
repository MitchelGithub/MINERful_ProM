package org.processmining.newpackage.plugins;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Vector;

import minerful.MinerFulLauncher;
import minerful.MinerFulMinerStarter;
import minerful.miner.params.MinerFulCmdParameters;
import minerful.params.InputCmdParameters;
import minerful.params.SystemCmdParameters;
import minerful.params.ViewCmdParameters;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.newpackage.dialogs.MINERfulDialog;
import org.processmining.newpackage.parameters.MinerParameters;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinitonCell;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintTemplate;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.DeclareModel;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;


/*
 * This plugin uses the MINERful algorithm to mine Declare models
 * The MINERful algorithm code used in this plugin is taken in consent from https://github.com/cdc08x/MINERful
 * 
 */

@Plugin(name = "MINERful Declare Miner", parameterLabels = { "Input event log" }, 
		    returnLabels = { "Declare model mined using MINERful miner" }, returnTypes = { org.processmining.plugins.declareminer.visualizing.DeclareMap.class }, userAccessible = true)
public class MINERfulMinerNew {
	/**
	 * The plug-in variant that runs in any context and requires a single event log and uses parameters
	 * 
	 * @param context The context to run in.
	 * @param input XES event log
	 * @return A declare model mined using the MINERful algorithm
	 * @throws Exception 
	 */
	@UITopiaVariant(affiliation = "TU/e", author = "M.H.M. Schouten", email = "m.h.m.schouten@student.tue.nl")
	@PluginVariant(variantLabel = "MINERful Declare Miner, dialog", requiredParameterLabels = {0 })
	public DeclareMap run(UIPluginContext context, XLog input) throws Exception { //, MinerParameters parameters
        context.getProgress().setMinimum(0);
        context.getProgress().setMaximum(input.size());

        MINERfulDialog dialog = new MINERfulDialog(input);
        InteractionResult result = context.showWizard("Mine using MINERful Miner", true, true, dialog);
        
        MinerParameters params =  dialog.getMinerParameters();
        params.writeArgs();
		String[] args = params.getArgs();
			
		//Create a MINERful starter and run main method
		MinerFulMinerStarter minerMinaStarter = new MinerFulMinerStarter();
		minerMinaStarter.main(args, input);
		
		InputCmdParameters inputParams = minerMinaStarter.inputParams2;
		MinerFulCmdParameters minerFulParams = minerMinaStarter.minerFulParams2;
		ViewCmdParameters viewParams = minerMinaStarter.viewParams2;
		SystemCmdParameters systemParams = minerMinaStarter.systemParams2;
		
		//Create a MINERful launcher	
		MinerFulLauncher minerLaunch = new MinerFulLauncher(inputParams, minerFulParams, viewParams, systemParams);
		
        context.getProgress().inc();
        
        //Get DeclareMap produced by the MINERful algorithm
        DeclareMap decModel = minerLaunch.mine(input);
   
        //Apply updateVisualization method to correct visualization
        DeclareMap output = updateVisualization( new DeclareModel( decModel.getModel(), decModel.getView() ) );	
        
        return output;
	}
	

	
       public DeclareMap updateVisualization(final DeclareModel models) {
                final AssignmentModel model = new AssignmentModel(models.getModel().getLanguage());
                model.setName("Declare model mined using MINERful miner");
            
                final Vector ads = new Vector();
                ActivityDefinition activitydefinition = null;
                        int k = 0;
                                for (final ActivityDefinition ad : models.getModel().getActivityDefinitions()) {
                                        if (!ads.contains(ad.getName())) {
                                                activitydefinition = model.addActivityDefinition(k + 1);
                                                activitydefinition.setName(ad.getName());
                                                final boolean c = ads.add(ad.getName());
                                                k++;
                                        }
                                }
                        
                        new Vector();
                        int l = 1;
                                for (final ConstraintDefinition cd : models.getModel().getConstraintDefinitions()) {
                                        final ConstraintTemplate ct = new ConstraintTemplate(l, cd);
                                        final ConstraintDefinition toAdd = new ConstraintDefinition(l, model, ct); 

                                        final Collection<Parameter> parameters = ct.getParameters();
                                        int h = 0;
                                        for (final Parameter parameter : parameters) {
                                                for (final ActivityDefinition branch : cd.getBranches(parameter)) {
                                                        toAdd.addBranch(parameter, branch);
                                                        h++;
                                                }
                                        }
//Following code removes the not chain succession relation                                       
                                        String x = cd.getCaption();
                                        boolean c;
                                        
/*
                                        if (x.contains("not chain succession")) {
                                        	//models.getModel().deleteConstraintDefinition(cd);
                                        	 c = false;
                                        	System.out.println("Deleted: " + x);
                                        }
                                        else */ c = model.addConstraintDefiniton(toAdd);
//Previous code removes the not chain succession relation                                        
                                        if (!c) {
                                                c = model.addConstraintDefiniton(new ConstraintDefinition(l, models.getModel(), cd));
                                        }
                                        l++;
                                }  
                                
                //Required for visualization   
                AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker("dummy");
                broker.addAssignmentAndView(model, new AssignmentModelView(model));
        		org.processmining.plugins.declare.visualizing.AssignmentViewBroker brokerCh = org.processmining.plugins.declare.visualizing.XMLBrokerFactory.newAssignmentBroker("dummy");
        		org.processmining.plugins.declare.visualizing.AssignmentModel modelCh = brokerCh.readAssignment();
        		org.processmining.plugins.declare.visualizing.AssignmentModelView viewCh = new org.processmining.plugins.declare.visualizing.AssignmentModelView(modelCh);
        		DeclareMap decModel = new DeclareMap(model, modelCh, new AssignmentModelView(model),viewCh, broker, brokerCh);
                
        		//Draw the layout for the Declare model
                decModel = layout(models.getView(), models.getModel());
                //ProM return blank output without this
                viewCh = layoutCh(viewCh, modelCh);
                DeclareMap decModel2 = new DeclareMap(decModel.getModel(), null, decModel.getView() ,viewCh, null, null);
                
                return decModel2;
	        }
    
    /*
     *  Draws the Declare model in a circle
     *  Changes made here will also need to be changed in the layoutCh function
     */
   	public static DeclareMap layout(AssignmentModelView view, AssignmentModel model ){
		view.getGraph().doLayout();
		int i = 0;
		int noCells = view.activityDefinitionCells().size();
		double stepsize = 2 * Math.PI /noCells;
		double alpha = 0;
		double X = 0;
		double Y = 0;
		for (ActivityDefinitonCell cell : view.activityDefinitionCells()) {
			new Integer(i + 1);
			X = 500+300*Math.sin(alpha);
			Y = 200+100*Math.cos(alpha);
			String label = cell.getActivityDefinition().getName();
			cell.setSize(new Point2D.Double(5.*(label.length()+9), 30.0));
					// puts tasks in a line
					//cell.setPosition(new Point2D.Double(20. + (i * 180), 50 + (i * 80)));
			cell.setPosition(new Point2D.Double(X, Y));
			i++;
			alpha += stepsize;
		}
		return new DeclareMap(model, null, view, null, null, null);
	}
   	
   	/*
   	 * 		Duplicate of normal layout function, for some reason required for ProM 
   	 * 		Changes made to layout function should also be made here
   	 */
   	 
   	public static org.processmining.plugins.declare.visualizing.AssignmentModelView layoutCh(org.processmining.plugins.declare.visualizing.AssignmentModelView view, org.processmining.plugins.declare.visualizing.AssignmentModel model ){
		view.getGraph().doLayout();
		int i = 0;
		int noCells = view.activityDefinitionCells().size();
		double stepsize = 2 * Math.PI /noCells;
		double alpha = 0;
		double X = 0;
		double Y = 0;
		for (org.processmining.plugins.declare.visualizing.ActivityDefinitonCell cell : view.activityDefinitionCells()) {
			new Integer(i + 1);
			X = 500+300*Math.sin(alpha);
			Y = 200+100*Math.cos(alpha);
			String label = cell.getActivityDefinition().getName();
			cell.setSize(new Point2D.Double(5.*(label.length()+9), 30.0));
					// puts tasks in a line
					//cell.setPosition(new Point2D.Double(20. + (i * 180), 50 + (i * 80)));
			cell.setPosition(new Point2D.Double(X, Y));
			i++;
			alpha += stepsize;
		}
		return view;
	}
}
