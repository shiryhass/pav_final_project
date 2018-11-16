package pa;

import bgu.cs.absint.UnaryOperation;
import soot.Local;
import soot.SootField;

public class TransformerAssignLocalToNull  extends
UnaryOperation<PaState> {
	protected final Local lhs;
	

	public TransformerAssignLocalToNull(Local lhs) {
		this.lhs = lhs;
	}
	
	
	@Override
	public String toString() {
		return "Pa[" + lhs.toString() + "=" + "null" + "]"; //maybe more
	}
	
	@Override
	public PaState apply(PaState input) {
		System.out.println("null");
		PaState out = input.copy();
		out.removeVar(lhs);
		SootField a = null;
		out.addFactoid(lhs, PaFactoid.NULL_VAR, a);
		
		return out;
	}
	
	
	
	
	/*	@Override
	public ZoneState apply(ZoneState input) {
		ZoneState delta1 = new ZoneState();
		ZoneState delta2 = new ZoneState();
		delta1.addFactoid(lhs, rhs, IntConstant.v(- 1));
		ZoneState out = input.copy();
		delta2.addFactoid(rhs,lhs, ZoneDomain.zero);
		if (polarity) {
			out = ZoneDomain.v().lb(out, delta1);
		} else {
			out = ZoneDomain.v().lb(out, delta2);
		}
		return out;
	}*/
	
}