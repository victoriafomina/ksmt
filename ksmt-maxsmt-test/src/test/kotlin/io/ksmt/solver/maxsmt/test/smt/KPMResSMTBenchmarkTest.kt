package io.ksmt.solver.maxsmt.test.smt

import io.ksmt.solver.KSolver
import io.ksmt.solver.KSolverConfiguration
import io.ksmt.solver.maxsmt.solvers.KMaxSMTSolver
import io.ksmt.solver.maxsmt.solvers.KPMResSolver
import io.ksmt.solver.maxsmt.test.utils.Solver

class KPMResSMTBenchmarkTest : KMaxSMTBenchmarkTest() {
    override fun getSolver(solver: Solver): KMaxSMTSolver<KSolverConfiguration> = with(ctx) {
        val smtSolver: KSolver<KSolverConfiguration> = getSmtSolver(solver)
        return KPMResSolver(this, smtSolver)
    }
}
