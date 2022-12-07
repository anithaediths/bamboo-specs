package des;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;

/**
 * Plan configuration for Bamboo.
 */
@BambooSpec
public class Plan3 extends AbstractPlanSpec {

    public static void main(final String[] args) {
        Plan plan = new Plan3().createPlan();
        publishPlan(plan);
    }

    Plan createPlan() {
        return new Plan(
                project(),
                "Plan Name3", "PLANKEY3")
                .description("Plan created from (enter repository url of your plan)");
    }


}
