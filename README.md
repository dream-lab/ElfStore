# ElfStore: Edge-local federated Store

Sumit K Monga, Sheshadri K R, Yogesh Simmhan.

ElfStore, a first-of-its-kind edge-local federated store for streams of data blocks. It uses reliable fog devices as a super-peer
overlay to monitor the edge resources, over federated metadata indexing using Bloom filters, locates data within 2-hops, and maintains approxi
mate global statistics about the reliability and storage capacity of edges. Edges host the actual data blocks, and we use a unique diferential replication scheme to select edges on which to replicate blocks, to guarantee a minimum reliability and to balance storage utilization.

Command Line Interface (CLI) , credits : Ishan Sharma (ishandnr@gmail.com)

Link to the paper : https://arxiv.org/pdf/1905.08932.pdf

Instructions to setup:

1. maven compile to generate executable jar at the level of 'src' directory. ( mvn clean compile assembly:single )
2. For setup and installation of Elfstore, please refer the Readme file which contain step-by-step instructions to test the Elfstore using the Command Line Interface (CLI) in your local machine.

Note: These software are research prototypes and made available on a best-effort basis, without any guarantees 🙂 If you have any questions or comments, you can sent the respective author a note. 


####   Copyright 2019 DREAM:Lab, Indian Institute of Science, Bangalore

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
