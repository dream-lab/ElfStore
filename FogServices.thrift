namespace java com.dreamlab.edgefs.thrift
namespace py fogclient

//just for testing

typedef i32 int32

//know this name is shitty but the brain is dead currently
struct NodeInfoPrimary {
	1: required string NodeIP;
	2: required i32 port;
}

//Node Class
struct NodeInfoData {
	//1: required string NodeID;
	1: required i16 nodeId;
	2: required string NodeIP;
	3: required i32 port;
}

//Fog Info Class
struct FogInfoData {
	1: required NodeInfoData nodeInstance;
	2: required double reliability;
}

//Edge Info Class
struct EdgeInfoData {
	1:required i16 nodeId;
	2:required string nodeIp;
	3:required i32 port;
	4:required byte reliability;
	5:required byte storage;
}

//Neighbor Class
struct NeighborInfoData {
	1: required NodeInfoData nodeInstance;
	//2: required string buddyPoolId;
	2: required i16 buddyPoolId;
	3: required double pool_reliability;
	4: required i16 pool_size;
}

//Two Phase PRE-COMMIT precommit request
struct TwoPhasePreCommitRequest {
	1: required string requestType;
	2: required NodeInfoData joiningNode;
	3: required i16 coordinatorId;
}

//Two Phase PRE-COMMIT precommit response
struct TwoPhasePreCommitResponse {
	1: required string responseType;
}

//Two Phase COMMIT request
struct TwoPhaseCommitRequest {
	1: required string requestType;
	2: required i16 nodeId;
	3: required i16 coordinatorId;
}

//Two Phase COMMIT response
struct TwoPhaseCommitResponse {
	1: required string responseType;
	2: optional i16 buddyPoolId;
}

//add other message types here
enum MessageType {
	BUDDY_JOIN,
	BUDDY_HEARTBEAT,
	NEIGHBOUR_HEARTBEAT
}

//message payload
//this can vary based on the messagetype 
//due to this, keeping most fields optional is a safe bet
//and handling based on messagetype looks a safe approach with
//some care to be taken
struct MessagePayload {
	1:required i16 nodeId;
	2:optional string ip;
	3:optional i32 port;
	//this will be set when sending message to subscriber
	4:optional i16 poolId;
	//this is not necessary for now
	5: optional double reliability;
	
	//add the bloom filter and related fields here
	6:optional binary bloomFilterUpdates;
	//this is 10 bytes
	7:optional list<binary> coarseGrainedStats;
	
	//the neighbor heartbeat can utilize this field to send its poolSize
	8:optional i16 poolSize;
	//buddies can get their poolSizeMap to each other to get a total count(N)
	9:optional map<i16, i16> poolSizeMap;
	
}

struct BuddyPayload {
	1:required binary payload;
}

struct NeighborPayload {
	1:required binary payload;
}


//optional becayse we need not send any update if the values are same previous time and this time
struct EdgePayload {
	1:required i16 edgeId;
	2:optional byte encodedStorage;
	//this is not changing for now but can change in the future
	3:optional byte reliability;
}

//this is the return type when a new joined node requests
//neighbor counts from its pool members by first trying to
//acquire a lock on each of them
struct NeighborCount {
	1:required bool isLockAcquired;
	//the two optional fields must be populated whenever
	//the lock acquisition is successful
	//else no need to populate them
	2:optional map<i16,i16> neighborCountPerPool;
	3:optional NodeInfoData nodeInfoData;
}

/** The metadata of a stream at register time is sent as a string with key value pairs **/
/** The metadata of a micro-batch which is sent before a write to Fog **/
/*
struct Metadata {
	1: required string mbId,
	2: required string streamId,
	3: required i64 timestamp,
	4: optional string checksum,
	//this is similar to key value pairs as received for the stream
	5: optional string properties;
}
*/

enum WritePreference {
	HHL,
	HLH,
	HHH,
	HLL,
	LHL,
	LLH,
	LHH,
	LLL
}

struct WritableFogData {
	1:required NodeInfoData node;
	2:required WritePreference preference;
	//this is necessary when the client finds out this Fog cannot serve
	//its request, so it can again contact a Fog to find replicas for the 
	//data by recalculating the reliability needed now while also including
	//the list of blacklisted Fog devices to not choose from
	3:required double reliability;
	4:optional EdgeInfoData edgeInfo;
}

//as per the new design, we want the metadata to consist of two types of
//properties, one that is static and propagated via bloom filters and other
//which is updatable where new fields can be added and old dynamic fields can
//be modified or deleted. For this, each field should have a updatable flag
//associated with it with true meaning that the field can be updated 

############################################################################
########################## STREAM METADATA #################################
############################################################################

//currently the approach is to have a struct for every type that we can have
//going forward this may change but for now, nothing easier than this is coming
//to mind so going ahead with this. Even to support this, we may need to add more
//types if needed

//no need for I8 as byte is provided which is equivalent
struct I16TypeStreamMetadata {
	1: required i16 value;
	2: required bool updatable;
}

struct I32TypeStreamMetadata {
	1: required i32 value;
	2: required bool updatable;
}

struct I64TypeStreamMetadata {
	1: required i64 value;
	2: required bool updatable;
}

struct DoubleTypeStreamMetadata {
	1: required double value;
	2: required bool updatable;
}

struct ByteTypeStreamMetadata {
	1: required byte value;
	2: required bool updatable;
}

struct StringTypeStreamMetadata {
	1: required string value;
	2: required bool updatable;
}

//this is to support the owner of the stream
struct NodeInfoPrimaryTypeStreamMetadata {
	1: required NodeInfoPrimary value;
	2: required bool updatable;
}

//this is to allow dynamic properties in the stream metadata.
//this can support only the primitive types as all primitives
//can be directly converted to their respective classes (clazz)
//using the string value
struct DynamicTypeStreamMetadata {
	1: required string value;
	2: required string clazz;
	3: required bool updatable;
}

struct StreamMetadata {
	1: required string streamId;
	//this is not an updatable property
	2: required I64TypeStreamMetadata startTime;
	//this will be updatable once stream is about to be closed
	3: optional I64TypeStreamMetadata endTime;
	4: required DoubleTypeStreamMetadata reliability;
	5: required ByteTypeStreamMetadata minReplica;
	6: required ByteTypeStreamMetadata maxReplica;
	//you create a stream with version 0 and everytime you fetch
	//the stream metadata, you also get the version back
	7: required I32TypeStreamMetadata version;
	//initially the client calling the create method for the stream
	//need not pass the owner information as the Fog node contacted
	//will become the owner of the stream
	//NOTE::this is not an updatable property
	8: optional NodeInfoPrimaryTypeStreamMetadata owner;
	9: optional map<string, DynamicTypeStreamMetadata> otherProperties;
}

struct SQueryRequest {

	// static properties which were registered during stream registration
	1: optional I64TypeStreamMetadata startTime;
	2: optional DoubleTypeStreamMetadata reliability;
	3: optional ByteTypeStreamMetadata minReplica;
	4: optional ByteTypeStreamMetadata maxReplica;
	5: optional I32TypeStreamMetadata version;
	6: optional NodeInfoPrimaryTypeStreamMetadata owner;	

}

struct SQueryResponse {
	1: required byte status;
	2: required list<string> streamList;
}

struct StreamMetadataInfo {
	1: required StreamMetadata streamMetadata;
	2: required bool cached;
	//assuming this time is the local time at the Fog when the metadata
	//was cached at the Fog
	3: optional i64 cacheTime;
}

struct Metadata {
	1: required string clientId;
	2: required string sessionSecret;
	3: required string streamId;
	4: required i64 mbId;
	5: required i64 timestamp;
	//this is optional since if write is not routed through
	//the Fog but directly to the Edge, then insertMetadata
	//will supply the checksum else it will be computed at
	//the Fog as the data is present for that case
	6: optional string checksum;
	//this is similar to key value pairs as received for the stream
	7: optional string properties;
}

struct ReadResponse {
	1: required byte status;
	2: optional binary data;
	3: optional EdgeInfoData edgeInfo;
	4: optional Metadata metadata;
}

struct FindResponse {
	1: required byte status;
	2: optional list<binary> data;
}

struct FindReplica {
	1: optional NodeInfoData node;
	2: optional EdgeInfoData edgeInfo;
}

struct ReadReplica {
	1: required byte status; //1 success
	2: optional binary data;
	3: optional Metadata metadata;
}

struct QueryReplica {
	//1: required map<string, list<NodeInfoData>> matchingNodes;
	1: required map<i64, list<NodeInfoData>> matchingNodes;
}

struct WriteResponse {
	1: required byte status;
	//in case write to edge is successful, we will be sending
	//back to client the reliability of the edge, value between 1 to 100
	2: optional byte reliability;
}

struct StreamMetadataUpdateResponse {
	1: required byte status;
	//code will distinguish between the types of failure
	2: required byte code;
	3: optional string message;
}

struct OpenStreamResponse {
	1: required byte status;
	//in case of failure to open, we can send a message as well
	//no need to set in case of success case
	2: optional string message;
	// in milliseconds
	3: optional i32 leaseTime;
	4: optional string sessionSecret;
	5: optional i64 lastBlockId;
}

struct StreamLeaseRenewalResponse {
	1: required byte status;
	2: required byte code;
	3: optional i32 leaseTime;
}
	

//this kind of pattern of attaching a message as well as code
//is good to provide client the necessary information to react
//as per the code returned
struct BlockMetadataUpdateResponse {
	1: required byte status;
	2: required string message;
	//adding the code field and having some handling at the client
	//side gives the client information about what's happening at
	//the server side. If there are any failures, what type of failure
	//it is, Fog is down or version mismatch
	3: required byte code;
}

// the interfaces belonging to Fog Interface
service FogService {

	//join cluster , will return candidate buddy belonging to different pools, the number is bewtween pmin and pmax
	list<NeighborInfoData> joinCluster(1:FogInfoData NodeX,2:i16 pmin,3:i16 pmax);

	TwoPhaseCommitResponse joinPool(1:FogInfoData NodeX);

	list<NeighborInfoData> getCandidatePool(1:double reliability, 2:i16 pmin);

	string bootstrapFog(1:FogInfoData NodeX);

	TwoPhasePreCommitResponse initiate2PhasePreCommit(1:TwoPhasePreCommitRequest preCommitRequest);

	TwoPhaseCommitResponse initiate2PhaseCommit(1:TwoPhaseCommitRequest commitRequest);

	//oneway void heartBeat(1:MessageType msgType, 2:NodeInfoData node, 3:MessagePayload payload);
	
	//oneway void heartBeat(1:MessagePayload payload);
	
	oneway void buddyHeartBeat(1:BuddyPayload payload);
	
	oneway void neighborHeartBeat(1:NeighborPayload payload);

	NodeInfoData getPoolMember(1:i16 buddyPoolId);

	list<FogInfoData> getBuddyPoolMembers();
	
	//this is for acquiring lock and getting the count of eighbors from each pool of every buddy when a new node wants to collect neighbors
	NeighborCount getNeighborCountPerPool();
	list<NeighborInfoData> requestNeighbors(1:map<i16, i16> requestMap);
	
	//once a joining node completes the process i.e. gets buddies and acquires neighbors from its buddies, it can tell its buddies to release the lock
	void nodeJoiningComplete();
	
	bool subscribe(1:NodeInfoData nodeInfoData);	
		
	//These next set of services are the ones that are more useful in terms of the nearest goal
	bool edgeHeartBeats(1:EdgePayload edgePayload);//expected to be called from a client

	// Device Management API
	//This is for Edge to join a particular fog 
	byte edgeJoin(1:EdgeInfoData  edgeInfoData);

	//The edge may want to leave the cluster
	byte edgeLeave(1: EdgeInfoData edgeInfoData);

	//Terminate will remove the entry of a stream
	byte terminate(1: string streamId);
	
	//Data management APIs
	
	//register stream with a Fog assuming client knows which Fog to contact
	byte registerStream(1: string streamId, 2: StreamMetadata streamMetadata, 3:i64 startSequenceNumber);
	
	// Returns a sessionID 
	string intentToWrite(1: byte clientId);

	//StreamMetadata getStreamMetadata(1:string streamId, 2:bool checkNeighbors, 3:bool checkBuddies);
	StreamMetadataInfo getStreamMetadata(1:string streamId, 2:bool checkNeighbors, 3:bool checkBuddies,
											4:bool forceLatest);
	
	StreamMetadata getStreamMetadataFromOwner(1:string streamId);
	
	//Returns a list of Fog Locations
	//list<WritableFogData> getWriteLocations(1: byte dataLength, 2: Metadata metadata, 
	//										3: list<i16> blackListedFogs, 4:EdgeInfoData selfInfo);
	list<WritableFogData> getWriteLocations(1: byte dataLength, 2: Metadata metadata, 
											3: list<i16> blackListedFogs, 4:bool isEdge);
	
	//byte write(1:Metadata mbMetadata, 2:binary data, 3:WritePreference preference);
	WriteResponse write(1:Metadata mbMetadata, 2:binary data, 3:WritePreference preference);

	// does a test and set tupe of thing, returns the same set of locations as done previously
	list<NodeInfoData> writeNext(1: string sessionId, 2: Metadata mbData, 3: byte dataLength);

	//Insert metadata once the write of mictobatch is succesfully completed
	//if the client is directly writing to an edge device, it should make a call
	//to the fog after completion to given the metadata which has edgeId as one of
	//the attributes. If client writes via the fog, on completion of the write it 
	//will store the metadata as well, edgeInfo will set only the edgeID
	
	byte insertMetadata(1: Metadata mbMetadata, 2: EdgeInfoData edgeInfoData);

	// Find the next micro bactch satisfying the query
	binary findNext(1: string microbatchId);
	
	//list<FindReplica> find(1: string microbatchId, 2:bool checkNeighbors, 3:bool checkBuddies,
		//					4:EdgeInfoData selfInfo);
							
	list<FindReplica> find(1: i64 microbatchId, 2:bool checkNeighbors, 3:bool checkBuddies,
							4:EdgeInfoData selfInfo);						
							
	//ReadReplica read(1: string microbatchId, 2:bool fetchMetadata);
	ReadReplica read(1: i64 microbatchId, 2:bool fetchMetadata);

	QueryReplica findUsingQuery(1: string metadataKey, 2:string metadataValue, 3:bool checkNeighbors, 4:bool checkBuddies);
	
	//only returning metadata in this operation
	//ReadReplica getMeta(1: string microbatchId, 2:bool checkNeighbors, 3:bool checkBuddies);
	ReadReplica getMeta(1: i64 microbatchId, 2:bool checkNeighbors, 3:bool checkBuddies);
	
	byte serializeState();
	
	//updating the StreamMetadata
	StreamMetadataUpdateResponse updateStreamMetadata(1: StreamMetadata metadata);
	
	//open a stream for putting blocks
	OpenStreamResponse open(1: string streamId, 2: string clientId, 3: i32 expectedLease, 4: bool setLease);
	
	//client will start writing by issuing putNext calls
	WriteResponse putNext(1:Metadata mbMetadata, 2:binary data, 3:WritePreference preference);
	
	//once block is written, increment the block count at the owner Fog
	BlockMetadataUpdateResponse incrementBlockCount(1:Metadata mbMetadata, 2:bool setLease);
	
	StreamLeaseRenewalResponse renewLease(1:string streamId, 2:string clientId, 3:string sessionSecret,
											 4:i32 expectedLease, 5:bool setLease);
	
	//this is not used generally, only used to get the largest blockId persisted to a particular stream.
	//This will be used when we are using discontinuous blockIds during different phases of experiment
	//without doing resetting										 
	i64 getLargestBlockId(1:string streamId); 

	//findstream searches for streams that match a given set of static stream properties provided in the squery
	SQueryResponse findStream(1: SQueryRequest squery);

	//update api, where the data of the previous block is overwritten
	WriteResponse updateBlock(1:i64 mbId, 2:Metadata mbMetadata, 3:binary mbData);

	//ISHAN:
	//flag is a dummy variable
	//returns the mbids in a local partition
	//this is mostly for testing purposes, but will be provided as a 'choice' in the EdgeClient.py file
	set<i64> listLocalPartitionMbId(1:bool flag);
	
	// used to get neighbours of a fog
	list<NeighborInfoData> requestAllNeighbors();
	
	//used to return mbIDLocationMap to the client
	map<i64,map<i16,byte>> requestMbIDLocationMap();	
}
