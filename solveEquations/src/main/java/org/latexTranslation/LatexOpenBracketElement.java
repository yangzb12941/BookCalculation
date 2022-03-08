package org.latexTranslation;

public abstract class LatexOpenBracketElement extends LatexElement {

	public LatexOpenBracketElement(PutMultipliesBehavior putMuls) {
		super(putMuls);
	}

	@Override
	public final boolean isNesting() {
		return true;
	}

}
