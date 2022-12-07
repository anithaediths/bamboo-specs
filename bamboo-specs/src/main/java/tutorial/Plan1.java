package tutorial;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.AtlassianModule;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.repository.VcsChangeDetection;
import com.atlassian.bamboo.specs.api.builders.repository.VcsRepositoryIdentifier;
import com.atlassian.bamboo.specs.api.builders.requirement.Requirement;
import com.atlassian.bamboo.specs.api.builders.task.AnyTask;
import com.atlassian.bamboo.specs.builders.repository.git.UserPasswordAuthentication;
import com.atlassian.bamboo.specs.builders.repository.github.GitHubRepository;
import com.atlassian.bamboo.specs.builders.repository.viewer.GitHubRepositoryViewer;
import com.atlassian.bamboo.specs.builders.task.*;
import com.atlassian.bamboo.specs.builders.trigger.RemoteTrigger;
import com.atlassian.bamboo.specs.builders.trigger.RepositoryPollingTrigger;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.util.MapBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

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

    public Plan createPlan() {
        return new Plan(project(), "Plan Name React Chart App", "PLANKEY1")
                .description("Plan for implementing CI CD 1")
                .pluginConfigurations(new ConcurrentBuilds()
                        .useSystemWideDefault(false))
                .stages(createStage())
                .linkedRepositories("react-chart-app")
                .planRepositories(getGitHubRepository())
                .triggers(new RepositoryPollingTrigger()
                                .enabled(false)
                                .withPollingPeriod(Duration.ofSeconds(30)),
                        new RemoteTrigger()
                                .description("GitHub trigger")
                                .enabled(false))
                .planBranchManagement(new PlanBranchManagement()
                        .delete(new BranchCleanup())
                        .notificationForCommitters());
    }

    private GitHubRepository getGitHubRepository() {
        return new GitHubRepository()
                .name("react-chart-app-second")
                .repositoryViewer(new GitHubRepositoryViewer())
                .repository("anithaediths/react-chart-app")
                .branch("main")
                .authentication(new UserPasswordAuthentication("anithaediths")
                        .password("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"))
                .changeDetection(new VcsChangeDetection());
    }

    @NotNull
    private Stage createStage() {
        return new Stage("Continuous Integration")
                .jobs(new Job("CI Job", new BambooKey("JOB1"))
                        .artifacts(new Artifact()
                                        .name("MyWARFile")
                                        .copyPattern("*.war")
                                        .location("target")
                                        .shared(true),
                                new Artifact()
                                        .name("Cobertura Report")
                                        .copyPattern("*")
                                        .location("target/site/cobertura"))
                        .tasks(new CleanWorkingDirectoryTask()
                                        .description("Clean working directory"),
                                new VcsCheckoutTask()
                                        .description("Checkout GitHub Repo")
                                        .checkoutItems(new CheckoutItem()
                                                .repository(new VcsRepositoryIdentifier()
                                                        .name("sherlock")))
                                        .cleanCheckout(true),
                                new MavenTask()
                                        .description("Maven with Cobertura CC report")
                                        .goal("cobertura:cobertura -Dcobertura.report.format=xml")
                                        .jdk("JDK 1.8")
                                        .executableLabel("Maven 3"),
                                new MavenTask()
                                        .description("Build task")
                                        .enabled(false)
                                        .goal("clean compile package")
                                        .jdk("JDK 1.8")
                                        .executableLabel("Maven 3")));
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
