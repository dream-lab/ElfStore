/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

# Thrift Tutorial
# Mark Slee (mcslee@facebook.com)
#
# This file aims to teach you how to use Thrift, in a .thrift file. Neato. The
# first thing to notice is that .thrift files support standard shell comments.
# This lets you make your thrift file executable and include your Thrift build
# step on the top line. And you can place comments like this anywhere you like.
#
# Before running this file, you will need to have installed the thrift compiler
# into /usr/local/bin.

/**
 * The first thing to know about are types. The available types in Thrift are:
 *
 *  bool        Boolean, one byte
 *  i8 (byte)   Signed 8-bit integer
 *  i16         Signed 16-bit integer
 *  i32         Signed 32-bit integer
 *  i64         Signed 64-bit integer
 *  double      64-bit floating point value
 *  string      String
 *  binary      Blob (byte array)
 *  map<t1,t2>  Map from one type to another
 *  list<t1>    Ordered list of one type
 *  set<t1>     Set of unique elements of one type
 *
 */

// Just in case you were wondering... yes. We support simple C comments too.



/**
 * Thrift lets you do typedefs to get pretty names for your types. Standard
 * C style here.
 */
namespace java com.dreamlab.edgefs.thrift
typedef i32 MyInteger


struct ParentFog {
	1: required i16 nodeId,
	2: required string nodeIp,
	3: required i32 port,
}
	
struct Metadata {
	1: required string mbId,
	2: required string streamId,
	3: required i64 timestamp, //This is actually a metadata
	//this is similar to key value pairs as received for the stream
	4: optional string properties;
}

struct EdgeInfoData {
	1:required i16 nodeId;
	2:required string nodeIp;
	3:required i32 port;
	4:required byte reliability;
	5:required byte storage;
}

struct ReadResponse {
	1: required byte status;
	2: optional binary data;
	3: optional EdgeInfoData edgeInfo;
	4: optional Metadata metadata;
}

struct ReadReplica {
	1: required byte status;
	2: optional binary data;
	3: optional Metadata metadata;
}

struct WriteResponse {
	1: required byte status;
	//in case write to edge is successful, we will be sending
	//back to client the reliability of the edge, value between 1 to 100
	2: optional byte reliability;
}


service EdgeService {

  /**
   * A method definition looks like C code. It has a return type, arguments,
   * and optionally a list of exceptions that it may throw. Note that argument
   * lists and exception lists are specified using the exact same syntax as
   * field lists in struct or exception definitions.
   */

   void pong(),

   byte insert(1: binary data),

   i32 add(1:i32 num1, 2:i32 num2),

   //byte write(1:string mbId, 2:Metadata mbMetadata, 3:binary mbData),
   
   WriteResponse write(1:string mbId, 2:Metadata mbMetadata, 3:binary mbData),

   ReadReplica read(1:string mbId, 2:byte fetchMetadata),
   
   //this only returns the metadata
   ReadReplica getMetadata(1:string mbId),

   /**
    * This method has a oneway modifier. That means the client only makes
    * a request and does not listen for any response at all. Oneway methods
    * must be void.
    */
   oneway void zip()

}

/**
 * That just about covers the basics. Take a look in the test/ folder for more
 * detailed examples. After you run this file, your generated code shows up
 * in folders with names gen-<language>. The generated code isn't too scary
 * to look at. It even has pretty indentation.
 */
