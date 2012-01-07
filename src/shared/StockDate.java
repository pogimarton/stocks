package shared;

import java.io.Serializable;

public final class StockDate implements Comparable<StockDate>, Serializable {
	private static final long serialVersionUID = 1L;
	private final int year;
	private final int month;
	private final int day;
	
	public StockDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockDate other = (StockDate) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String y=getYear()+"";
		String m;
		String d;
		if(getMonth()<10)
		{
			m="0"+getMonth();
		}
		else
		{
			m=getMonth()+"";
		}
		if(getDay()<10)
		{
			d="0"+getDay();
		}
		else
		{
			d=getDay()+"";
		}
		return ""+y+"-"+m+"-"+d+"";
	}

	public int compareTo(StockDate o) {
		if (this == o)
			return 0;
		if (o == null)
			throw new ClassCastException ();
		
		
		int diff = year - o.year;
		if (diff != 0)
			return diff;
		
		diff = month - o.month;
		if (diff != 0)
			return diff;
		
		diff = day - o.day;
		return diff;
	}
}
