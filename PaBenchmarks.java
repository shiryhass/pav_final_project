public class PaBenchmarks {



	private class Obj{
		Obj f;
		Integer i = 0;
		Integer k = 0;
	}
	
	public int NullCheck(Integer a, Integer b, Obj c)
	{
		Obj d = null;
		a = null;
		if(c == d)
			c= null;
		c.f = null;
		
		if(a == b)
			b= null;
		else
			c.i= null;

		return b+c.i;
	}
	
	
	
	
		public int SimNullCheck(Integer a, Integer b)
	{
		b = null;
		if (b == null)
			b = a;
		return b;					
	}
	
	
	public int CheckFieldToLocal(Integer a, Integer b,  Obj c)
	{
		a = c.i;
		b = c.k;
		
		return a+b;
	}



	public int testNullToField(Obj a, Obj b)
	{
		a.f = null;
		b = a.f;

		return b.i;
	}

	public int checktNew(Obj a, Obj b, Obj c)
	{
		a.f = b;
		a = new Obj();
		b = new Obj();
		if(a.i < 0 && a.k < 0)
			c = a;
		else
			c = b;

		return c.i + c.k;
	}

	public int checkLocalToLocal(Integer a, Integer b, Integer c)
	{
		a = new Integer(1); 
		b = new Integer(2);

		if(a == c)
			c = a;
		if(b == c)
			c = b;	
	
		return c;
	}
	

	public int CheckLocalToLocalField(Integer a, Integer b, Obj c)
	{
		c.f = new Obj();
		c.i = a;
		c.k = b;

		return c.i+c.k;
	}
	
}
