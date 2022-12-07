package tutorial;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.repository.VcsRepositoryIdentifier;
import com.atlassian.bamboo.specs.api.builders.requirement.Requirement;
import com.atlassian.bamboo.specs.builders.repository.git.UserPasswordAuthentication;
import com.atlassian.bamboo.specs.builders.repository.github.GitHubRepository;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.util.BambooServer;
import org.jetbrains.annotations.NotNull;

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
        Plan plan = new Plan(project(), "Plan Name1", "PLANKEY1");
        plan.description("Plan created from (enter repository url of your plan)");

        GitHubRepository gitHubRepository = new GitHubRepository()
                .name("terraform-react")
                .authentication(new UserPasswordAuthentication("anithaediths")
                        .password("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"))
                .branch("main");

        plan.planRepositories(gitHubRepository);
        plan.stages(createStage().jobs(createJob()));
        return plan;
    }

    @NotNull
    private Stage createStage() {
        return new Stage("Default stage");
    }

    private Job createJob() {
        return new Job("Default Job", new BambooKey("JOB1"))
                .tasks(new VcsCheckoutTask()
                                .description("Checkout Default Repository")
                                .checkoutItems(new CheckoutItem()
                                        .repository(new VcsRepositoryIdentifier()
                                                .name("terraform-react")))
                                .cleanCheckout(true),
                        new ScriptTask()
                                .description("build")
                                .location(ScriptTaskProperties.Location.FILE)
                                .fileFromPath("build.sh"))
                .requirements(new Requirement("system.os")
                        .matchValue("linux")
                        .matchType(Requirement.MatchType.EQUALS));
    }
}
