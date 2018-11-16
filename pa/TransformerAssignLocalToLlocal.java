package pa;

import java.util.Iterator;

import bgu.cs.absint.UnaryOperation;
import soot.Local;

public class TransformerAssignLocalToLlocal extends 
UnaryOperation<PaState>  {
	protected final Local lhs;
	protected final Local rhs;
	 
	public TransformerAssignLocalToLlocal(Local lhs,Local rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}


	@Override
	public String toString() {
		return "Pa[" + lhs.toString() + "=" + rhs.toString() + "]"; //maybe more
	}
	
	
	@Override
	public PaState apply(PaState input) {
		PaState out = input.copy();
		out.removeVar(lhs); 
		PaState tmp = out.copy();
		for (Iterator<PaFactoid> iter = tmp.iterator(); iter.hasNext();) {
			PaFactoid var = iter.next();
				if( var.lhs.equivTo(rhs) ){
					{
						out.addFactoid(lhs, var.rhs, var.field);
					}

				}
			}

		return out;
	}
}
