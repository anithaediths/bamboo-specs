package tutorial;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;

/**
 * Plan configuration for Bamboo.
 * Learn more on: <a href="https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs</a>
 */
@BambooSpec
public class Plan3 extends AbstractPlanSpec {

    public static void main(final String[] args)  {
        Plan plan = new Plan3().createPlan();
        publishPlan(plan);
    }

    Plan createPlan() {
        return new Plan(
                project(),
                "Plan Name", "PLANKEY3")
                .description("Plan created from (enter repository url of your plan)");
    }


}
