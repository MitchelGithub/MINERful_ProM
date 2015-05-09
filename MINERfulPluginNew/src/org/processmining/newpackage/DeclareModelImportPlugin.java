package org.processmining.newpackage;

import java.io.File;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.DeclareModel;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;


/**
 * @author michael
 * 
 */
@Plugin(name = "Import Declare Model", parameterLabels = { "Filename" }, returnLabels = { "Declare Model" }, returnTypes = { DeclareModel.class })
@UIImportPlugin(description = "Declare Model", extensions = { "xml" })
public class DeclareModelImportPlugin { //extends AbstractImportPlugin 
	
	public AssignmentViewBroker broker2;
	
	protected FileFilter getFileFilter() {
		return new FileNameExtensionFilter("XML files", "xml");
	}

	//@Override
	public DeclareModel importFromStream(final PluginContext context, final File input,
			final String filename, final long fileSizeInBytes) throws Exception {
		try {
			context.getFutureResult(0).setLabel("Declare Model imported from " + filename);
		} catch (final Throwable _) {
			// Dont care if this fails
		}
		final AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(input.getAbsolutePath());
		final AssignmentModel model = broker.readAssignment();
		final AssignmentModelView view = new AssignmentModelView(model);
		broker.readAssignmentGraphical(model, view);
		broker2 = broker;
		final DeclareModel decModel = new DeclareModel(model, view);
		return decModel;
	}
	
	public AssignmentViewBroker getBroker(){
		return broker2;
	}
}