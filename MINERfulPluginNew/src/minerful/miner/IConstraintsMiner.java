package minerful.miner;

import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.TaskCharRelatedConstraintsBag;

public interface IConstraintsMiner {
	TaskCharRelatedConstraintsBag discoverConstraints();

	TaskCharRelatedConstraintsBag discoverConstraints(
			TaskCharRelatedConstraintsBag constraintsBag);

	long howManyPossibleConstraints();

	long getComputedConstraintsAboveTresholds();

	boolean hasValuesAboveThresholds(Constraint c);

	boolean hasSufficientInterestFactor(Constraint c);

	boolean hasSufficientConfidence(Constraint c);

	boolean hasSufficientSupport(Constraint c);

	void setInterestFactorThreshold(Double interestFactorThreshold);

	Double getInterestFactorThreshold();

	void setConfidenceThreshold(Double confidenceThreshold);

	Double getConfidenceThreshold();

	void setSupportThreshold(Double supportThreshold);

	Double getSupportThreshold();
}
