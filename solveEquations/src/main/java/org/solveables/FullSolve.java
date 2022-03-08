package org.solveables;

import org.solutions.Solution;

public class FullSolve {

	public Solveable fullSolve(Solveable toSolve) {
		Solveable solveableState = toSolve;
		Solution tryGetSolution = solveableState.reachedSolution();
		while (solveableState.reachedSolution() == null) {
			solveableState = solveableState.stepSolve();
			tryGetSolution = solveableState.reachedSolution();
		}
		return solveableState;
	}

}
