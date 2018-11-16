package pa;


import bgu.cs.absint.soot.SootFactoid;
import soot.IntType;
import soot.Local;
import soot.SootField;
import soot.jimple.internal.JimpleLocal;

public class PaFactoid extends SootFactoid {
	public static final Local NULL_VAR = new JimpleLocal("NULL", IntType.v());

	public final Local lhs;
	public final Local rhs;
	public final SootField field;

	public PaFactoid(Local lhs, Local rhs, SootField field) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.field = field;
		//assert lhs != null; //check
	}

	@Override
	public boolean equals(Object obj) {
		PaFactoid other = (PaFactoid) obj;
		if (this.lhs == other.lhs && this.rhs == other.rhs
				|| this.lhs == other.rhs && this.rhs == other.lhs && this.field.getName().equals(other.lhs.getName())) 
			return true;
		return false;
	}

	@Override
	public String toString() {  
		return "(" + lhs.toString() + rhs.toString() + ")" +"(" + field.toString() +")" ;
		
	}

}


