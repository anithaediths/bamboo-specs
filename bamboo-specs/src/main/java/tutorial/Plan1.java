package tutorial;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.requirement.Requirement;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.util.BambooServer;

/**
 * Plan configuration for Bamboo.
 * Learn more on: <a href="https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs</a>
 */
@BambooSpec
public class Plan1 extends AbstractPlanSpec {

    public static void main(final String[] args) {
        Plan plan = new Plan1().createPlan();
        publishPlan(plan);
    }

    Plan createPlan() {
        return new Plan(
                project(),
                "Plan Name1", "PLANKEY1")
                .description("Plan created from (enter repository url of your plan)")
                .stages(new Stage("Default stage")
                                .jobs(new Job("Default Job", new BambooKey("JOB1"))
                                        .tasks(new VcsCheckoutTask()
                                                .description("Checkout Default Repository")
                                                .checkoutItems(new CheckoutItem().defaultRepository()),
                                                new ScriptTask()
                                                        .description("build")
                                                        .location(ScriptTaskProperties.Location.FILE)
                                                        .fileFromPath("build.sh"))
                                        .requirements(new Requirement("system.os")
                                                .matchValue("linux")
                                                .matchType(Requirement.MatchType.EQUALS))));

    }


}
