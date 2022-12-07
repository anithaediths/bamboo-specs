## Bamboo Spec
Bamboo specs are specifications to create new Bamboo pipleines

## Steps
Below steps are to create bamboo spec using Java. These are specifications for Build plan creation

1. Create a Bamboo spec repository, a folder named bamboo-specs are to be created
2. Defined the Java code package, AbstractPlanSpec has common code for creating any plan class including their permissions, parent project details and Bamboo servers
3. Plan java classes can extend the AbstractPlanSpec and define plan specific stages and jobs
4. There can be multiple stages and multiple jobs per stage in Bamboo plans. A Stage could be something like Build and Test and Jobs could be having tasks such as checkout, build, test, validate
5. ReactPlan.java has an example of checkout from github repository and a linked repository , with a stage namely "Continuous Integration"

## Steps to run bamboo spec in Bamboo 
Below steps can be used to refresh multiple plans at the same time.

1. Go to the Linked Repository
2. Navigate to Specs status 
3. click on Scan
4. The Bamboo plans along with Project if not present, will be created.

## Best Practices
Have one bamboo spec repository containing all Plans for a given Project in the same repo.
Abstract as much common methods as possible to maintain DRY principle.

