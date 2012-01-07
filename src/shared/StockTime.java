package shared;

import java.io.Serializable;

public class StockTime implements Comparable<StockTime>, Serializable {
	private static final long serialVersionUID = 1L;
	private final int		hours;
	private final int		minutes;
	private final int		seconds;
	
	public StockTime(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minutes;
		result = prime * result + seconds;
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
		StockTime other = (StockTime) obj;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		if (seconds != other.seconds)
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		String h;
		String m;
		String s;
		
		if(hours<10)
		{
			h="0"+hours;
		}
		else
		{
			h=hours+"";
		}
		
		if(minutes<10)
		{
			m="0"+minutes;
		}
		else
		{
			m=minutes+"";
		}
		
		if(seconds<10)
		{
			s="0"+seconds;
		}
		else
		{
			s=seconds+"";
		}
		

		return "" + h + ":" + m+ ":" + s + "";
	}

	public int compareTo(StockTime o) {
		
		if (this == o)
			return 0;
		if (o == null)
			throw new ClassCastException ();
		
		int diff = hours - o.hours;
		if (diff != 0)
			return diff;
		
		diff = minutes - o.minutes;
		if (diff != 0)
			return diff;
		
		diff = seconds - o.seconds;
		return diff;
	}
}
