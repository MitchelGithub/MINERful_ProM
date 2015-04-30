package org.processmining.newpackage.plugins;

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
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintTemplate;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.DeclareModel;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

//@Plugin(name = "Your plug-in name", parameterLabels = { "Name of your first input", "Name of your second input", "Name of your parameters" }, 
//	    returnLabels = { "Name of your output" }, returnTypes = { YourOutput.class })
//public class YourPlugin extends YourAlgorithm {

@Plugin(name = "MINERful Declare Miner", parameterLabels = { "Input event log" }, 
		    returnLabels = { "Declare model mined using MINERful miner" }, returnTypes = { org.processmining.plugins.declareminer.visualizing.DeclareMap.class }, userAccessible = true)
public class MINERfulMinerNew {
	
	/**
	 * The plug-in variant that runs in any context and requires a single event log
	 * 
	 * @param context The context to run in.
	 * @param input XES event log
	 * @return A declare model mined using the MINERful algorithm
	 */


	/**
	 * The plug-in variant that runs in any context and requires a single event log and uses parameters
	 * 
	 * @param context The context to run in.
	 * @param input XES event log
	 * @return A declare model mined using the MINERful algorithm
	 */
	@UITopiaVariant(affiliation = "TU/e", author = "M.H.M. Schouten", email = "m.h.m.schouten@student.tue.nl")
	@PluginVariant(variantLabel = "MINERful Declare Miner, dialog", requiredParameterLabels = {0 })
	public DeclareMap run(UIPluginContext context, XLog input) { //, MinerParameters parameters
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
        
        //Apply export method to correct visualization
        DeclareMap output = export( new DeclareModel( decModel.getModel(), decModel.getView() ) );	
		
        //DeclareMap decModel = new DeclareMap(model, modelCh, view,viewCh, broker, brokerCh);
		System.out.println("No. Constraints: "+ decModel.getModel().constraintDefinitionsCount()+"-- No. Activities: "+ decModel.getModel().activityDefinitionsCount());
        return output;
	}
	
	/*
	if (result != InteractionResult.FINISHED) {

	}
    */ //nodig?

       public DeclareMap export(final DeclareModel models) {
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
                                        

                                        if (x.contains("not chain succession")) {
                                        	//models.getModel().deleteConstraintDefinition(cd);
                                        	 c = false;
                                        	System.out.println("Deleted: " + x);
                                        }
                                        else  c = model.addConstraintDefiniton(toAdd);
//Previous code removes the not chain succession relation                                        
                                        if (!c) {
                                                c = model.addConstraintDefiniton(new ConstraintDefinition(l, models.getModel(), cd));
                                        }
                                        l++;
                                }    
                final AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker("dummy");
                broker.addAssignmentAndView(model, new AssignmentModelView(model));
        		org.processmining.plugins.declare.visualizing.AssignmentViewBroker brokerCh = org.processmining.plugins.declare.visualizing.XMLBrokerFactory.newAssignmentBroker("dummy");
        		org.processmining.plugins.declare.visualizing.AssignmentModel modelCh = brokerCh.readAssignment();
        		org.processmining.plugins.declare.visualizing.AssignmentModelView viewCh = new org.processmining.plugins.declare.visualizing.AssignmentModelView(modelCh);
                DeclareMap decModel = new DeclareMap(model, modelCh, new AssignmentModelView(model),viewCh, broker, brokerCh);
                
                return decModel;
	        }
     /*  
   	protected DeclareModel importFromStream(final PluginContext context, final InputStream input,
		final long fileSizeInBytes)  {
		context.getFutureResult(0).setLabel("test");
		final AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker("dummy");
		final AssignmentModel model = broker.readAssignment();
		final AssignmentModelView view = new AssignmentModelView(model);
		broker.readAssignmentGraphical(model, view);
		final DeclareModel decModel = new DeclareModel(model, view);
		return decModel;
	}*/      
}
