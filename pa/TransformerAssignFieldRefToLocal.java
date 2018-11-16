package pa;

import java.util.Iterator;

import bgu.cs.absint.UnaryOperation;
import soot.Local;
import soot.SootField;

public class TransformerAssignFieldRefToLocal extends 
UnaryOperation<PaState>  {
	protected final Local lhs;
	protected final Local rhs;
	protected final SootField field;
	
	public TransformerAssignFieldRefToLocal(Local lhs, Local rhs, SootField field) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.field = field;
	}
	
	@Override
	public String toString() {
		return "Pa[" + lhs.toString() + "=" + "pa" + rhs.toString() + field.getName()+"]"; //maybe more
	}
	
	@Override
	public PaState apply(PaState input) {
		PaState out = input.copy();
		out.removeVar(lhs); 
		PaState tmp = out.copy();
		for (Iterator<PaFactoid> iter = tmp.iterator(); iter.hasNext();) {
			PaFactoid var = iter.next();
				if( var.lhs.equivTo(rhs) && var.field.equals(field)){
					{
						//out.addFactoid(rhs, var.rhs, field);
						//out.add(var);
						out.addFactoid(lhs, var.rhs, null);
						break;
					}

				}
			}
		
		toString();
		return out;
	}
}

