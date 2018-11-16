package pa;

import java.util.HashSet;

import bgu.cs.absint.constructor.ConjunctiveState;
import soot.Local;
import soot.SootField;

public class PaState extends ConjunctiveState<Local, PaFactoid> {
	public static final PaState bottom = new PaState(false) {
		@Override
		public boolean addFactoid(Local lhs, Local rhs, SootField rhsF) {
			throw new Error("Attempt to modify " + toString());
		}

		@Override
		public boolean add(PaFactoid factoid) {
			throw new Error("Attempt to modify " + toString());
		}

		@Override
		public boolean removeVar(Local lhs) {
			throw new Error("Attempt to modify " + toString());
		}
	};

	/**
	 * An immutable top element.
	 */
	public static final PaState top = new PaState() {
		@Override
		public boolean addFactoid(Local lhs, Local rhs, SootField rhsF) {
			throw new Error("Attempt to modify " + toString());
		}

		@Override
		public boolean add(PaFactoid factoid) {
			throw new Error("Attempt to modify " + toString());
		}

		@Override
		public boolean removeVar(Local lhs) {
			throw new Error("Attempt to modify " + toString());
		}
	};

	public PaState() {
		super();
	}

	@Override
	public PaState copy() {
		if (this == bottom)
			return bottom;
		else
			return new PaState(this);
	}

	public boolean addFactoid(Local lhs, Local rhs, SootField rhsF) {
		return factoids.add(new PaFactoid(lhs, rhs, rhsF));
	}

	protected PaState(boolean dummy) {
		super(false);
	}
	
	protected PaState(PaState copyFrom) {
		super(copyFrom.factoids);
	}

}
