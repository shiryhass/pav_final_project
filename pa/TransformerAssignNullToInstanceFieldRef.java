package pa;

import java.util.Iterator;

import bgu.cs.absint.UnaryOperation;
import soot.Local;
import soot.SootField;

public class TransformerAssignNullToInstanceFieldRef extends UnaryOperation<PaState> {
	protected final Local base;
	protected final SootField field;
	
	public  TransformerAssignNullToInstanceFieldRef(Local base, SootField field){
		this.base = base;
		this.field = field;
	}
	
	@Override
	public String toString() {
		return "Pa[" + base.toString()+"."+ field.getName()+ "=" + "null" + "]"; //maybe more
	}
	
	@Override
	public PaState apply(PaState input) {
		PaState out = input.copy();
		out.removeVar(base);  //not sure
		PaState tmp = out.copy();
		for (Iterator<PaFactoid> iter = tmp.iterator(); iter.hasNext();) {
			PaFactoid var = iter.next();
			if( var.lhs.equivTo(base) && var.field.equals(field)){
				{
					out.addFactoid(base, PaFactoid.NULL_VAR, field); //not sure
					break;
				}

				}
			}

		return out;

	}
}
/**
 * Matches statements of the form {@code x.n = null} where 'x' is a local
 * variable.
 */
