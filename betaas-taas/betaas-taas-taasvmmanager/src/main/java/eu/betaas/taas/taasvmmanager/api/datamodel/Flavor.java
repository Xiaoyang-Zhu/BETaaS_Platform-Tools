package eu.betaas.taas.taasvmmanager.api.datamodel;

public class Flavor {
	public enum FlavorType {tiny, small, standard}
	
	private FlavorType type;
	private int vCpu;
	private long memory;
	private long disk;
	
	public Flavor() {}
	
	public Flavor(FlavorType type, int vCpu, long memory, long disk) {
		this.type = type;
		this.vCpu = vCpu;
		this.memory = memory;
		this.disk = disk;
	}
	
	public FlavorType getType() {
		return type;
	}
	
	public void setType(FlavorType type) {
		this.type = type;
	}
	
	public int getvCpu() {
		return vCpu;
	}
	
	public void setvCpu(int vCpu) {
		this.vCpu = vCpu;
	}
	
	public long getMemory() {
		return memory;
	}
	
	public void setMemory(long memory) {
		this.memory = memory;
	}
	
	public long getDisk() {
		return disk;
	}
	
	public void setDisk(long disk) {
		this.disk = disk;
	}
	
	public Flavor clone() {
		return new Flavor(type, vCpu, memory, disk);
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null
			 && obj instanceof Flavor
			 && ((Flavor)obj).getMemory() == memory
			 && ((Flavor)obj).getvCpu() == vCpu;
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
}
