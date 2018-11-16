package pa;

import bgu.cs.absint.UnaryOperation;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.jimple.internal.JimpleLocal;

public class TransformerAssignNewToLocal extends UnaryOperation<PaState> {
protected final Local lhs;
protected final RefType baseType;
public static int  count = 0;
protected final String label = "label_new";
	

	public TransformerAssignNewToLocal(Local lhs, RefType baseType) {
		this.lhs = lhs;
		this.baseType = baseType;
	}
	
	
	@Override
	public String toString() {
		return "Pa[" + lhs.toString() + "=" + "label_new" + count + "]"; //maybe more
	}
	
	@Override
	public PaState apply(PaState input) {
		PaState out = input.copy();
		out.removeVar(lhs);  //not sure
		Local rhs = new JimpleLocal(label + count , IntType.v());
		count++;
		rhs.setType(baseType);
		out.addFactoid(lhs, rhs, null);

		return out;

	}

}
