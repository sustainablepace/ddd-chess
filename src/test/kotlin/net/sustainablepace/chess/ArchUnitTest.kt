package net.sustainablepace.chess

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.Command
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@AnalyzeClasses(packages = ["net.sustainablepace.chess"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchUnitTest {
    @ArchTest
    val `hexagon architecture should be enforced` = Architectures.onionArchitecture()
        .domainModels("..domain..")
        .applicationServices("..application..")
        .adapter("adapter", "..adapter..")
        .withOptionalLayers(true)

    @ArchTest
    val `commands are incoming ports` = ArchRuleDefinition.classes()
        .that().areAssignableTo(Command::class.java)
        .should().resideInAPackage("..application.port.in..")

    @ArchTest
    val `outgoing ports are always interfaces` = ArchRuleDefinition.classes()
        .that().resideInAPackage("..application.port.out..")
        .should().beInterfaces()

    @ArchTest
    val `application services are workflows` = ArchRuleDefinition.classes()
        .that().areAnnotatedWith(Service::class.java)
        .should().resideInAPackage("..application.service..")
        .andShould().beAssignableTo(ApplicationService::class.java)

    @ArchTest
    val `repositories are adapters` = ArchRuleDefinition.classes()
        .that().areAnnotatedWith(Repository::class.java)
        .should().resideInAPackage("..adapter.database..")

    @ArchTest
    val `controllers are adapters` = ArchRuleDefinition.classes()
        .that().areAnnotatedWith(Controller::class.java)
        .should().resideInAPackage("..adapter.web..")

    @ArchTest
    val `adapters are independent of one another` = SlicesRuleDefinition.slices()
        .matching("net.sustainablepace.chess.adapter.(*)")
        .should().notDependOnEachOther()

}