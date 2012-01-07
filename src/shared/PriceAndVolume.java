package shared;

import java.io.Serializable;

public class PriceAndVolume implements Serializable {
	private static final long serialVersionUID = 1L;
	private double	Price	= 0;
	private int		volume	= 0;
	
	
	
	public PriceAndVolume() {
	}

	public PriceAndVolume(double Price, int volume) {
		this.Price = Price;
		this.volume = volume;
	}

	public double getPrice() {
		return Price;
	}

	public int getVolume() {
		return volume;
	}
	
	public void setPrice(double Price) {
		this.Price = Price;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "PriceAndVolume [Price=" + Price + ", volume=" + volume + "]";
	}
}
