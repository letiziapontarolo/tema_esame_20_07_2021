package it.polito.tdp.yelp.model;

public class Arco {
	
	User u1;
	User u2;
	int grado;
	public Arco(User u1, User u2, int grado) {
		super();
		this.u1 = u1;
		this.u2 = u2;
		this.grado = grado;
	}
	public User getU1() {
		return u1;
	}
	public void setU1(User u1) {
		this.u1 = u1;
	}
	public User getU2() {
		return u2;
	}
	public void setU2(User u2) {
		this.u2 = u2;
	}
	public int getGrado() {
		return grado;
	}
	public void setGrado(int grado) {
		this.grado = grado;
	}
	
	

}
