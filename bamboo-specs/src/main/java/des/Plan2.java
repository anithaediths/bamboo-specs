package des;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;

/**
 * Plan configuration for Bamboo.
 */
@BambooSpec
public class Plan2 extends AbstractPlanSpec {

    public static void main(final String[] args) {
        Plan plan = new Plan2().createPlan();
        publishPlan(plan);
    }

    Plan createPlan() {
        return new Plan(
                project(),
                "Plan Name2", "PLANKEY2")
                .description("Plan created from (enter repository url of your plan)");
    }


}
