/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package minerful.miner.stats;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import minerful.concept.TaskChar;
import minerful.concept.TaskCharArchive;
import minerful.miner.stats.xmlenc.GlobalStatsMapAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GlobalStatsTable {

	@XmlElement
	@XmlJavaTypeAdapter(value=GlobalStatsMapAdapter.class)
    public Map<TaskChar, LocalStatsWrapper> statsTable;
	@XmlTransient
    public final TaskCharArchive taskCharArchive;
	@XmlAttribute
    public long logSize;
	@XmlAttribute
	public final Integer maximumBranchingFactor;
	
	private GlobalStatsTable() {
		this.maximumBranchingFactor = null;
		this.taskCharArchive = new TaskCharArchive();
	}

	public GlobalStatsTable(TaskCharArchive taskCharArchive, long testbedDimension, Integer maximumBranchingFactor) {
		this.taskCharArchive = taskCharArchive;
		this.logSize = testbedDimension;
		this.maximumBranchingFactor = maximumBranchingFactor;
		this.initGlobalStatsTable();
	}

    public GlobalStatsTable(TaskCharArchive taskCharArchive) {
        this(taskCharArchive, 0, null);
    }
    public GlobalStatsTable(TaskCharArchive taskCharArchive, Integer maximumBranchingFactor) {
    	this(taskCharArchive, 0, maximumBranchingFactor);
    }

    private void initGlobalStatsTable() {
        this.statsTable = new TreeMap<TaskChar, LocalStatsWrapper>();
        Set<TaskChar> alphabet = this.taskCharArchive.getTaskChars();
        if (this.isForBranchedConstraints()) {
        	for (TaskChar task: this.taskCharArchive.getTaskChars()) {
       			this.statsTable.put(task, new LocalStatsWrapperForCharsetsWAlternation(taskCharArchive, task, maximumBranchingFactor));
	        }
        } else {
        	for (TaskChar task: alphabet) {
	            this.statsTable.put(task, new LocalStatsWrapper(taskCharArchive, task));
	        }
        }
    }
	
    public boolean isForBranchedConstraints() {
		return maximumBranchingFactor != null && maximumBranchingFactor > 1;
	}

    @Override
    public String toString() {
        StringBuilder sBuf = new StringBuilder();
        for(TaskChar key: this.statsTable.keySet()) {
            StringBuilder aggregateAppearancesBuffer = new StringBuilder();
            LocalStatsWrapper statsWrapper = this.statsTable.get(key);
            if (statsWrapper.repetitions != null) {
                for (Integer counter: statsWrapper.repetitions.keySet()) {
                    aggregateAppearancesBuffer.append(", <");
                    aggregateAppearancesBuffer.append(counter);
                    aggregateAppearancesBuffer.append(", ");
                    aggregateAppearancesBuffer.append(statsWrapper.repetitions.get(counter));
                    aggregateAppearancesBuffer.append(">");
                }
            }
            sBuf.append(
                    "\t[" + key + "\n"
                    + "\t aggregate appearances = {"
                    + (aggregateAppearancesBuffer.length() > 0 ? aggregateAppearancesBuffer.substring(2) : "")
                    + "}, for a total amount of "
                    + statsWrapper.getTotalAmountOfAppearances()
                    + " time(/s)\n");
            sBuf.append("\t as the first for " + statsWrapper.getAppearancesAsFirst() + ",");   
            sBuf.append(" as the last for " + statsWrapper.appearancesAsLast + " time(/s)");
            sBuf.append("\t]\n");
            sBuf.append(statsWrapper.toString());

        }
        return sBuf.toString();
    }
}