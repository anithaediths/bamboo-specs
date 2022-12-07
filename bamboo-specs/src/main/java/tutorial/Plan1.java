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
        final Plan plan = new Plan(new Project()
                /*.oid(new BambooOid("cxx7l5poipz5"))*/
                .key(new BambooKey("MYD"))
                .name("MyDev")
                .description("Dev project for testing CICD"),
                "MyPlan1",
                new BambooKey("MYZ"))
                /*.oid(new BambooOid("cxnidkcgoxkx"))*/
                .description("Plan for implementing CICD 1")
                .pluginConfigurations(new ConcurrentBuilds()
                        .useSystemWideDefault(false))
                .stages(new Stage("Continuous Integration")
                        .jobs(new Job("CI Job",
                                new BambooKey("JOB1"))
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
                                        new AnyTask(new AtlassianModule("ch.mibex.bamboo.sonar4bamboo:sonar4bamboo.maven3task"))
                                                .description("Sonar Report Publisher")
                                                .enabled(false)
                                                .configuration(new MapBuilder()
                                                        .put("incrementalFileForInclusionList", "")
                                                        .put("chosenSonarConfigId", "1")
                                                        .put("useGradleWrapper", "")
                                                        .put("useNewGradleSonarQubePlugin", "")
                                                        .put("sonarJavaSource", "")
                                                        .put("sonarProjectName", "")
                                                        .put("buildJdk", "JDK 1.8")
                                                        .put("gradleWrapperLocation", "")
                                                        .put("sonarLanguage", "")
                                                        .put("sonarSources", "")
                                                        .put("useGlobalSonarServerConfig", "true")
                                                        .put("incrementalMode", "")
                                                        .put("failBuildForBrokenQualityGates", "")
                                                        .put("sonarTests", "")
                                                        .put("incrementalNoPullRequest", "incrementalModeFailBuildField")
                                                        .put("failBuildForSonarErrors", "")
                                                        .put("sonarProjectVersion", "")
                                                        .put("sonarBranch", "")
                                                        .put("executable", "Maven 3")
                                                        .put("illegalBranchCharsReplacement", "_")
                                                        .put("failBuildForTaskErrors", "true")
                                                        .put("incrementalModeNotPossible", "incrementalModeRunFullAnalysis")
                                                        .put("sonarJavaTarget", "")
                                                        .put("environmentVariables", "")
                                                        .put("incrementalModeGitBranchPattern", "")
                                                        .put("legacyBranching", "true")
                                                        .put("replaceSpecialBranchChars", "")
                                                        .put("additionalProperties", "")
                                                        .put("autoBranch", "true")
                                                        .put("sonarProjectKey", "")
                                                        .put("incrementalModeBambooUser", "")
                                                        .put("overrideSonarBuildConfig", "")
                                                        .put("workingSubDirectory", "")
                                                        .build()),
                                        new MavenTask()
                                                .description("Build task")
                                                .enabled(false)
                                                .goal("clean compile package")
                                                .jdk("JDK 1.8")
                                                .executableLabel("Maven 3"))))
                .linkedRepositories("react-chart-app")
                .planRepositories(new GitHubRepository()
                        .name("sherlock")
                        /* .oid(new BambooOid("cxro1e2p6vwh"))*/
                        .repositoryViewer(new GitHubRepositoryViewer())
                        .repository("Gpkmr/sherlock")
                        .branch("master")
                        .authentication(new UserPasswordAuthentication("gpkmr")
                                .password("Gopi@16!"))
                        .changeDetection(new VcsChangeDetection()))

                .triggers(new RepositoryPollingTrigger()
                                .enabled(false)
                                .withPollingPeriod(Duration.ofSeconds(30)),
                        new RemoteTrigger()
                                .description("GitHub trigger")
                                .enabled(false))
                .planBranchManagement(new PlanBranchManagement()
                        .delete(new BranchCleanup())
                        .notificationForCommitters());
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
