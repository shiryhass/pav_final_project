package pa;
import soot.PackManager;
import soot.Transform;
import bgu.cs.absint.soot.BaseAnalysis;

public class PaMain {
	public static void main(String[] args) {
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.CPAnalysis", new CPAnalysis()));
		soot.Main.main(args);
	}

	public static class CPAnalysis extends BaseAnalysis<PaState, PaDomain> {
		public CPAnalysis() {
			super(PaDomain.v());
		}
	}
}

