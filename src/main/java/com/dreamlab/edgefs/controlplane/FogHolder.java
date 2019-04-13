package com.dreamlab.edgefs.controlplane;

public class FogHolder {

	private Fog fog;

	public FogHolder() {
		
	}
	
	public FogHolder(Fog fog) {
		super();
		this.fog = fog;
	}
	
	public Fog getFog() {
		return fog;
	}

	public void setFog(Fog fog) {
		this.fog = fog;
	}
	
}
