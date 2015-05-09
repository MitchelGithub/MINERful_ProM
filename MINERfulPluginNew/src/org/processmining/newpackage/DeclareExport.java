package org.processmining.newpackage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintTemplate;
import org.processmining.plugins.declareminer.visualizing.DeclareModel;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;


@Plugin(name = "Export Declare Model", parameterLabels = { "Declare Model", "File" }, returnLabels = {}, returnTypes = {}, userAccessible = true)
@UIExportPlugin(description = "Declare files", extension = "xml")
public class DeclareExport {
	
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "F.M. Maggi", email = "F.M.Maggi@tue.nl")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Declare Models")
	public void export(final UIPluginContext context, final DeclareModel[] models, final File file) throws IOException {
		final AssignmentModel model = new AssignmentModel(models[0].getModel().getLanguage());
		model.setName("new model");
		final Vector ads = new Vector();
		ActivityDefinition activitydefinition = null;
		try {
			int k = 0;
			for (int i = 0; i < models.length; i++) {
				for (final ActivityDefinition ad : models[i].getModel().getActivityDefinitions()) {
					if (!ads.contains(ad.getName())) {
						activitydefinition = model.addActivityDefinition(k + 1);
						activitydefinition.setName(ad.getName());
						final boolean c = ads.add(ad.getName());
						System.out.println(c);
						k++;
					}
				}
			}
			new Vector();
			int l = 1;
			for (int i = 0; i < models.length; i++) {
				for (final ConstraintDefinition cd : models[i].getModel().getConstraintDefinitions()) {
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
					boolean c = model.addConstraintDefiniton(toAdd);
					if (!c) {
						c = model.addConstraintDefiniton(new ConstraintDefinition(l, models[i].getModel(), cd));
					}
					l++;
					System.out.println(c);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		final AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(file.getAbsolutePath());
		broker.addAssignmentAndView(model, new AssignmentModelView(model));
	}
}