package io.ksmt.solver.maxsmt.test.configurations

import io.ksmt.solver.KSolverConfiguration
import io.ksmt.solver.maxsmt.KMaxSMTContext
import io.ksmt.solver.maxsmt.solvers.KMaxSMTSolver
import io.ksmt.solver.maxsmt.solvers.KPMResSolver
import io.ksmt.solver.maxsmt.test.smt.KMaxSMTBenchmarkTest
import io.ksmt.solver.maxsmt.test.utils.Solver

class KPMResSMTBenchmarkTest : KMaxSMTBenchmarkTest() {
    // TODO: in fact for KPMRes we don't need MaxSMTContext.
    override val maxSmtCtx = KMaxSMTContext()

    override fun getSolver(solver: Solver): KMaxSMTSolver<KSolverConfiguration> = with(ctx) {
        val smtSolver = getSmtSolver(solver)
        return KPMResSolver(this, smtSolver)
    }
}