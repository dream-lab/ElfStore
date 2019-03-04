package com.dreamlab.edgefs.controlplane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.model.StorageReliability;

/**
 * 
 * @author swamiji
 *
 */
public class LocalReplicaAllocation {
	
	

	public EdgeInfo allocateLocalReplica(FogStats fStat,Map<StorageReliability, Set<Short>> localEdgesMap, Map<Short, EdgeInfo> fogEdges ){
		
		System.out.println("Need to select an edge > median storage");
		if(fStat.getD()>0) {
			
			ArrayList<Short> edges = new ArrayList<Short>(localEdgesMap.get(StorageReliability.HH));
			int randRange = (new Random().nextInt(edges.size()));
			return fogEdges.get(edges.get(randRange));
			
		}else if(fStat.getB()>0) {
			
			ArrayList<Short> edges = new ArrayList<Short>(localEdgesMap.get(StorageReliability.HL));
			int randRange = (new Random().nextInt(edges.size()));
			return fogEdges.get(edges.get(randRange));
			
		}else if(fStat.getC()>0) {
			ArrayList<Short> edges = new ArrayList<Short>(localEdgesMap.get(StorageReliability.LL));
			int randRange = (new Random().nextInt(edges.size()));
			return fogEdges.get(edges.get(randRange));
			
		}else {
			ArrayList<Short> edges = new ArrayList<Short>(localEdgesMap.get(StorageReliability.LH));
			int randRange = (new Random().nextInt(edges.size()));
			return fogEdges.get(edges.get(randRange));
			
		}

	}
}
