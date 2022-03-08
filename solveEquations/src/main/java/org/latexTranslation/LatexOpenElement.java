package org.latexTranslation;

public class LatexOpenElement extends LatexElement {

	public LatexOpenElement() {
		super(new ExpectingOperatorPutMultiplies());
	}

	@Override
	public boolean isNesting() {
		return true;
	}

}
