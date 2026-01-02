package com.franciscogarciagarzon.pricetracker.test

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses


private const val BASE_PACKAGE = "com.franciscogarciagarzon.pricetracker"

/**
 * ArchUnit tests specifically enforcing rules for the Domain layer.
 * This file MUST reside in the 'test' subdirectory:
 */
@AnalyzeClasses(packages = ["$BASE_PACKAGE.."])
class ArchitectureRulesTest {


    // Using @ArchTest makes the rule executable directly by the JUnit 5 engine.
    @ArchTest
    val domainShouldBeIndependent: ArchRule = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "..app..",
            "..presentation..",
            "..data..",
            "..architecture_tests"
        ).`as`("The Domain layer (core) must not depend on any outer layer (app, presentation, or data).")

    @ArchTest
    val domainShouldNotDependOnFrameworks: ArchRule = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "androidx..",
            "com.android..",
            "android.."
        ).`as`("The Domain layer must be free of Android/framework dependencies.")

    @ArchTest
    val presentationShouldOnlySeeDomain: ArchRule = noClasses()
        .that().resideInAPackage("..presentation..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "..app..",
            "..data..",
            "..architecture_tests.."
        ).`as`("The Presentation layer (ui) must not depend on any layer other than domain.")

    @ArchTest
    val dataShouldOnlySeeDomain: ArchRule = noClasses()
        .that().resideInAPackage("..data..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "..app..",
            "..presentation..",
            "..architecture_tests.."
        ).`as`("The Data layer (API/DB/Sensors) must not depend on any layer other than domain.")
}
