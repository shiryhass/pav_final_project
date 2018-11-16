package pa;

import java.util.Iterator;

import bgu.cs.absint.UnaryOperation;
import soot.Local;
import soot.SootField;

public class TransformerLocalAssignToFieldRef extends UnaryOperation<PaState> {
	protected final Local base;
	protected final SootField field;
	protected final Local rhs;
	
	public TransformerLocalAssignToFieldRef(Local base, SootField field,
			Local rhs){
		this.base = base;
		this.field = field;
		this.rhs = rhs;
	}
	@Override
	public String toString() {
		return "Pa[" + base.toString() +"."+ field.getName()+ "=" + rhs.toString()+"]"; //maybe more
	}
	
	@Override
	public PaState apply(PaState input) {
		PaState out = input.copy();
		out.removeVar(base);  //not sure
		PaState tmp = out.copy();
		for (Iterator<PaFactoid> iter = tmp.iterator(); iter.hasNext();) {
			PaFactoid var = iter.next();
			//			if( var.lhs.equivTo(base) && var.field.equals(field)){
			if( var.lhs.equivTo(base) && var.field.equals(field)){
				{
					//out.addFactoid(rhs, var.rhs, field);
					//out.add(var);
					out.addFactoid(base, var.rhs, field); //not sure
					break;
				}

				}
			}

		return out;

	}
}
/**
 * Matches statements of the form {@code x.n = y} where 'x' and 'y' are
 * local variables.
 */