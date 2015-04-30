/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minerful.miner.stats;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlTransient;

import minerful.concept.Event;
import minerful.concept.TaskChar;
import minerful.concept.TaskCharArchive;
import minerful.miner.stats.charsets.FixedTaskSetIncrementalCountersCollection;

public class LocalStatsWrapperForCharsetsWAlternation extends LocalStatsWrapperForCharsets {
	@XmlTransient
	protected Map<TaskChar, Integer> missingAtThisStepBeforeNextRepetition;

    public LocalStatsWrapperForCharsetsWAlternation(TaskCharArchive archive, TaskChar baseTask, Integer maximumCharactersSetSize) {
    	super(archive, baseTask);
    	Set<TaskChar> alphabet = archive.getTaskChars();
        this.neverAppearedCharacterSets = new FixedTaskSetIncrementalCountersCollection(alphabet);
        this.neverMoreAppearedAfterCharacterSets = new FixedTaskSetIncrementalCountersCollection(alphabet);
        this.neverMoreAppearedBeforeCharacterSets = new FixedTaskSetIncrementalCountersCollection(alphabet);
//		this.orderedAlreadyMetCharsAtThisStep = new ArrayList<Character>();
//		this.alreadyMetCharsAtThisStep = new TreeSet<Character>();
//		this.nearestMatesAtThisStep = new TreeMap<String, Character>();
        this.maximumTasksSetSize = (
        		maximumCharactersSetSize == null ? null :
        			(maximumCharactersSetSize < archive.size() ? maximumCharactersSetSize : archive.size())
		);
        if (this.maximumTasksSetSize != null && this.maximumTasksSetSize < archive.size()) {
	        this.repetitionsBeforeCharactersAppearingAfter = new FixedTaskSetIncrementalCountersCollection(alphabet);
	        this.repetitionsAfterCharactersAppearingBefore = new FixedTaskSetIncrementalCountersCollection(alphabet);
        } else {
	        this.repetitionsBeforeCharactersAppearingAfter = new FixedTaskSetIncrementalCountersCollection(alphabet);
	        this.repetitionsAfterCharactersAppearingBefore = new FixedTaskSetIncrementalCountersCollection(alphabet);
        }
    }

    public LocalStatsWrapperForCharsetsWAlternation(TaskCharArchive archive, TaskChar baseTask) {
    	this(archive, baseTask, null);
    }

    public int getMaximumCharactersSetSize() {
		return this.maximumTasksSetSize;
	}

	@Override
	protected void initLocalStatsTable(Set<TaskChar> alphabet) {
    	super.initLocalStatsTable(alphabet);
		this.missingAtThisStepBeforeNextRepetition = new TreeMap<TaskChar, Integer>();
		for (TaskChar task : alphabet) {
			this.localStatsTable.put(task, new StatsCell());
			if (!task.equals(this.baseTask)) {
				this.missingAtThisStepBeforeNextRepetition.put(task, 0);
			}
		}
	}
    
    @Override
	void newAtPosition(Event event, int position, boolean onwards) {
		if (this.archive.containsTaskCharByEvent(event)) {
			TaskChar tCh = this.archive.getTaskCharByEvent(event);
			
	        /* if the appeared character is equal to this */
	        if (tCh.equals(this.baseTask)) {
	            for (TaskChar nevMoreAppTCh: this.neverMoreAppearancesAtThisStep.keySet()) {
	                this.neverMoreAppearancesAtThisStep.put(nevMoreAppTCh,
	                        this.neverMoreAppearancesAtThisStep.get(nevMoreAppTCh) + 1
	                );
	            }
	            /* if this is the first occurrence in the step, record it */
	            if (this.firstOccurrenceInStep == null) {
	                this.firstOccurrenceInStep = position;
	            } else {
	                /* if this is not the first time this chr appears in the step, initialize the repetitions register */
	                if (repetitionsAtThisStep == null) {
	                    repetitionsAtThisStep = new TreeSet<Integer>();
	                }
	            }
	            if (onwards) {
	            	this.repetitionsBeforeCharactersAppearingAfter.merge(
	            			FixedTaskSetIncrementalCountersCollection.fromNumberedSingletons(missingAtThisStepBeforeNextRepetition)
	            	);
	            }
	            else {
	            	this.repetitionsAfterCharactersAppearingBefore.merge(
	            			FixedTaskSetIncrementalCountersCollection.fromNumberedSingletons(missingAtThisStepBeforeNextRepetition)
	            	);
	            }
	            // TODO Document this passage: it is crucial!
	            for (TaskChar chr: this.missingAtThisStepBeforeNextRepetition.keySet()) {
	                this.missingAtThisStepBeforeNextRepetition.put(chr, 1);
	            }
	        }
	        /* if the appeared character is NOT equal to this */
	        else {
	            /* store the info that chr appears after the pivot */
	            this.neverMoreAppearancesAtThisStep.put(tCh, 0);
	            this.missingAtThisStepBeforeNextRepetition.put(tCh, 0);
	        }
	
	        if (repetitionsAtThisStep != null) {
	            /* for each repetition of the same character during the analysis, record not only the info of the appearance at a distance equal to (chr.position - firstOccurrenceInStep.position), but also at the (chr.position - otherOccurrenceInStep.position) for each other appearance of the pivot! */
	            /* THIS IS THE VERY BIG TRICK TO AVOID ANY TRANSITIVE CLOSURE!! */
	            for (Integer occurredAlsoAt : repetitionsAtThisStep) {
	                this.localStatsTable.get(tCh).newAtDistance(position - occurredAlsoAt);
	            }
	        }
	        /* If this is not the first occurrence position, record the distance equal to (chr.position - firstOccurrenceInStep.position) */
	        if (firstOccurrenceInStep != position)
	            this.localStatsTable.get(tCh).newAtDistance(position - firstOccurrenceInStep);
	        /* If this is the repetition of the pivot, record it (it is needed for the computation of all the other distances!) */
	        if (this.repetitionsAtThisStep != null && tCh.equals(this.baseTask)) {
	            this.repetitionsAtThisStep.add(position);
	        }
	
//			this.alreadyMetCharsAtThisStep.add(character);
//			this.orderedAlreadyMetCharsAtThisStep.remove(character);
//			this.orderedAlreadyMetCharsAtThisStep.add(0, character);
		}
    }

    @Override
	void finalizeAnalysisStep(boolean onwards, boolean secondPass) {
    	// Step 1: as in the single-char case:
    	/* Record the amount of occurrences AT THIS STEP and the total amount OVER ALL OF THE STEPS */
    	/* Record what did not appear in the step, afterwards or backwards */
    	/* Resets the switchers for the alternations counter */
    	/* Resets the local stats table counters */
        super.finalizeAnalysisStep(onwards, secondPass);
        for (TaskChar chr: this.missingAtThisStepBeforeNextRepetition.keySet()) {
            this.missingAtThisStepBeforeNextRepetition.put(chr, 0);
        }
    }

    @Override
	protected void setAsNeverAppeared(Set<TaskChar> neverAppearedStuff) {
    	if (neverAppearedStuff.size() < 1) {
    		return;
    	}
    	// Step 1: each character in neverAppearedStuff must be recorded as never appearing in the current string 
    	for (TaskChar neverAppearedChr : neverAppearedStuff) {
    		super.setAsNeverAppeared(neverAppearedChr);
    	}
    	// Step 2: the whole set of characters has to be recorded at once
    	// Step 2 is needed because Step 1 loses the information that all of the char's are not read at once all together in a string
    	addSetToNeverAppearedCharSets(
    			neverAppearedStuff,
				(   (this.repetitionsAtThisStep == null || this.repetitionsAtThisStep.size() < 1) 
                    ? 1
                    : this.repetitionsAtThisStep.size() + 1
                )
            );
    }
    
    protected void addSetToNeverAppearedCharSets(Set<TaskChar> neverAppearedStuff, int sum) {
    	this.neverAppearedCharacterSets.incrementAt(neverAppearedStuff, sum);
    }
     
    @Override
	protected void recordCharactersThatNeverAppearedAnymoreInStep(boolean onwards) {
    	// Step 1: aggregate this.neverMoreAppearancesInStep and record
    	if (onwards) {
	    	this.neverMoreAppearedAfterCharacterSets.merge(
	    			FixedTaskSetIncrementalCountersCollection.fromNumberedSingletons(neverMoreAppearancesAtThisStep)
	    	);
    	} else {
    		this.neverMoreAppearedBeforeCharacterSets.merge(
	    			FixedTaskSetIncrementalCountersCollection.fromNumberedSingletons(neverMoreAppearancesAtThisStep)
	    	);
    	}
    	// Step 2: update singletons
        super.recordCharactersThatNeverAppearedAnymoreInStep(onwards);
    }
   
    @Override
    public String toString() {
        if (this.totalAmountOfAppearances == 0)
            return "";

        StringBuilder sBuf = new StringBuilder();
        for (TaskChar key : this.localStatsTable.keySet()) {
            sBuf.append("\t\t[" + key + "] => " + this.localStatsTable.get(key).toString());
        }
        sBuf.append("\n\t\t\tnever's " + this.neverAppearedCharacterSets.toString().replace("\n", "\n\t\t\t\t"));
        sBuf.append("\n\t\t\tnever-after's " + this.neverMoreAppearedAfterCharacterSets.toString().replace("\n", "\n\t\t\t\t"));
        sBuf.append("\n\t\t\tnever-before's " + this.neverMoreAppearedBeforeCharacterSets.toString().replace("\n", "\n\t\t\t\t"));
        sBuf.append("\n\t\t\trepetitions-in-between-after's " + this.repetitionsBeforeCharactersAppearingAfter.toString().replace("\n", "\n\t\t\t\t"));
        sBuf.append("\n\t\t\trepetitions-in-between-before's " + this.repetitionsAfterCharactersAppearingBefore.toString().replace("\n", "\n\t\t\t\t"));
        sBuf.append("\n");

        return sBuf.toString();
    }

}