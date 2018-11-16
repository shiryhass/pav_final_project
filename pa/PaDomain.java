package pa;

import java.util.Iterator;

import bgu.cs.absint.ComposedOperation;
import bgu.cs.absint.Domain;
import bgu.cs.absint.IdOperation;
import bgu.cs.absint.UnaryOperation;
import bgu.cs.absint.soot.ForgetVarTransformer;
import bgu.cs.absint.soot.TransformerMatcher;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.SootField;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.jimple.internal.JimpleLocal;

	/**
	 * Implementation of abstract operations for a static analysis for the Zone
	 * abstract domain.
	 * 
	 * @author ???
	 * 
	 */
	public class PaDomain extends Domain<PaState, Unit> {
		public static final Local ZERO_VAR = new JimpleLocal("NULL", IntType.v());
		/**
		 * Singleton value.
		 */
		private static final PaDomain v = new PaDomain(); 

		/**
		 * Matches statements to transformers.
		 */
		protected PasMatcher matcher = new PasMatcher(); //TODO

		public static final PaDomain v() {
			return v;
		}

		@Override
		public PaState getBottom() {
			return PaState.bottom;
		}

		@Override
		public PaState getTop() {
			return PaState.top;
		}


		@Override
		public boolean leq(PaState first, PaState second) {
			boolean result;
			if (first == PaState.bottom || second == PaState.top) {
				result = true;
			} else if (second == PaState.bottom) {
				result =  false;
			} else {
				result = second.factoids.containsAll(first.factoids);
			}
		return result;
	}

	

		@Override
		public UnaryOperation<PaState> getTransformer(Unit stmt) {
			UnaryOperation<PaState> vanillaTransformer = matcher.getTransformer(stmt);
			if (vanillaTransformer.equals(IdOperation.v())) {
				// An optimization - no need to run a reduction after an identity
				// transformer.
				return vanillaTransformer;
				
			} else {
				return ComposedOperation.compose(vanillaTransformer, getReductionOperation());
			}
		}



		/**
		 * Singleton pattern.
		 */
		private PaDomain() {
		}

		/**
		 * A helper class for matching transformers to statements.
		 * 
		 * @author romanm
		 */
		protected class PasMatcher extends TransformerMatcher<PaState> {
			@Override
			public void matchAssignToLocal(AssignStmt stmt, Local lhs) {
				super.matchAssignToLocal(stmt, lhs);
				if (transformer == null)
					transformer = new ForgetVarTransformer<PaFactoid, PaState>(lhs);
			}

			@Override
			public void matchIdentityStmt(IdentityStmt stmt, Local lhs, ParameterRef rhs) {
				transformer = new ForgetVarTransformer<PaFactoid, PaState>(lhs);
			}

			@Override
			public void matchIdentityStmt(IdentityStmt stmt, Local lhs, ThisRef rhs) {
				transformer = new ForgetVarTransformer<PaFactoid, PaState>(lhs);
			}
			
			/**
		 * Matches assignments of the form {@code x=y} where both 'x' and 'y' are
		 * locals.
		 */

			@Override
			public void matchAssignLocalToLocal(AssignStmt stmt, Local lhs, Local rhs){
				if (rhs.equals(PaFactoid.NULL_VAR)) {
					transformer =  new TransformerAssignLocalToNull(lhs);
				} else if (lhs.equals(rhs)) {
						transformer = IdOperation.v();
						}
					else
						transformer = new TransformerAssignLocalToLlocal(lhs, rhs);
			}

	 /**
	 * Matches statements of the form {@code x=y.f} where 'x' and 'y' are local
	 * variables.
	 */

	@Override
	public void matchAssignInstanceFieldRefToLocal(AssignStmt stmt, Local lhs,
			Local rhs, SootField field) {
				transformer = new TransformerAssignFieldRefToLocal(lhs, rhs, field);
			}			
	/**
	 * Matches statements of the form {@code x.n = y} where 'x' and 'y' are
	 * local variables.
	 */
	public void matchAssignLocalToInstanceFieldRef(Local base, SootField field,
			Local rhs) {
	
		transformer = new TransformerLocalAssignToFieldRef(base, field, rhs);
	}

	/**
	 * Matches statements of the form {@code x.n = null} where 'x' is a local
	 * variable.
	 */
	public void matchAssignNullToInstanceFieldRef(Local base, SootField field) {
		transformer = new TransformerAssignNullToInstanceFieldRef(base, field);
	}
	
	/**
	 * @override
	 * Matches statements of the form {@code x = new T} where 'x' is a local
	 * variable.
	 */
	public void matchAssignNewToLocal(AssignStmt stmt, Local lhs,
				RefType baseType) {
			transformer = new TransformerAssignNewToLocal(lhs, baseType);
			}
	}	


		@Override
		public PaState ub(PaState first, PaState second) {
			PaState result = new PaState();
		if (first == PaState.bottom || second == PaState.top) {
			return second;
		} else if (second == PaState.bottom || first == PaState.top ) {
			return first;			
		  } 
			else {
				PaState first_copy = first.copy();
				PaState second_copy = second.copy();
				//Iterator<Local> iter = first_copy.getVars().iterator(); iter.hasNext();
				for (Iterator<PaFactoid> iter_f = first_copy.iterator(); iter_f.hasNext();) {
					PaFactoid var_f = iter_f.next();
					if (! result.factoids.contains(var_f)){
						result.add(var_f);
					}
				}
				for (Iterator<PaFactoid> iter_s = second_copy.iterator(); iter_s.hasNext();) {
					PaFactoid var_s = iter_s.next();
					if (! result.factoids.contains(var_s)){
						result.add(var_s);
					}
					
				}	
			}
		return result;
	}
		}





/**
			 * 
		@Override
		public PaState ub(PaState elem1, PaState elem2) {
			// TODO Auto-generated method stub
			return null;
		}

			/**
			 * Handle statements of the form x=a+b where 'a' and 'b' are either
			 * variables of constants.
			 */
		/*	@Override
			public void matchAssignAddLocalLocalToLocal(AssignStmt stmt, Local lhs, Local op1, Local op2) {
				if (lhs.equivTo(op1)) {
					//transformer = new TransformerAssignIncrementLocalByLocal(lhs, op2);
				} else if (lhs.equivTo(op2)) {
					//transformer = new TransformerAssignIncrementLocalByLocal(lhs, op1);
				} else {
					//transformer = new TransformerAssignAddLocalLocalToLocal(lhs, op1, op2);
				}
			}

			/**
			 * Matches statements of the form {@code x=y+c} and {@code x=c+y}.
			 */
		/*	@Override
			public void matchAssignAddLocalConstantToLocal(AssignStmt stmt, Local lhs, Local op1, Constant op2) {
				if (op2 instanceof IntConstant) {
					// Special case x=x+c.
					if (lhs.equivTo(op1)) ;
						//transformer = new TransformerAssignIncrementLocalByConstant(lhs, (IntConstant) op2);
					// General case.
					//else
						//transformer = new TransformerAssignAddLocalConstantToLocal(lhs, op1, (IntConstant) op2);
				}
			}

			/**
			 * Matches statements of the form {@code x=y-c}.
			 */
			//@Override
		/*	public void matchAssignSubLocalConstantToLocal(AssignStmt stmt, Local lhs, Local op1, Constant op2) {
				op2 = ((IntConstant) op2).multiply(IntConstant.v(-1));
				if (op2 instanceof IntConstant) {
					// Special case x=x+c.
					if (lhs.equivTo(op1));
						//transformer = new TransformerAssignIncrementLocalByConstant(lhs, (IntConstant) op2);
					// General case.
					//else
						//transformer = new TransformerAssignAddLocalConstantToLocal(lhs, op1, (IntConstant) op2);
				}
			}

			@Override
			public void matchAssignMulLocalConstantToLocal(AssignStmt stmt, Local lhs, Local op1, Constant op2) {
				if (op2 instanceof IntConstant);
					//transformer = new TransformerAssignMulLocalConstantToLocal(lhs, op1, (IntConstant) op2);
			}

			@Override
			public void matchAssumeLocalEqConstant(IfStmt stmt, boolean polarity, Local lhs, Constant rhs) {
				if (rhs instanceof IntConstant);
					transformer = new TransformerAssumeLocalEqConstant(polarity, lhs, (IntConstant) rhs);
			}

			@Override
			public void matchAssumeLocalEqLocal(IfStmt stmt, boolean polarity, Local lhs, Local rhs) {
				transformer = new TransformerAssumeLocalEqLocal(polarity, lhs, rhs);
			}

			/**
			 * Matches statements of the form x<y for two local variables 'x' and
			 * 'y'.
			 */
		/*	public void matchAssumeLocalLtLocal(IfStmt stmt, boolean polarity, Local lhs, Local rhs) {
				//transformer = new TransformerAssumeLocalLtLocal(polarity, lhs, rhs);
			}

			/**
			 * Matches statements of the form x>y for two local variables 'x' and
			 * 'y'.
			 */
		/*	public void matchAssumeLocalGtLocal(IfStmt stmt, boolean polarity, Local lhs, Local rhs) {
				assert false : "should not be matched!";
			}

			/**
			 * Matches statements of the form x<c for a local variable 'x' and
			 * constant 'c'.
			 */
		/*	public void matchAssumeLocalLtConstant(IfStmt stmt, boolean polarity, Local lhs, Constant rhs) {
				if (rhs instanceof IntConstant);
					transformer = new TransformerAssumeLocalLtConstant(polarity, lhs, (IntConstant) rhs);
			}

			/**
			 * Matches statements of the form x>c for a local variable 'x' and
			 * constant 'c'.
			 */
			/*public void matchAssumeLocalGtConstant(IfStmt stmt, boolean polarity, Local lhs, Constant rhs) {
				assert false : "should not be matched!";
			}
		}
		

		@Override
		public PaState ub(PaState elem1, PaState elem2) {
			// TODO Auto-generated method stub
			return null;
		}*/


